package com.example.SIS_Sample.service;

import com.example.SIS_Sample.dto.JwtResponse;
import com.example.SIS_Sample.dto.Request.AdminRequest;
import com.example.SIS_Sample.dto.Request.StudentRequest;
import com.example.SIS_Sample.dto.Request.TeacherRequest;
import com.example.SIS_Sample.dto.Request.UserRequest;
import com.example.SIS_Sample.dto.Response.AdminProfileResponse;
import com.example.SIS_Sample.dto.Response.AdminResponse;
import com.example.SIS_Sample.dto.Response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    Long getUserIdByEmail(String email);
    Long getUserIdByAdminId(Long adminId);

    UserResponse RegisterAdmin(AdminRequest adminRequest, MultipartFile file);
    UserResponse getUserById(Long id);

    AdminResponse getAdminProfileById(Long userId);
    AdminProfileResponse getAdminProfileByEmail(String email);

    JwtResponse login(UserRequest userRequest);
}
