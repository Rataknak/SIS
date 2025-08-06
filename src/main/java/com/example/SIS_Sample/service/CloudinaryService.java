package com.example.SIS_Sample.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {
        try {
            // Corrected parameters
            Map<String, Object> params = ObjectUtils.asMap(
                    "resource_type", "image", // Changed from "static/images" to "image"
                    "access_mode", "authenticated"
            );

            // Upload file and log the response
            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), params);
            System.out.println("Cloudinary Response: " + result);

            // Validate response
            if (!result.containsKey("public_id")) {
                throw new RuntimeException("Invalid response from Cloudinary: Missing 'public_id'");
            }

            String publicId = (String) result.get("public_id");
            return generateSignedUrl(publicId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
    }

    private String generateSignedUrl(String publicId) {
        return cloudinary.url()
                .publicId(publicId)
                .secure(true)
                .signed(true)
                .resourceType("image") // Changed from "static/images" to "image"
                .format("jpg")
                .generate();
    }
}
