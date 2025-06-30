package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.RegistrationDao;
import com.flavio.paxcanina.model.Dog;
import com.flavio.paxcanina.model.Registration;
import com.flavio.paxcanina.model.Session;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationDao registrationDao;

    public RegistrationServiceImpl(RegistrationDao registrationDao) {
        this.registrationDao = registrationDao;
    }

    @Override
    public Registration save(Registration registration) {
        return registrationDao.save(registration);
    }

    @Override
    public boolean existsBySessionAndDog(Session session, Dog dog) {
        return registrationDao.existsBySessionAndDog(session, dog);
    }
}
