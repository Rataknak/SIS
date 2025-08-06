package com.example.SIS_Sample.dto.Request;

public record EnrollmentUpdateRequest(
        Long courseId,
        String semester,
        String academicYear,
        String status
) {


}
