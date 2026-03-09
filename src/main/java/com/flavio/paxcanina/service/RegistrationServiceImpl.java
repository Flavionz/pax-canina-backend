package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.AgeGroupDao;
import com.flavio.paxcanina.dao.DogDao;
import com.flavio.paxcanina.dao.RegistrationDao;
import com.flavio.paxcanina.dao.SessionDao;
import com.flavio.paxcanina.exception.BusinessException;
import com.flavio.paxcanina.exception.NotFoundException;
import com.flavio.paxcanina.model.AgeGroup;
import com.flavio.paxcanina.model.Dog;
import com.flavio.paxcanina.model.Registration;
import com.flavio.paxcanina.model.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationDao registrationDao;
    private final DogDao dogDao;
    private final SessionDao sessionDao;
    private final AgeGroupDao ageGroupDao;
    private final AgeEligibilityService ageEligibilityService;

    @Override
    public Registration save(Registration registration) {
        return registrationDao.save(registration);
    }

    @Override
    public boolean existsBySessionAndDog(Session session, Dog dog) {
        return registrationDao.existsBySessionAndDog(session, dog);
    }

    /**
     * Use case: register an owner's dog to a session with full domain rules:
     * - Ownership check (dog belongs to the authenticated owner)
     * - Duplicate registration check (unique dog+session)
     * - Capacity check (maxCapacity)
     * - Age eligibility check (based on session date and session's AgeGroup)
     *
     * Returns the persisted Registration.
     * Throws BusinessException/NotFoundException with precise error codes on failure.
     */
    @Transactional
    @Override
    public Registration registerDogToSession(Integer ownerIdUser, Integer dogId, Integer sessionId) {

        Dog dog = dogDao.findById(dogId)
                .orElseThrow(() -> new NotFoundException("DOG_NOT_FOUND"));

        if (dog.getOwner() == null
                || dog.getOwner().getIdUser() == null
                || !dog.getOwner().getIdUser().equals(ownerIdUser)) {
            throw new NotFoundException("DOG_NOT_FOUND_OR_NOT_OWNED");
        }

        Session session = sessionDao.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("SESSION_NOT_FOUND"));

        if (this.existsBySessionAndDog(session, dog)) {
            throw new BusinessException("ALREADY_REGISTERED", 409);
        }

        Integer maxCapacity = session.getMaxCapacity();
        int currentRegistrations = registrationDao.countBySession(session);
        if (maxCapacity != null && currentRegistrations >= maxCapacity) {
            throw new BusinessException("SESSION_FULL", 422);
        }

        LocalDate referenceDate = session.getDate() != null ? session.getDate() : LocalDate.now();

        AgeGroup group = session.getAgeGroup();
        if (group == null) {
            throw new BusinessException("AGE_GROUP_NOT_SET_FOR_SESSION", 422);
        }

        Integer minMonths = group.getMinAge();
        Integer maxMonths = group.getMaxAge();

        int dogAgeMonthsAtSession = ageEligibilityService.calcAgeInMonths(dog.getBirthDate(), referenceDate);
        boolean eligible = ageEligibilityService.isEligible(dogAgeMonthsAtSession, minMonths, maxMonths);

        if (!eligible) {
            String code = (minMonths != null && dogAgeMonthsAtSession < minMonths)
                    ? "DOG_TOO_YOUNG" : "DOG_TOO_OLD";
            throw new BusinessException(code, 422)
                    .withDetail("ageMonths", dogAgeMonthsAtSession)
                    .withDetail("minMonths", minMonths)
                    .withDetail("maxMonths", maxMonths);
        }

        Registration reg = new Registration();
        reg.setDog(dog);
        reg.setSession(session);
        reg.setRegistrationDate(LocalDate.now());

        return registrationDao.save(reg);
    }
}
