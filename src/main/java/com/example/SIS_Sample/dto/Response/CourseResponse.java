package com.example.SIS_Sample.dto.Response;

import java.util.List;

public class CourseResponse {

    private Long id;
    private String name;
    private String description;
    private List <Long> teacherIds;
    private String isActive;

    public CourseResponse() {
    }

    public CourseResponse(Long id, String name, String description,List<Long> teacherIds, String isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.teacherIds = teacherIds;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(List<Long> teacherIds) {
        this.teacherIds = teacherIds;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
