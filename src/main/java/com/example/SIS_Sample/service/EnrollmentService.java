package com.example.SIS_Sample.service;

import com.example.SIS_Sample.dto.Request.EnrollmentRequest;
import com.example.SIS_Sample.dto.Request.EnrollmentUpdateRequest;
import com.example.SIS_Sample.dto.Response.EnrollmentResponse;
import com.example.SIS_Sample.model.Enrollment;

import java.util.List;
import java.util.Map;

public interface EnrollmentService {
    EnrollmentResponse enrollStudent(EnrollmentRequest enrollmentRequest);
    EnrollmentResponse getEnrollmentById(Long enrollmentId);
    List<EnrollmentResponse> getAllEnrollmentIsActive();
    Map<String, Object> getEnrollmentWithPagination(int page, int size);
    List<EnrollmentResponse> getEnrollmentByStudent(Long studentId);
    List<EnrollmentResponse> getEnrollmentByCourse(Long courseId);
    //List<EnrollmentResponse> getCoursesByStudentName(String studentName);
    List<EnrollmentResponse> batchEnrollStudent(Long studentId, List<Long> courseIds, String semester, String academicYear);
    Enrollment partialUpdate(Long enrollmentId, EnrollmentUpdateRequest request);
    Long getTotalStudentsByCourse(Long courseId);
}
