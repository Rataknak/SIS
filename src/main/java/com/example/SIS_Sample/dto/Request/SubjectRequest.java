package com.example.SIS_Sample.dto.Request;

import jakarta.validation.constraints.NotNull;

public class SubjectRequest {
    @NotNull(message = "Subject is required")
    private String name;
    private String code;
    private String description;
    private String active;


    public @NotNull(message = "Subject is required") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Subject is required") String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
