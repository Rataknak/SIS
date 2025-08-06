package com.example.SIS_Sample.controller;

import com.example.SIS_Sample.dto.Request.CourseRequest;
import com.example.SIS_Sample.dto.Response.CourseResponse;
import com.example.SIS_Sample.dto.Response.TeacherResponse;
import com.example.SIS_Sample.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/create")
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest courseRequest) {
        CourseResponse courseResponse = courseService.createCourse(courseRequest);
        return new ResponseEntity<>(courseResponse, HttpStatus.CREATED);
    }

    @GetMapping("/allActive")
    public ResponseEntity<List<CourseResponse>> getAllActiveCourses() {
        List<CourseResponse> courseResponses = courseService.getAllActiveCourses();
        return new ResponseEntity<>(courseResponses, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable Long id, @RequestBody CourseRequest courseRequest) {
        CourseResponse courseResponse = courseService.updateCourse(id, courseRequest);
        return new ResponseEntity<>(courseResponse, HttpStatus.OK);
    }

    @PostMapping("/assignTeacher/{courseId}/{teacherId}")
    public ResponseEntity<String> assignTeacherToCourse(@PathVariable Long courseId, @PathVariable Long teacherId) {
        courseService.assignTeacherToCourse(courseId, teacherId);
        return ResponseEntity.ok("Teacher assigned to course successfully.");
    }

    @PostMapping("/{courseId}/assignSubjects")
    public ResponseEntity<String> assignSubjectsToCourse(@PathVariable Long courseId, @RequestBody List<Long> subjectIds) {
        courseService.assignSubjectsToCourse(courseId, subjectIds);
        return ResponseEntity.ok("Subjects assigned to course successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        CourseResponse courseResponse = courseService.getCourseById(id);
        return new ResponseEntity<>(courseResponse, HttpStatus.OK);
    }

    @GetMapping("/teachers/{courseId}")
    public ResponseEntity<List<TeacherResponse>> getTeachersByCourseId(@PathVariable Long courseId) {
        List<TeacherResponse> teacherResponses = courseService.getTeachersByCourseId(courseId);
        return new ResponseEntity<>(teacherResponses, HttpStatus.OK);
    }

    @GetMapping("/top10")
    public ResponseEntity<List<CourseResponse>> getTop10ActiveCourses() {
        List<CourseResponse> courseResponses = courseService.getTop10ActiveCourses();
        return new ResponseEntity<>(courseResponses, HttpStatus.OK);
    }

    @GetMapping("/totalActive")
    public ResponseEntity<Integer> getTotalActiveCourses() {
        int totalActiveCourses = courseService.getTotalActiveCourses();
        return new ResponseEntity<>(totalActiveCourses, HttpStatus.OK);
    }

    @DeleteMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateCourse(@PathVariable Long id) {
        courseService.deactivateCourse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
