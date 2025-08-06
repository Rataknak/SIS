package com.example.SIS_Sample.controller.Teacher;

import com.example.SIS_Sample.dto.Response.*;
import com.example.SIS_Sample.model.Course;
import com.example.SIS_Sample.model.Teacher;
import com.example.SIS_Sample.service.CourseService;
import com.example.SIS_Sample.service.TeacherService;
import com.example.SIS_Sample.service.UserService;
import com.example.SIS_Sample.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/system/teacher")
public class TeacherControllerSystem {
    @Autowired
    private UserService userService;

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private CourseService courseService;

    @GetMapping("/profile")
    public ResponseEntity<TeacherProfileResponse> getTeacherProfile(HttpServletRequest request) {
        try {
            String email = extractEmailFromToken(request);
            TeacherProfileResponse profile = teacherService.getTeacherProfileByEmail(email);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to get profile", e);
        }
    }

//    @GetMapping("/{teacherId}/courses")
//    public ResponseEntity<List<TeacherSubjectGradeResponse>> getTeacherCourses(@PathVariable Long teacherId) {
//        try {
//            List<Course> courses = courseService.getCoursesByTeacherId(teacherId);
//            List<TeacherSubjectGradeResponse> responses = courses.stream()
//                    .map(course -> new TeacherSubjectGradeResponse(
//                            course.getId(),
//                            course.getTeachers().isEmpty() ? null : course.getTeachers().get(0).getName(),
//                            course.getSubjects().isEmpty() ? null : course.getSubjects().get(0).getName(),
//                            course.getName()
//                    ))
//                    .collect(Collectors.toList());
//            return ResponseEntity.ok(responses);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get teacher courses", e);
//        }
//    }

    private String extractEmailFromToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or missing token");
        }
        String token = authorizationHeader.substring(7);
        if (!JwtUtil.isTokenValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
        return JwtUtil.getUsernameFromToken(token);
    }

    private Long extractUserIdFromTokenAsLong(HttpServletRequest request) {
        String email = extractEmailFromToken(request);
        return userService.getUserIdByEmail(email);
    }
}
