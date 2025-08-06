package com.example.SIS_Sample.dto;

public enum EnrollmentStatus {
    ACTIVE("active"),
    INACTIVE("inactive"),
    COMPLETED("completed"),
    DROPPED("dropped");

    private final String status;

    EnrollmentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
