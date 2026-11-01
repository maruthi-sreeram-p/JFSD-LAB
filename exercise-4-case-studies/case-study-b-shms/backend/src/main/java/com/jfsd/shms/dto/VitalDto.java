package com.jfsd.shms.dto;

import com.jfsd.shms.model.VitalReading;
import com.jfsd.shms.service.SafeRanges;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A reading plus its verdict: status is STABLE, WARNING or CRITICAL and
 * flags says which values are out of range (key -> severity).
 */
public record VitalDto(Long id, Long patientId, LocalDateTime recordedAt, int heartRate, int systolic,
                       int diastolic, int oxygenLevel, double temperature,
                       String status, Map<String, String> flags) {

    public static VitalDto from(VitalReading r) {
        List<SafeRanges.Finding> findings = SafeRanges.check(r);

        Map<String, String> flags = new HashMap<>();
        findings.forEach(f -> flags.put(f.key(), f.severity().name()));

        var worst = SafeRanges.worst(findings);
        return new VitalDto(r.getId(), r.getPatient().getId(), r.getRecordedAt(), r.getHeartRate(),
                r.getSystolic(), r.getDiastolic(), r.getOxygenLevel(), r.getTemperature(),
                worst == null ? "STABLE" : worst.name(), flags);
    }
}
