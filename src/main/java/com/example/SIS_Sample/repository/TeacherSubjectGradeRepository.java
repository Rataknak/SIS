package com.example.SIS_Sample.repository;

import com.example.SIS_Sample.model.Course;
import com.example.SIS_Sample.model.Subject;
import com.example.SIS_Sample.model.Teacher;
import com.example.SIS_Sample.model.TeacherSubjectGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherSubjectGradeRepository extends JpaRepository<TeacherSubjectGrade, Long> {
    boolean existsByTeacherIdAndSubjectId(Long teacherId, Long subjectId);
    boolean existsBySubjectIdAndCourseId(Long subjectId, Long courseId);
    boolean existsByTeacherAndSubjectAndCourse(Teacher teacher, Subject subject, Course course);

    List<TeacherSubjectGrade> findByTeacherId(Long teacherId);


}
