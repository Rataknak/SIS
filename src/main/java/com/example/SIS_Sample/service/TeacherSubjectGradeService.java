package com.example.SIS_Sample.service;

import com.example.SIS_Sample.dto.Response.TeacherSubjectGradeResponse;
import com.example.SIS_Sample.model.TeacherSubjectGrade;

import java.util.List;
import java.util.Map;

public interface TeacherSubjectGradeService {
    TeacherSubjectGradeResponse assignTeacherToUnassignedCourse(Long teacherId, Long subjectId, Long courseId);
    List<TeacherSubjectGrade> findByTeacherId(Long teacherId);
    List<Map<String, Object>> getTeacherCoursesWithTotalStudents(Long teacherId);
    int getTotalCoursesByTeacherId(Long teacherId);
    List<Map<String, Object>> getCoursesWithStudentCountByTeacherId(Long teacherId);
}
