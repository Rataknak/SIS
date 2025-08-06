package com.example.SIS_Sample.dto.Response;

public class TeacherSubjectGradeResponse {
    private Long id;
    private String teacherName;
    private String subjectName;
    private String courseName;

    // Constructors, Getters, and Setters
    public TeacherSubjectGradeResponse() {
    }

    public TeacherSubjectGradeResponse(Long id, String teacherName, String subjectName, String courseName) {
        this.id = id;
        this.teacherName = teacherName;
        this.subjectName = subjectName;
        this.courseName = courseName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}