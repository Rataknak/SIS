package com.example.SIS_Sample.service;

import com.example.SIS_Sample.dto.JwtResponse;
import com.example.SIS_Sample.dto.Request.StudentRequest;
import com.example.SIS_Sample.dto.Response.StudentResponse;
import com.example.SIS_Sample.dto.Response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AdminService {

    UserResponse registerStudent(StudentRequest userRequest, MultipartFile file);
    UserResponse updateStudent(StudentRequest userRequest, Long id);
    UserResponse updateStudentProfileImages(Long id, MultipartFile file);
    List<StudentResponse> getTop20Students();
    List<StudentResponse> getStudentwithPagination(int page, int size);
    List<StudentResponse> getAllStudents();
    List<StudentResponse> getAllIDeActiveStudents();
    List<StudentResponse> searchStudents(String keyword);
    List<StudentResponse> getStudentByGender(String gender);
    StudentResponse getStudentById(Long id);
    void activateStudent(Long id);
    void deactivateStudent(Long id);
    int getTotalActiveStudents();
    Map<String, Long> getGenderDistribution();
    Map<Integer, Long> getTotalStudentsByRegistrationYear();


}
