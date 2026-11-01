package com.jfsd.shms.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record VitalRequest(
        @NotNull(message = "Heart rate is required") @Min(value = 20, message = "Heart rate must be between 20 and 250") @Max(value = 250, message = "Heart rate must be between 20 and 250") Integer heartRate,
        @NotNull(message = "Systolic pressure is required") @Min(value = 40, message = "Systolic pressure must be between 40 and 260") @Max(value = 260, message = "Systolic pressure must be between 40 and 260") Integer systolic,
        @NotNull(message = "Diastolic pressure is required") @Min(value = 20, message = "Diastolic pressure must be between 20 and 180") @Max(value = 180, message = "Diastolic pressure must be between 20 and 180") Integer diastolic,
        @NotNull(message = "Oxygen level is required") @Min(value = 50, message = "Oxygen level must be between 50 and 100") @Max(value = 100, message = "Oxygen level must be between 50 and 100") Integer oxygenLevel,
        @NotNull(message = "Temperature is required") @DecimalMin(value = "30.0", message = "Temperature must be between 30 and 45 C") @DecimalMax(value = "45.0", message = "Temperature must be between 30 and 45 C") Double temperature) {
}
