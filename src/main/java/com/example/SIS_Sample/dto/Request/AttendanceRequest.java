package com.example.SIS_Sample.dto.Request;

import com.example.SIS_Sample.dto.AttendanceStatus;

import java.util.List;

public class AttendanceRequest {

    private Long studentId;
    private Long courseId;
    private String date;
    private boolean present;
    private List<StudentAttendance> students;

    public static class StudentAttendance {
        private Long studentId;
        private AttendanceStatus status;

        public Long getStudentId() {
            return studentId;
        }

        public void setStudentId(Long studentId) {
            this.studentId = studentId;
        }

        public AttendanceStatus getStatus() {
            return status;
        }

        public void setStatus(AttendanceStatus status) {
            this.status = status;
        }
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public List<StudentAttendance> getStudents() {
        return students;
    }

    public void setStudents(List<StudentAttendance> students) {
        this.students = students;
    }
}
