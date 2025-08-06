package com.example.SIS_Sample.service;

import com.example.SIS_Sample.dto.JwtResponse;
import com.example.SIS_Sample.dto.Request.StudentRequest;
import com.example.SIS_Sample.dto.Request.TeacherRequest;
import com.example.SIS_Sample.dto.Request.UserRequest;
import com.example.SIS_Sample.dto.Response.*;
import com.example.SIS_Sample.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherService {

    //for teacher
    TeacherProfileResponse getTeacherProfileByEmail(String email);

    //for admin
    UserResponse registerTeacher(TeacherRequest teacherRequest, MultipartFile file);
    UserResponse updateTeacher(TeacherRequest teacherRequest, Long id);
    UserResponse updateTeacherProfileImages(Long id, MultipartFile file);
    List<TeacherResponse> getAllTeachers();
    List<TeacherResponse> getTeachersBySubject(Long subjectId);
    TeacherResponse getTeacherById(Long id);
    void deactivateTeacher(Long id);
    int getTotalActiveTeachers();
}
