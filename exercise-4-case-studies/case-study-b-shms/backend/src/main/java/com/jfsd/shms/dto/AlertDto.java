package com.jfsd.shms.dto;

import com.jfsd.shms.model.Alert;
import com.jfsd.shms.model.Severity;

import java.time.LocalDateTime;

public record AlertDto(Long id, Long patientId, String patientName, String doctorName, String parameter,
                       String value, String message, Severity severity, LocalDateTime createdAt,
                       boolean acknowledged) {

    public static AlertDto from(Alert a) {
        return new AlertDto(a.getId(), a.getPatient().getId(), a.getPatient().getName(),
                a.getPatient().getDoctor() == null ? null : a.getPatient().getDoctor().getName(),
                a.getParameter(), a.getValue(), a.getMessage(), a.getSeverity(),
                a.getCreatedAt(), a.isAcknowledged());
    }
}
