package com.example.SIS_Sample.dto.Response;

import com.example.SIS_Sample.model.Teacher;

public class TeacherProfileResponse {
    private UserResponse userInfo;
    private TeacherResponse teacherInfo;

    public TeacherProfileResponse(UserResponse userInfo, TeacherResponse teacherInfo) {
        this.userInfo = userInfo;
        this.teacherInfo = teacherInfo;
    }

    public UserResponse getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserResponse userInfo) {
        this.userInfo = userInfo;
    }

    public TeacherResponse getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(TeacherResponse teacherInfo) {
        this.teacherInfo = teacherInfo;
    }
}