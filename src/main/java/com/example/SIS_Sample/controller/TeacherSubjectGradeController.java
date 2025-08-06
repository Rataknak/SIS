package com.example.SIS_Sample.controller;

import com.example.SIS_Sample.dto.Response.TeacherSubjectGradeResponse;
import com.example.SIS_Sample.model.TeacherSubjectGrade;
import com.example.SIS_Sample.service.EnrollmentService;
import com.example.SIS_Sample.service.TeacherSubjectGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teacher-subject-grade")
public class TeacherSubjectGradeController {

    @Autowired
    private TeacherSubjectGradeService teacherSubjectGradeService;

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/assign")
    public TeacherSubjectGradeResponse assignTeacherToUnassignedCourse(
            @RequestParam Long teacherId,
            @RequestParam Long subjectId,
            @RequestParam Long courseId) {
        return teacherSubjectGradeService.assignTeacherToUnassignedCourse(teacherId, subjectId, courseId);
    }

    @GetMapping("/{teacherId}/courses")
    public ResponseEntity<List<Map<String, Object>>> getTeacherCourses(@PathVariable Long teacherId) {
        try {
            List<Map<String, Object>> responses = teacherSubjectGradeService.getTeacherCoursesWithTotalStudents(teacherId);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get teacher courses", e);
        }
    }

    @GetMapping("/{teacherId}/totalCourses")
    public ResponseEntity<Integer> getTotalCoursesByTeacherId(@PathVariable Long teacherId) {
        try {
            int totalCourses = teacherSubjectGradeService.getTotalCoursesByTeacherId(teacherId);
            return ResponseEntity.ok(totalCourses);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get total courses by teacher ID", e);
        }
    }

    @GetMapping("/{teacherId}/subjects")
    public ResponseEntity<List<TeacherSubjectGradeResponse>> getSubjectsByTeacherId(@PathVariable Long teacherId) {
        try {
            List<TeacherSubjectGrade> teacherSubjects = teacherSubjectGradeService.findByTeacherId(teacherId);
            List<TeacherSubjectGradeResponse> responses = teacherSubjects.stream()
                    .map(tsg -> new TeacherSubjectGradeResponse(
                            tsg.getId(),
                            tsg.getTeacher().getName(),
                            tsg.getSubject().getName(),
                            tsg.getCourse().getName()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get subjects by teacher ID", e);
        }
    }

    @GetMapping("/{teacherId}/coursesWithStudentCount")
    public ResponseEntity<List<Map<String, Object>>> getCoursesWithStudentCountByTeacherId(@PathVariable Long teacherId) {
        try {
            List<Map<String, Object>> responses = teacherSubjectGradeService.getCoursesWithStudentCountByTeacherId(teacherId);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get courses with student count by teacher ID", e);
        }
    }
}

