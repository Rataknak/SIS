package com.example.SIS_Sample.service;

import com.example.SIS_Sample.dto.Request.AttendanceRequest;

public interface AttendanceService {

    void takeAttendance(AttendanceRequest request, String teacherId);
}
