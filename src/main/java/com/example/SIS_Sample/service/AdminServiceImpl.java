package com.example.SIS_Sample.service;

import com.example.SIS_Sample.config.EmailAlreadyExistsException;
import com.example.SIS_Sample.config.UserNotFound;
import com.example.SIS_Sample.config.UsernameAlreadyExistsException;
import com.example.SIS_Sample.dto.Request.StudentRequest;
import com.example.SIS_Sample.dto.Request.TeacherRequest;
import com.example.SIS_Sample.dto.Request.UserRequest;
import com.example.SIS_Sample.dto.Response.StudentResponse;
import com.example.SIS_Sample.dto.Response.TeacherResponse;
import com.example.SIS_Sample.dto.Response.UserResponse;
import com.example.SIS_Sample.dto.Role;
import com.example.SIS_Sample.model.Student;
import com.example.SIS_Sample.model.Teacher;
import com.example.SIS_Sample.model.User;
import com.example.SIS_Sample.repository.StudentRepository;
import com.example.SIS_Sample.repository.TeacherRepository;
import com.example.SIS_Sample.repository.UserRepository;
import com.example.SIS_Sample.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public UserResponse registerStudent(StudentRequest studentRequest, MultipartFile file){
        if (userRepository.existsByUsername(studentRequest.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(studentRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        long studentCount = studentRepository.count();
        String nextUsername = "student" + String.format("%04d", studentCount + 1);

        User user = new User();
        user.setUsername(nextUsername);
        user.setEmail(studentRequest.getEmail());
        user.setRole(Role.STUDENT);

        String defaultPassword = "student123";
        user.setPassword(JwtUtil.hashPassword(defaultPassword));

        User savedUser = userRepository.save(user);

        Student student = new Student();
        student.setUser(savedUser);
        student.setName(studentRequest.getName());
        student.setGender(studentRequest.getGender());
        student.setDob(studentRequest.getDob());
        student.setAddress(studentRequest.getAddress());
        student.setPhone(studentRequest.getPhone());
        student.setGuardian(studentRequest.getGuardian());
        if (file != null && !file.isEmpty()) {
            try {
                student.setProfilePicture(cloudinaryService.uploadFile(file));
            } catch (IOException e) {
                e.printStackTrace();
                student.setProfilePicture("static/images/student.png");
            }
        } else {
            student.setProfilePicture("static/images/student.png");
        }
        studentRepository.save(student);

        return mapUserToUserResponse(savedUser);
    }

    @Override
    public UserResponse updateStudent(StudentRequest studentRequest, Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("Student not found"));

        if(studentRequest.getName() != null){
            student.setName(studentRequest.getName());
        }
        if(studentRequest.getEmail() != null){
            student.getUser().setEmail(studentRequest.getEmail());
        }
        if(studentRequest.getGender() != null){
            student.setGender(studentRequest.getGender());
        }
        if(studentRequest.getDob() != null){
            student.setDob(studentRequest.getDob());
        }
        if(studentRequest.getAddress() != null){
            student.setAddress(studentRequest.getAddress());
        }
        if(studentRequest.getPhone() != null){
            student.setPhone(studentRequest.getPhone());
        }
        if(studentRequest.getGuardian() != null){
            student.setGuardian(studentRequest.getGuardian());
        }
        studentRepository.save(student);
        return mapUserToUserResponse(student.getUser());
    }

    @Override
    public UserResponse updateStudentProfileImages(Long id, MultipartFile file) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("Student not found"));

        if (file != null && !file.isEmpty()) {
            try {
                student.setProfilePicture(cloudinaryService.uploadFile(file));
            } catch (IOException e) {
                e.printStackTrace();
                student.setProfilePicture("static/images/student.png");
            }
        } else {
            student.setProfilePicture("static/images/student.png");
        }

        studentRepository.save(student);
        return mapUserToUserResponse(student.getUser());
    }

    @Override
    public List<StudentResponse> getTop20Students() {
        List<Student> students = studentRepository.findTop10ByIsActiveTrue();
        return students.stream()
                .map(this::mapStudentToStudentResponse)
                .toList();
    }

    @Override
    public List<StudentResponse> getStudentwithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Student> studentPage = studentRepository.findActiveStudents(pageable);
        return studentPage.getContent().stream()
                .map(this::mapStudentToStudentResponse)
                .toList();
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        List<Student> students = studentRepository.findByIsActiveTrue();
        return students.stream()
                .map(this::mapStudentToStudentResponse)
                .toList();
    }

    @Override
    public List<StudentResponse> getAllIDeActiveStudents() {
        List<Student> students = studentRepository.findByIsActiveFalse();
        return students.stream()
                .map(this::mapStudentToStudentResponse)
                .toList();
    }

    @Override
    public List<StudentResponse> searchStudents(String keyword) {
        List<Student> students = studentRepository.searchActiveStudents(keyword);
        return students.stream()
                .map(this::mapStudentToStudentResponse)
                .toList();
    }

    @Override
    public List<StudentResponse> getStudentByGender(String gender) {
        List<Student> students = studentRepository.findByGenderAndIsActiveTrue(gender);
        return students.stream()
                .map(this::mapStudentToStudentResponse)
                .toList();
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("Student not found"));
        return mapStudentToStudentResponse(student);
    }

    @Override
    public Map<String, Long> getGenderDistribution(){
        Map<String, Long> genderDistribution = new HashMap<>();
        genderDistribution.put("male", (long) studentRepository.countByGenderAndIsActiveTrue("MALE"));
        genderDistribution.put("female",(long) studentRepository.countByGenderAndIsActiveTrue("FEMALE"));
        genderDistribution.put("other",(long) studentRepository.countByGenderAndIsActiveTrue("OTHER"));
        return genderDistribution;
    }

    @Override
    public Map<Integer, Long> getTotalStudentsByRegistrationYear(){
        Map<Integer, Long> totalStudentsByYear = new HashMap<>();
        List<Object[]> results = studentRepository.findTotalStudentsByRegistrationYear();
        for (Object[] result : results) {
            int year = (int) result[0];
            long count = (long) result[1];
            totalStudentsByYear.put(year, count);
        }
        return totalStudentsByYear;
    }

    @Override
    public void activateStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("Student not found"));
        student.setActive(true);
        studentRepository.save(student);
    }

    @Override
    public void deactivateStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("Student not found"));
        student.setActive(false);
        studentRepository.save(student);
    }

    @Override
    public int getTotalActiveStudents() {
        return studentRepository.countByIsActiveTrue();
    }

    private UserResponse mapUserToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    private StudentResponse mapStudentToStudentResponse(Student student) {
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
                teacher.getCourse() != null ? teacher.getCourse().getName() : null
        );
    }
}
