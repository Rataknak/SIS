package com.example.SIS_Sample.service;

import com.example.SIS_Sample.dto.Request.AttendanceRequest;
import com.example.SIS_Sample.model.Attendance;
import com.example.SIS_Sample.model.Course;
import com.example.SIS_Sample.model.Student;
import com.example.SIS_Sample.model.Teacher;
import com.example.SIS_Sample.repository.AttendanceRepository;
import com.example.SIS_Sample.repository.CourseRepository;
import com.example.SIS_Sample.repository.StudentRepository;
import com.example.SIS_Sample.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public void takeAttendance(AttendanceRequest request, String teacherId){
        // Validate the teacher
        Teacher teacher = teacherRepository.findById(Long.parseLong(teacherId))
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // Validate the course
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Ensure the teacher is assigned to the course
        if (!course.getTeachers().contains(teacher)) {
            throw new RuntimeException("Teacher is not assigned to this course.");
        }

        // Process each student's attendance
        request.getStudents().forEach(studentAttendance -> {
            // Validate the student
            Student student = studentRepository.findById(studentAttendance.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student with ID " + studentAttendance.getStudentId() + " not found"));

            // Create and save attendance record
            Attendance attendance = new Attendance();
            attendance.setStudent(student);
            attendance.setCourse(course);
            attendance.setTeacher(teacher);
            attendance.setDate(LocalDate.parse(request.getDate())); // Parse date
            attendance.setStatus(studentAttendance.getStatus());
            attendanceRepository.save(attendance);
        });
        
    }

}
