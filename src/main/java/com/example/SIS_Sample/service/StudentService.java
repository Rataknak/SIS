package com.example.SIS_Sample.service;

import com.example.SIS_Sample.dto.JwtResponse;
import com.example.SIS_Sample.dto.Request.PasswordUpdateRequest;
import com.example.SIS_Sample.dto.Request.StudentRequest;
import com.example.SIS_Sample.dto.Request.UserRequest;
import com.example.SIS_Sample.dto.Response.StudentResponse;
import com.example.SIS_Sample.dto.Response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {

    Long getUserIdByEmail(String email);
    StudentResponse getStudentById(Long id);
    Long getUserIdByStudentId(Long studentId);

    void updateStudentPassword(Long StudentId, PasswordUpdateRequest passwordUpdateRequest);
}
