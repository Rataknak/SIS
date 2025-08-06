package com.example.SIS_Sample.controller;

import com.example.SIS_Sample.dto.JwtResponse;
import com.example.SIS_Sample.dto.Request.AdminRequest;
import com.example.SIS_Sample.dto.Request.UserRequest;
import com.example.SIS_Sample.dto.Response.AdminProfileResponse;
import com.example.SIS_Sample.dto.Response.AdminResponse;
import com.example.SIS_Sample.dto.Response.UserResponse;
import com.example.SIS_Sample.service.UserService;
import com.example.SIS_Sample.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

//@CrossOrigin(origins = "http://localhost:63343")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public JwtResponse login(@RequestBody UserRequest userRequest){
        return userService.login(userRequest);
    }

    @PostMapping("/register")
    public UserResponse registerAdmin(@RequestPart("adminRequest") AdminRequest adminRequest,  @RequestPart("file") MultipartFile file) {
        return userService.RegisterAdmin(adminRequest, file);
    }
    @GetMapping("/profile")
    public ResponseEntity<AdminProfileResponse> getAdminProfile(HttpServletRequest request) {
        try {
            String email = extractEmailFromToken(request);
            AdminProfileResponse profile = userService.getAdminProfileByEmail(email);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            logger.error("Failed to get profile", e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to get profile", e);
        }
    }

    private String extractEmailFromToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or missing token");
        }
        String token = authorizationHeader.substring(7);
        if (!JwtUtil.isTokenValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
        return JwtUtil.getUsernameFromToken(token);
    }

    private Long extractUserIdFromTokenAsLong(HttpServletRequest request) {
        String email = extractEmailFromToken(request);
        return userService.getUserIdByEmail(email);
    }

}
