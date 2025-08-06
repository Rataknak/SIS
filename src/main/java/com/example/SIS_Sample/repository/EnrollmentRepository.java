package com.example.SIS_Sample.repository;

import com.example.SIS_Sample.dto.EnrollmentStatus;
import com.example.SIS_Sample.model.Course;
import com.example.SIS_Sample.model.Enrollment;
import com.example.SIS_Sample.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentAndCourseAndAcademicYear(Student student, Course course, String academicYear);
    long countByStudentAndAcademicYear(Student student, String academicYear);

    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId")
    List<Enrollment> findByStudentId(Long studentId);

    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId")
    List<Enrollment> findByCourseId(Long courseId);

    int countByStudentAndStatus(Student student, EnrollmentStatus status);
    boolean existsByStudentAndCourse(Student student, Course course);
    List<Enrollment> findByStatus(EnrollmentStatus status);
    List<Enrollment> findByStudentNameContainingIgnoreCase(String studentName);
    @Query("SELECT e FROM Enrollment e WHERE e.status = :status ORDER BY e.enrollmentDate DESC ")
    Page<Enrollment> findAllEnrollments(@Param("status") EnrollmentStatus status, Pageable pageable);
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.status = :status")
    Long countStudentsByCourse(@Param("courseId") Long courseId, @Param("status") EnrollmentStatus status);
}
