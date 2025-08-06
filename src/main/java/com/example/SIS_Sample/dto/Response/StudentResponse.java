package com.example.SIS_Sample.dto.Response;

import java.time.LocalDate;

public class StudentResponse {

    private Long id;
    private String name;
    private String email;
    private String dob;
    private String gender;
    private String address;
    private String phone;
    private String profilePicture;
    private String guardian;

    public StudentResponse(Long id, String email, LocalDate dob, String name, String gender, String address, String phone, String guardian, String profilePicture) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.dob = (dob != null) ? dob.toString() : null;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.guardian = guardian;
        this.profilePicture = profilePicture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getGuardian() {
        return guardian;
    }
    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

}
