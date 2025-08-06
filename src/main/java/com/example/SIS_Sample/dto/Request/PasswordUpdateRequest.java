package com.example.SIS_Sample.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordUpdateRequest {
    @NotBlank(message = "Old password cannot be empty")
    private String oldPassword;

    @NotBlank(message = "New password cannot be empty")
    @Size(min = 6, message = "New password must be at least 6 characters long")
    private String newPassword;

    public @NotBlank(message = "Old password cannot be empty") String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(@NotBlank(message = "Old password cannot be empty") String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public @NotBlank(message = "New password cannot be empty") @Size(min = 6, message = "New password must be at least 6 characters long") String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank(message = "New password cannot be empty") @Size(min = 6, message = "New password must be at least 6 characters long") String newPassword) {
        this.newPassword = newPassword;
    }
}
