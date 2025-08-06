package com.example.SIS_Sample.service;

import com.example.SIS_Sample.config.EmailAlreadyExistsException;
import com.example.SIS_Sample.config.UserNotFound;
import com.example.SIS_Sample.config.UsernameAlreadyExistsException;
import com.example.SIS_Sample.dto.JwtResponse;
import com.example.SIS_Sample.dto.Request.StudentRequest;
import com.example.SIS_Sample.dto.Request.TeacherRequest;
import com.example.SIS_Sample.dto.Request.UserRequest;
import com.example.SIS_Sample.dto.Response.*;
import com.example.SIS_Sample.dto.Role;
import com.example.SIS_Sample.model.*;
import com.example.SIS_Sample.repository.StudentRepository;
import com.example.SIS_Sample.repository.SubjectRepository;
import com.example.SIS_Sample.repository.TeacherRepository;
import com.example.SIS_Sample.repository.UserRepository;
import com.example.SIS_Sample.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    //for teacher
    @Override
    public TeacherProfileResponse getTeacherProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFound("User not found with email: " + email));
        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found for user with email: " + email));

        UserResponse userResponse = mapUserToUserResponse(user);
        TeacherResponse teacherResponse = mapTeacherToTeacherResponse(teacher);
        return new TeacherProfileResponse(userResponse, teacherResponse);
    }

    //for admin
    @Override
    public TeacherResponse getTeacherById(Long id){
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        return mapTeacherToTeacherResponse(teacher);
    }

    @Override
    public UserResponse registerTeacher(TeacherRequest teacherRequest, MultipartFile file) {
        if (userRepository.existsByUsername(teacherRequest.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(teacherRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        long teacherCount = teacherRepository.count();
        String nextUsername = "teacher" + String.format("%04d", teacherCount + 1);

        // Create and save the User entity
        User user = new User();
        user.setUsername(nextUsername);
        user.setEmail(teacherRequest.getEmail());
        user.setRole(Role.TEACHER);

        String defaultPassword = "teacher123";
        user.setPassword(JwtUtil.hashPassword(defaultPassword));

        User savedUser = userRepository.save(user);

        // Create and save the Teacher entity
        Teacher teacher = new Teacher();
        teacher.setUser(savedUser);
        teacher.setName(teacherRequest.getName());
        teacher.setGender(teacherRequest.getGender());
        teacher.setDob(teacherRequest.getDob());
        teacher.setAddress(teacherRequest.getAddress());
        teacher.setPhone(teacherRequest.getPhone());

        // Handle profile image upload
        if (file != null && !file.isEmpty()) {
            try {
                teacher.setProfilePicture(cloudinaryService.uploadFile(file));
            } catch (IOException e) {
                e.printStackTrace();
                teacher.setProfilePicture("static/images/teacher.png");
            }
        } else {
            teacher.setProfilePicture("static/images/teacher.png");
        }

        teacherRepository.save(teacher);

        // Return the UserResponse DTO
        return mapUserToUserResponse(savedUser);
    }

    @Override
    public UserResponse updateTeacher(TeacherRequest teacherRequest, Long id){
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));

        if(teacherRequest.getName() != null){
            teacher.setName(teacherRequest.getName());
        }
        if(teacherRequest.getEmail() != null){
            teacher.getUser().setEmail(teacherRequest.getEmail());
        }
        if(teacherRequest.getGender() != null){
            teacher.setGender(teacherRequest.getGender());
        }
        if(teacherRequest.getDob() != null){
            teacher.setDob(teacherRequest.getDob());
        }
        if(teacherRequest.getAddress() != null){
            teacher.setAddress(teacherRequest.getAddress());
        }
        if(teacherRequest.getPhone() != null){
            teacher.setPhone(teacherRequest.getPhone());
        }
        teacherRepository.save(teacher);

        return mapUserToUserResponse(teacher.getUser());
    }

    @Override
    public UserResponse updateTeacherProfileImages(Long id, MultipartFile file) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));

        if (file != null && !file.isEmpty()) {
            try {
                teacher.setProfilePicture(cloudinaryService.uploadFile(file));
            } catch (IOException e) {
                e.printStackTrace();
                teacher.setProfilePicture("static/images/teacher.png");
            }
        } else {
            teacher.setProfilePicture("static/images/teacher.png");
        }

        teacherRepository.save(teacher);
        return mapUserToUserResponse(teacher.getUser());
    }

    @Override
    public List<TeacherResponse> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findActiveTeachers();
        return teachers.stream()
                .map(this::mapTeacherToTeacherResponse)
                .toList();
    }
    @Override
    public List<TeacherResponse> getTeachersBySubject(Long subjectId){
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));
        return subject.getTeachers().stream()
                .filter(teacher -> teacher.getUser().isActive())
                .map(this::mapTeacherToTeacherResponse)
                .toList();
    }

    @Override
    public void deactivateTeacher(Long id){
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        User user = teacher.getUser();
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public int getTotalActiveTeachers() {
        return teacherRepository.countActiveTeachers();
    }

    private UserResponse mapUserToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    private TeacherResponse mapTeacherToTeacherResponse(Teacher teacher) {
        return new TeacherResponse(
                teacher.getId(),
                teacher.getUser().getEmail(),
                teacher.getDob(),
                teacher.getName(),
                teacher.getGender(),
                teacher.getAddress(),
                teacher.getPhone(),
                teacher.getProfilePicture(),
                teacher.getSubject() != null ? teacher.getSubject().getName() : null
        );
    }
}
