package com.flavio.paxcanina.service;

import com.flavio.paxcanina.model.Dog;
import com.flavio.paxcanina.model.Registration;
import com.flavio.paxcanina.model.Session;

public interface RegistrationService {
    Registration save(Registration registration);
    boolean existsBySessionAndDog(Session session, Dog dog);
}
