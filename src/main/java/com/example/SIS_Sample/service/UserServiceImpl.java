package com.example.SIS_Sample.service;

import com.example.SIS_Sample.config.EmailAlreadyExistsException;
import com.example.SIS_Sample.config.UserNotFound;
import com.example.SIS_Sample.config.UsernameAlreadyExistsException;
import com.example.SIS_Sample.dto.JwtResponse;
import com.example.SIS_Sample.dto.Request.AdminRequest;
import com.example.SIS_Sample.dto.Request.UserRequest;
import com.example.SIS_Sample.dto.Response.AdminProfileResponse;
import com.example.SIS_Sample.dto.Response.AdminResponse;
import com.example.SIS_Sample.dto.Response.UserResponse;
import com.example.SIS_Sample.dto.Role;
import com.example.SIS_Sample.model.Admin;
import com.example.SIS_Sample.model.User;
import com.example.SIS_Sample.repository.AdminRepository;
import com.example.SIS_Sample.repository.UserRepository;
import com.example.SIS_Sample.utils.JwtUtil;
import org.hibernate.annotations.DialectOverride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public Long getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFound("User not found"));
        return user.getId();
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("User not found"));

        return new UserResponse(user);  // Pass the entire user object to the UserResponse constructor
    }

    @Override
    public Long getUserIdByAdminId(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
        return admin.getUser().getId();
    }

    @Override
    public AdminResponse getAdminProfileById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound("User not found"));

        Admin admin = adminRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));

        return mapAdminToAdminResponse(admin);
    }

    @Override
    public AdminProfileResponse getAdminProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFound("User not found"));

        Admin admin = adminRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));

        UserResponse userResponse = mapUserToUserResponse(user);
        AdminResponse adminResponse = mapAdminToAdminResponse(admin);

        return new AdminProfileResponse(userResponse, adminResponse);
    }

    @Override
    public JwtResponse login(UserRequest userRequest) {
        User user = userRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(() -> new EmailAlreadyExistsException("user not found with this email"));


        if(!JwtUtil.verifyPassword(userRequest.getPassword(), user.getPassword())){
            throw new UserNotFound("Invalid password");
        }

        String token = JwtUtil.generateToken(user.getEmail());
        return new JwtResponse(token);
    }


    @Override
    public UserResponse RegisterAdmin(AdminRequest adminRequest, MultipartFile file) {
        if (userRepository.existsByUsername(adminRequest.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(adminRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // Create and save the User entity
        User user = new User();
        user.setUsername(adminRequest.getUsername());
        user.setEmail(adminRequest.getEmail());
        user.setRole(Role.ADMIN);
        user.setPassword(JwtUtil.hashPassword(adminRequest.getPassword()));
        User savedUser = userRepository.save(user);

        Admin admin = new Admin();
        admin.setUser(savedUser);
        admin.setName(adminRequest.getName());
        admin.setDob(adminRequest.getDob());
        admin.setGender(adminRequest.getGender());
        admin.setAddress(adminRequest.getAddress());
        admin.setPhone(adminRequest.getPhone());

        if (file != null && !file.isEmpty()) {
            try {
                String uploadedImageUrl = cloudinaryService.uploadFile(file);
                admin.setProfilePicture(uploadedImageUrl);
            } catch (IOException e) {
                e.printStackTrace();
                admin.setProfilePicture("static/images/default-admin.png");
            }
        } else {
            admin.setProfilePicture("static/images/default-admin.png");
        }

        adminRepository.save(admin);

        // Return the UserResponse DTO
        return mapUserToUserResponse(savedUser);
    }

    private UserResponse mapUserToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    private AdminResponse mapAdminToAdminResponse(Admin admin) {
        String dob = (admin.getDob() != null) ? admin.getDob().toString() : null;
        return new AdminResponse(
                admin.getId(),
                admin.getName(),
                admin.getUser().getEmail(),
                dob,
                admin.getGender(),
                admin.getAddress(),
                admin.getPhone(),
                admin.getProfilePicture()
        );
    }
}
