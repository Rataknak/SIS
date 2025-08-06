package com.example.SIS_Sample.service;

import com.example.SIS_Sample.config.InvalidPassword;
import com.example.SIS_Sample.config.UserNotFound;
import com.example.SIS_Sample.dto.Request.PasswordUpdateRequest;
import com.example.SIS_Sample.dto.Request.StudentRequest;
import com.example.SIS_Sample.dto.Response.StudentResponse;
import com.example.SIS_Sample.dto.Response.UserResponse;
import com.example.SIS_Sample.model.Student;
import com.example.SIS_Sample.model.User;
import com.example.SIS_Sample.repository.StudentRepository;
import com.example.SIS_Sample.repository.UserRepository;
import com.example.SIS_Sample.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public Long getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFound("User not found"));
        return user.getId();
    }

    @Override
    public Long getUserIdByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFound("Student not found"));
        return student.getUser().getId();
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("Student not found"));
        return mapStudentToStudentResponse(student);
    }

   @Override
   public void updateStudentPassword(Long StudentId, PasswordUpdateRequest passwordUpdateRequest){
        Student student = studentRepository.findById(StudentId)
                .orElseThrow(() -> new UserNotFound("Student not found"));

        User user = student.getUser();
        if(!JwtUtil.verifyPassword(passwordUpdateRequest.getOldPassword(), user.getPassword())){
            throw new InvalidPassword("Invalid old password");
        }
        user.setPassword(JwtUtil.hashPassword(passwordUpdateRequest.getNewPassword()));
        userRepository.save(user);
   }


    private StudentResponse mapStudentToStudentResponse(Student student) {
        String dob = (student.getDob() != null) ? student.getDob().toString() : null;
        return new StudentResponse(
                student.getId(),
                student.getUser().getEmail(),
                student.getDob(),
                student.getName(),
                student.getGender(),
                student.getAddress(),
                student.getPhone(),
                student.getGuardian(),
                student.getProfilePicture()
        );
    }

    private UserResponse mapUserToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    private String handleFileUpload(MultipartFile file){
        try{
            if(file != null && !file.isEmpty()){
                return cloudinaryService.uploadFile(file);
            }else{
                return "http://res.cloudinary.com/diw5sne2n/image/upload/v1741925281/mcauv8vkxi4cc1bprniv.jpg";
            }
        }catch (IOException e){
            e.printStackTrace();
            return "http://res.cloudinary.com/diw5sne2n/image/upload/v1741925281/mcauv8vkxi4cc1bprniv.jpg";
        }
    }
}