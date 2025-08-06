package com.example.SIS_Sample.dto.Request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class EnrollmentRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    private String semester;

    @Pattern(regexp = "^\\d{4}-\\d{4}$") String academicYear;

    public @NotNull(message = "Student ID is required") Long getStudentId() {
        return studentId;
    }

    public void setStudentId(@NotNull(message = "Student ID is required") Long studentId) {
        this.studentId = studentId;
    }

    public @NotNull(message = "Course ID is required") Long getCourseId() {
        return courseId;
    }

    public void setCourseId(@NotNull(message = "Course ID is required") Long courseId) {
        this.courseId = courseId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public @Pattern(regexp = "^\\d{4}-\\d{4}$") String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(@Pattern(regexp = "^\\d{4}-\\d{4}$") String academicYear) {
        this.academicYear = academicYear;
    }
}
