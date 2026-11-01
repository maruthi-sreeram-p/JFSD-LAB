package com.jfsd.ocms.dto;

import com.jfsd.ocms.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Email is required") @Email(message = "Email is not valid") String email,
        @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must have at least 6 characters") String password,
        String contact,
        @NotNull(message = "Choose student or instructor") Role role) {
}
