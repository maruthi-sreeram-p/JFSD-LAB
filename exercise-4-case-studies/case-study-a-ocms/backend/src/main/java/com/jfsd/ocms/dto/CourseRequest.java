package com.jfsd.ocms.dto;

import jakarta.validation.constraints.NotBlank;

public record CourseRequest(
        @NotBlank(message = "Title is required") String title,
        String description,
        @NotBlank(message = "Category is required") String category) {
}
