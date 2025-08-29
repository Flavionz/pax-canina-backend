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
        // 1) Ownership
        Dog dog = dogDao.findById(dogId)
                .orElseThrow(() -> new NotFoundException("DOG_NOT_FOUND"));

        if (dog.getOwner() == null || dog.getOwner().getIdUser() == null
                || !dog.getOwner().getIdUser().equals(ownerIdUser)) {
            // Hide existence of resources not owned by the caller (security by design).
            throw new NotFoundException("DOG_NOT_FOUND_OR_NOT_OWNED");
        }

        // 2) Session existence
        Session session = sessionDao.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("SESSION_NOT_FOUND"));

        // 3) Duplicate registration
        if (registrationDao.existsBySessionAndDog(session, dog)) {
            throw new BusinessException("ALREADY_REGISTERED", 422);
        }

        // 4) Capacity
        Integer maxCapacity = session.getMaxCapacity(); // nullable in your model
        int currentRegistrations = registrationDao.countBySession(session);
        if (maxCapacity != null && currentRegistrations >= maxCapacity) {
            throw new BusinessException("SESSION_FULL", 422);
        }

        // 5) Age eligibility (calculate age at the session date, not "today")
        LocalDate referenceDate = session.getDate();
        if (referenceDate == null) {
            referenceDate = LocalDate.now();
        }

        AgeGroup group = session.getAgeGroup();
        if (group == null) {
            throw new BusinessException("AGE_GROUP_NOT_SET_FOR_SESSION", 422);
        }

        Integer minMonths = group.getMinAge();
        Integer maxMonths = group.getMaxAge();

        int dogAgeMonthsAtSession = ageEligibilityService.calcAgeInMonths(dog.getBirthDate(), referenceDate);
        boolean eligible = ageEligibilityService.isEligible(dogAgeMonthsAtSession, minMonths, maxMonths);

        if (!eligible) {
            String code = (minMonths != null && dogAgeMonthsAtSession < minMonths) ? "DOG_TOO_YOUNG" : "DOG_TOO_OLD";
            throw new BusinessException(code, 422)
                    .withDetail("ageMonths", dogAgeMonthsAtSession)
                    .withDetail("minMonths", minMonths)
                    .withDetail("maxMonths", maxMonths);
        }

        // 6) Persist Registration
        Registration reg = new Registration();
        reg.setDog(dog);
        reg.setSession(session);
        reg.setRegistrationDate(LocalDate.now()); // audit-friendly and useful for reports
        // reg.setStatus("CONFIRMED"); // set if you manage a status workflow

        return registrationDao.save(reg);
    }
}
