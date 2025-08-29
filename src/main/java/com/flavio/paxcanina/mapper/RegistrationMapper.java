package com.flavio.paxcanina.mapper;

import com.flavio.paxcanina.dto.RegistrationDto;
import com.flavio.paxcanina.model.Course;
import com.flavio.paxcanina.model.Dog;
import com.flavio.paxcanina.model.Registration;
import com.flavio.paxcanina.model.Session;

public class RegistrationMapper {

    public static RegistrationDto toDto(Registration reg) {
        RegistrationDto dto = new RegistrationDto();
        dto.setId(reg.getIdRegistration());
        dto.setRegistrationDate(reg.getRegistrationDate());
        dto.setStatus(reg.getStatus());

        // Dog
        Dog dog = reg.getDog();
        if (dog != null) {
            dto.setDogId(dog.getIdDog());
            dto.setDogName(dog.getName());
        }

        // Session + Course
        Session session = reg.getSession();
        if (session != null) {
            dto.setSessionId(session.getIdSession());
            dto.setSessionName(session.getDescription());

            Course course = session.getCourse();
            if (course != null) {
                dto.setCourseId(course.getIdCourse());
                dto.setCourseName(course.getName());
            }
        }
        return dto;
    }
}
