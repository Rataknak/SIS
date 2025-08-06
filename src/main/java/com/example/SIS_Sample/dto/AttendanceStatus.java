package com.example.SIS_Sample.dto;

public enum AttendanceStatus {
    PRESENT("Present"),
    ABSENT("Absent");

    private final String status;

    AttendanceStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
