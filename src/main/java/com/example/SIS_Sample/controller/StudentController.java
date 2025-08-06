package com.example.SIS_Sample.controller;

import com.example.SIS_Sample.dto.Request.PasswordUpdateRequest;
import com.example.SIS_Sample.dto.Request.StudentRequest;
import com.example.SIS_Sample.dto.Response.StudentResponse;
import com.example.SIS_Sample.dto.Response.UserResponse;
import com.example.SIS_Sample.service.StudentService;
import com.example.SIS_Sample.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/student")
 // Assuming you have a custom annotation @PreAuthorize for role checking
@CrossOrigin(origins = "*", maxAge = 3600)  // Assuming you have CORS configuration in your application.properties file
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id, HttpServletRequest request) {
        Long userIdFromToken = extractUserIdFromToken(request);
        Long studentUserId = studentService.getUserIdByStudentId(id);
        if (!userIdFromToken.equals(studentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view this student");
        }

        StudentResponse studentResponse = studentService.getStudentById(id);
        return ResponseEntity.ok(studentResponse);
    }

    @PutMapping("/update/{studentId}")
    public ResponseEntity<String> updateStudent(@PathVariable Long studentId, @RequestBody PasswordUpdateRequest passwordUpdateRequest, HttpServletRequest request) {
        Long userIdFromToken = extractUserIdFromToken(request);
        Long studentUserId = studentService.getUserIdByStudentId(studentId);
        if (!userIdFromToken.equals(studentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this student");
        }

        studentService.updateStudentPassword(studentId, passwordUpdateRequest);
        return ResponseEntity.ok("Password has been successfully updated");
    }


    private Long extractUserIdFromToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or missing token");
        }
        String token = authorizationHeader.substring(7);
        if (!JwtUtil.isTokenValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
        String email = JwtUtil.getUsernameFromToken(token);
        return studentService.getUserIdByEmail(email);
    }
}
