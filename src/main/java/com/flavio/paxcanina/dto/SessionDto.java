package com.flavio.paxcanina.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class SessionDto {
    private Integer idSession;
    private LocalDate date;
    private String level; // "DEBUTANT", "INTERMEDIAIRE", "AVANCE"
    private String startTime;
    private String endTime;
    private Integer maxCapacity;
    private Integer registrationsCount;
    private String description;
    private String location;
    private String imageUrl;

    // Course relation (essential data only)
    private Integer courseId;
    private String courseName;

    // Coach relation (optional)
    private Integer coachId;
    private String coachFirstName;
    private String coachLastName;

    // AgeGroup relation (optional)
    private Integer ageGroupId;
    private String ageGroupName;
    private Integer minAge;
    private Integer maxAge;

    // Computed: "available" / "full"
    private String status;


}
