package com.flavio.paxcanina.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class AgeEligibilityService {

    public int calcAgeInMonths(LocalDate birthDate, LocalDate referenceDate) {
        if (birthDate == null || referenceDate == null) throw new IllegalArgumentException("dates required");
        if (referenceDate.isBefore(birthDate)) return 0;
        return (int) ChronoUnit.MONTHS.between(
                birthDate.withDayOfMonth(1),
                referenceDate.withDayOfMonth(1)
        );
    }

    public boolean isEligible(int ageMonths, Integer minMonths, Integer maxMonths) {
        if (minMonths != null && ageMonths < minMonths) return false;
        if (maxMonths != null && ageMonths > maxMonths) return false;
        return true;
    }
}
