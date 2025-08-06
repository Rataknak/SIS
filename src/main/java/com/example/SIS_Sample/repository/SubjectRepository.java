package com.example.SIS_Sample.repository;

import com.example.SIS_Sample.model.Course;
import com.example.SIS_Sample.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query("SELECT s FROM Subject s JOIN s.course c WHERE c.id = :courseId")
    List<Subject> findSubjectsByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT s FROM Subject s  ORDER BY s.id DESC")
    List<Subject> findTop10Subject();

    @Query("SELECT s FROM Subject s WHERE :course NOT MEMBER OF s.course")
    List<Subject> findSubjectsNotAssignedToCourse(@Param("course") Course course);
}

