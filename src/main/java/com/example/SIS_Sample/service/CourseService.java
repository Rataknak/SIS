package com.example.SIS_Sample.service;

import com.example.SIS_Sample.dto.Request.CourseRequest;
import com.example.SIS_Sample.dto.Response.CourseResponse;
import com.example.SIS_Sample.dto.Response.TeacherResponse;
import com.example.SIS_Sample.model.Course;

import java.util.List;

public interface CourseService {

    CourseResponse createCourse(CourseRequest courseRequest);
    void assignTeacherToCourse(Long courseId, Long teacherId);
    CourseResponse updateCourse(Long id, CourseRequest courseRequest);
    CourseResponse getCourseById(Long id);
    List<CourseResponse> getTop10ActiveCourses();
    List<CourseResponse> getAllActiveCourses();
    List<TeacherResponse> getTeachersByCourseId(Long courseId);
    void deactivateCourse(Long id);
    int getTotalActiveCourses();

    void assignSubjectsToCourse(Long courseId, List<Long> subjectIds);
}
