package com.example.SIS_Sample.dto.Response;

import com.example.SIS_Sample.dto.EnrollmentStatus;
import com.example.SIS_Sample.model.Enrollment;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record EnrollmentResponse(
        Long enrollmentId,

        StudentInfo student,
        CourseInfo course,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate enrollmentDate,

        String semester,
        String academicYear,
        String status,

        String termDisplayName
) {

    public record StudentInfo(
            Long id,
            String name,
            String email
    ) {}

    public record CourseInfo(
            Long id,
            String name,
            String title
    ) {}

    // Static factory method
    public static EnrollmentResponse fromEntity(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                new StudentInfo(
                        enrollment.getStudent().getId(),
                        enrollment.getStudent().getName(),
                        enrollment.getStudent().getUser().getEmail()
                ),
                new CourseInfo(
                        enrollment.getCourse().getId(),
                        enrollment.getCourse().getName(),
                        enrollment.getCourse().getDescription()
                ),
                enrollment.getEnrollmentDate(),
                enrollment.getSemester(),
                enrollment.getAcademicYear(),
                enrollment.getStatus().name(),
                enrollment.getSemester() + " " + enrollment.getAcademicYear()
        );
    }
}