package com.example.SIS_Sample.repository;

import com.example.SIS_Sample.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Custom query methods can be defined here if needed
    // For example, to find attendance by studentId and courseId
    // List<Attendance> findByStudentIdAndCourseId(Long studentId, Long courseId);

    // Additional methods can be added as per requirements
}
