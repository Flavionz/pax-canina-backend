package com.flavio.paxcanina.dto;

import java.util.List;

public class CourseDto {
    private Integer idCourse;
    private String name;
    private String description;
    private String status;
    private String imageUrl;

    // Specializations associated with this course (list of specialization IDs)
    private List<Integer> specializationIds;

    public CourseDto() {}

    public CourseDto(Integer idCourse, String name, String description, String status, String imageUrl, List<Integer> specializationIds) {
        this.idCourse = idCourse;
        this.name = name;
        this.description = description;
        this.status = status;
        this.imageUrl = imageUrl;
        this.specializationIds = specializationIds;
    }

    // Getters & Setters
    public Integer getIdCourse() { return idCourse; }
    public void setIdCourse(Integer idCourse) { this.idCourse = idCourse; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<Integer> getSpecializationIds() { return specializationIds; }
    public void setSpecializationIds(List<Integer> specializationIds) { this.specializationIds = specializationIds; }
}
