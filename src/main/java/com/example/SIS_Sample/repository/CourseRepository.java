package com.example.SIS_Sample.repository;

import com.example.SIS_Sample.model.Course;
import com.example.SIS_Sample.model.Student;
import com.example.SIS_Sample.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByName(String name);
    Optional<Course> findByIdAndIsActive(Long id, boolean isActive);
    List<Course> findByTeacherId(Long teacherId);
    List<Course> findByIsActive(boolean isActive);
    int countByIsActive(boolean isActive);
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Course c JOIN c.teachers t WHERE t = :teacher")
    boolean existsByTeacher(@Param("teacher") Teacher teacher);
    @Query("SELECT c FROM Course c WHERE c.isActive = true ORDER BY c.id DESC")
    List<Course> findTop10ByIsActiveTrue();

}
