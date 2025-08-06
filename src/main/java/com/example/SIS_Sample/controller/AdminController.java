package com.example.SIS_Sample.controller;

import com.example.SIS_Sample.dto.Request.StudentRequest;
import com.example.SIS_Sample.dto.Request.TeacherRequest;
import com.example.SIS_Sample.dto.Response.StudentResponse;
import com.example.SIS_Sample.dto.Response.TeacherResponse;
import com.example.SIS_Sample.dto.Response.UserResponse;
import com.example.SIS_Sample.service.AdminService;
import com.example.SIS_Sample.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TeacherService teacherService;


    @PostMapping("/registerTeacher")
    public UserResponse registerTeacher(@RequestPart("teacherRequest") TeacherRequest teacherRequest, @RequestPart("file") MultipartFile file) {
        return teacherService.registerTeacher(teacherRequest, file);
    }

    @PutMapping("/updateTeacher/{teacherId}")
    public UserResponse updateTeacher(@RequestBody TeacherRequest teacherRequest, @PathVariable Long teacherId) {
        return teacherService.updateTeacher(teacherRequest, teacherId);
    }

    @GetMapping("/teachers/{id}")
    public TeacherResponse getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id);
    }

    @PutMapping("/updateTeacherProfileImage/{teacherId}")
    public UserResponse updateTeacherProfileImage(@PathVariable Long teacherId, @RequestParam("file") MultipartFile file) {
        return teacherService.updateTeacherProfileImages(teacherId, file);
    }

    @PutMapping("/deActivateTeacher/{id}")
    public ResponseEntity<Void> deactivateTeacher(@PathVariable Long id) {
        teacherService.deactivateTeacher(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teachers")
    public List<TeacherResponse> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/teachers/bySubject/{subjectId}")
    public List<TeacherResponse> getTeachersBySubject(@PathVariable Long subjectId) {
        return teacherService.getTeachersBySubject(subjectId);
    }

    @GetMapping("/teachers/ActiveTeacher")
    public ResponseEntity<Integer> getTotalActiveTeachers() {
        int totalActiveTeachers = teacherService.getTotalActiveTeachers();
        return ResponseEntity.ok(totalActiveTeachers);
    }




    @PostMapping(value = "/registerStudent")
    public UserResponse registerStudent(@RequestPart("studentRequest") StudentRequest studentRequest, @RequestPart("file") MultipartFile file) {
        return adminService.registerStudent(studentRequest, file);
    }

    @PutMapping("/updateStudent/{studentId}")
    public UserResponse updateStudent(@RequestBody StudentRequest studentRequest, @PathVariable Long studentId) {
        return adminService.updateStudent(studentRequest, studentId);
    }

    @PutMapping("/updateStudentProfileImage/{studentId}")
    public UserResponse updateStudentProfileImage(@PathVariable Long studentId, @RequestParam("file") MultipartFile file) {
        return adminService.updateStudentProfileImages(studentId, file);
    }

    @GetMapping("/students")
    public List<StudentResponse> getAllStudents() {
        return adminService.getAllStudents();
    }

    @GetMapping("/students/top20")
    public List<StudentResponse> getTop20Students() {
        return adminService.getTop20Students();
    }

    @GetMapping("/students/pagination")
    public List<StudentResponse> getStudentsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        return adminService.getStudentwithPagination(page, size);
    }

    @GetMapping("/students/{id}")
    public StudentResponse getStudentById(@PathVariable Long id) {
        return adminService.getStudentById(id);
    }

    @GetMapping("/students/search")
    public List<StudentResponse> searchStudents(@RequestParam String keyword) {
        return adminService.searchStudents(keyword);
    }

    @GetMapping("/students/findByGender")
    public List<StudentResponse> findByGender(@RequestParam String gender){
        return adminService.getStudentByGender(gender);
    }

    @GetMapping("/students/genderDistribution")
    public ResponseEntity<Map<String, Long>> getGenderDistribution() {
        Map<String, Long> genderDistribution = adminService.getGenderDistribution();
        return ResponseEntity.ok(genderDistribution);
    }

    @GetMapping("/students/registrationYear")
    public ResponseEntity<Map<Integer, Long>> getTotalStudentsByRegistrationYear() {
        Map<Integer, Long> totalStudentsByRegistrationYear = adminService.getTotalStudentsByRegistrationYear();
        return ResponseEntity.ok(totalStudentsByRegistrationYear);
    }

    @PutMapping("/activateStudent/{id}")
    public ResponseEntity<Void> activateStudent(@PathVariable Long id) {
        adminService.activateStudent(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deactivateStudent/{id}")
    public ResponseEntity<Void> deactivateStudent(@PathVariable Long id) {
        adminService.deactivateStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/students/deactivated")
    public List<StudentResponse> getAllDeactivatedStudents() {
        return adminService.getAllIDeActiveStudents();
    }

    @GetMapping("/students/activecount")
    public ResponseEntity<Integer> getTotalActiveStudents() {
        int totalActiveStudents = adminService.getTotalActiveStudents();
        return ResponseEntity.ok(totalActiveStudents);
    }

}
