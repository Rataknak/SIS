package com.example.SIS_Sample.controller;

import com.example.SIS_Sample.dto.Request.EnrollmentRequest;
import com.example.SIS_Sample.dto.Request.EnrollmentUpdateRequest;
import com.example.SIS_Sample.dto.Response.EnrollmentResponse;
import com.example.SIS_Sample.model.Enrollment;
import com.example.SIS_Sample.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<EnrollmentResponse> enroll(
            @Valid @RequestBody EnrollmentRequest request) {
        return ResponseEntity.ok(enrollmentService.enrollStudent(request));
    }

    @GetMapping("/active")
    public ResponseEntity<List<EnrollmentResponse>> getAllEnrollmentIsActive() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollmentIsActive());
    }

    @GetMapping("/pageable")
    public ResponseEntity<Map<String, Object>> getEnrollmentWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentWithPagination(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> getEnrollmentById(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentByStudent(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentByCourse(courseId));
    }

    @GetMapping("/totalStudents/{courseId}")
    public ResponseEntity<Long> getTotalStudentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getTotalStudentsByCourse(courseId));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<EnrollmentResponse>> batchEnrollStudent(@RequestParam Long studentId, @RequestParam String semester, @RequestParam String academicYear, @RequestBody List<Long> courseIds) {
        List<EnrollmentResponse> responses = enrollmentService.batchEnrollStudent(studentId, courseIds, semester, academicYear);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{enrollmentId}")
    public ResponseEntity<EnrollmentResponse> partialUpdate(@PathVariable Long enrollmentId, @Valid @RequestBody EnrollmentUpdateRequest request) {
        Enrollment updated = enrollmentService.partialUpdate(enrollmentId, request);
        return ResponseEntity.ok(EnrollmentResponse.fromEntity(updated));
    }
}
