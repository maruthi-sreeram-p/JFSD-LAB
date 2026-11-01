package com.jfsd.shms.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PatientRequest(
        @NotBlank(message = "Name is required") String name,
        @NotNull(message = "Age is required") @Min(value = 0, message = "Age cannot be negative") @Max(value = 130, message = "Age is not valid") Integer age,
        @NotBlank(message = "Gender is required") String gender,
        String contact,
        String address,
        String bloodGroup,
        String medicalHistory,
        String allergies) {
}
