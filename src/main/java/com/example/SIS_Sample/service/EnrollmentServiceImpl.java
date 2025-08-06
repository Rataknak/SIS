package com.example.SIS_Sample.service;

import com.example.SIS_Sample.Constants.EnrollmentLimits;
import com.example.SIS_Sample.dto.EnrollmentStatus;
import com.example.SIS_Sample.dto.Request.EnrollmentRequest;
import com.example.SIS_Sample.dto.Request.EnrollmentUpdateRequest;
import com.example.SIS_Sample.dto.Response.EnrollmentResponse;
import com.example.SIS_Sample.model.Course;
import com.example.SIS_Sample.model.Enrollment;
import com.example.SIS_Sample.model.Student;
import com.example.SIS_Sample.repository.CourseRepository;
import com.example.SIS_Sample.repository.EnrollmentRepository;
import com.example.SIS_Sample.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Override
    public EnrollmentResponse enrollStudent(EnrollmentRequest enrollmentRequest) {
        Student student = studentRepository.findById(enrollmentRequest.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findByIdAndIsActive(enrollmentRequest.getCourseId(), true)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if(enrollmentRepository.existsByStudentAndCourseAndAcademicYear(student, course, enrollmentRequest.getAcademicYear())) {
            throw new RuntimeException("Student is already enrolled in this course for the specified academic year");
        }

        long enrollmentCount = enrollmentRepository.countByStudentAndAcademicYear(student, enrollmentRequest.getAcademicYear());
        if (enrollmentCount >= 8) {
            throw new RuntimeException("Student cannot enroll in more than 8 courses in the same academic year");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setSemester(enrollmentRequest.getSemester());
        enrollment.setAcademicYear(enrollmentRequest.getAcademicYear());
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return EnrollmentResponse.fromEntity(savedEnrollment);
    }

    @Override
    public List<EnrollmentResponse> getAllEnrollmentIsActive() {
        List<Enrollment> enrollments = enrollmentRepository.findByStatus(EnrollmentStatus.ACTIVE);
        return enrollments.stream()
                .map(EnrollmentResponse::fromEntity)
                .toList();
    }

    @Override
    public Map<String, Object> getEnrollmentWithPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page <Enrollment> enrollmentPage = enrollmentRepository.findAllEnrollments(EnrollmentStatus.ACTIVE, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("enrollments", enrollmentPage.getContent()
                .stream()
                .map(EnrollmentResponse::fromEntity)
                .toList());
        response.put("currentPage", enrollmentPage.getNumber() + 1);
        response.put("totalItems", enrollmentPage.getTotalElements());
        response.put("totalPages", enrollmentPage.getTotalPages());
        response.put("hasNext", enrollmentPage.hasNext());
        response.put("hasPrevious", enrollmentPage.hasPrevious());
        return response;
    }

    @Override
    public EnrollmentResponse getEnrollmentById(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        return EnrollmentResponse.fromEntity(enrollment);
    }

    @Override
    public List<EnrollmentResponse>getEnrollmentByStudent(Long studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollments.stream()
                .map(EnrollmentResponse::fromEntity)
                .toList();
    }

    @Override
    public List<EnrollmentResponse>getEnrollmentByCourse(Long courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        return enrollments.stream()
                .map(EnrollmentResponse::fromEntity)
                .toList();
    }

    @Override
    public Long getTotalStudentsByCourse(Long courseId){
        return enrollmentRepository.countStudentsByCourse(courseId,EnrollmentStatus.ACTIVE);
    }

    @Override
    public  List<EnrollmentResponse> batchEnrollStudent(Long studentId, List<Long> courseIds, String semester, String academicYear) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        int currentEnrollments = enrollmentRepository.countByStudentAndStatus(student, EnrollmentStatus.ACTIVE);
        if(currentEnrollments + courseIds.size() > EnrollmentLimits.MAX_COURSES_PER_STUDENT){
            throw new RuntimeException("Exceeds maximum course limit for student");
        }

        List<EnrollmentResponse> responses = new ArrayList<>();
        Set<Long> duplicateCourseIds = new HashSet<>();

        for (Long courseId : courseIds) {
            try {
                Course course = courseRepository.findById(courseId)
                        .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

                if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
                    duplicateCourseIds.add(courseId);
                    continue;
                }

                Enrollment enrollment = new Enrollment();
                enrollment.setStudent(student);
                enrollment.setCourse(course);
                enrollment.setSemester(semester);
                enrollment.setAcademicYear(academicYear);
                enrollment.setStatus(EnrollmentStatus.ACTIVE);
                enrollment.setEnrollmentDate(LocalDate.now());

                Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
                responses.add(EnrollmentResponse.fromEntity(savedEnrollment));

            } catch (Exception e) {
                throw new RuntimeException("Failed to process course " + courseId + ": " + e.getMessage());
            }
        }

        return responses;
    }

    @Override
    public Enrollment partialUpdate(Long enrollmentId, EnrollmentUpdateRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (request.status() != null) {
            EnrollmentStatus newStatus = EnrollmentStatus.valueOf(request.status());
            validateStatusTransition(enrollment.getStatus(), newStatus);
            enrollment.setStatus(newStatus);
        }

        if (request.courseId() != null) {
            Course newCourse = courseRepository.findById(request.courseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            enrollment.setCourse(newCourse);
        }

        if (request.semester() != null) {
            enrollment.setSemester(request.semester());
        }

        if (request.academicYear() != null) {
            enrollment.setAcademicYear(request.academicYear());
        }

        return enrollmentRepository.save(enrollment);
    }

    private void validateStatusTransition(EnrollmentStatus current, EnrollmentStatus newStatus) {
        if (current == EnrollmentStatus.COMPLETED) {
            throw new RuntimeException("Completed enrollments cannot be modified");
        }
    }
}
