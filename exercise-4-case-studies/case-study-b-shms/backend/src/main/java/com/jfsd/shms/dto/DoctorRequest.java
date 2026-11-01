package com.jfsd.shms.dto;

import jakarta.validation.constraints.NotBlank;

public record DoctorRequest(
        @NotBlank(message = "Name is required") String name,
        String specialization,
        String contact) {
}
