package com.example.SIS_Sample.dto.Response;

public class AdminProfileResponse {
    private UserResponse userInfo;
    private AdminResponse adminInfo;

    public AdminProfileResponse(UserResponse userInfo, AdminResponse adminInfo) {
        this.userInfo = userInfo;
        this.adminInfo = adminInfo;
    }

    public UserResponse getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserResponse userInfo) {
        this.userInfo = userInfo;
    }

    public AdminResponse getAdminInfo() {
        return adminInfo;
    }

    public void setAdminInfo(AdminResponse adminInfo) {
        this.adminInfo = adminInfo;
    }
}