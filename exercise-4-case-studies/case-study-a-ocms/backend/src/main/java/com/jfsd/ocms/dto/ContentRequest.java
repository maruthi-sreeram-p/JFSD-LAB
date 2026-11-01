package com.jfsd.ocms.dto;

import com.jfsd.ocms.model.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContentRequest(
        @NotNull(message = "Choose a content type") ContentType type,
        @NotBlank(message = "Content title is required") String title,
        String url) {
}
