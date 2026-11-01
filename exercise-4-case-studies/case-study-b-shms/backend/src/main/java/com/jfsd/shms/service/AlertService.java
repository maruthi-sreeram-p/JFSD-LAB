package com.jfsd.shms.service;

import com.jfsd.shms.dto.AlertDto;
import com.jfsd.shms.model.Alert;
import com.jfsd.shms.repository.AlertRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alerts;

    public AlertService(AlertRepository alerts) {
        this.alerts = alerts;
    }

    /**
     * Newest first. Filter by patient or by doctor (the doctor's patients); openOnly hides acknowledged alerts.
     */
    @Transactional(readOnly = true)
    public List<AlertDto> search(Long patientId, Long doctorId, boolean openOnly) {
        List<Alert> found;
        if (patientId != null) {
            found = alerts.findByPatientIdOrderByCreatedAtDescIdDesc(patientId);
        } else if (doctorId != null) {
            found = alerts.findByPatientDoctorIdOrderByCreatedAtDescIdDesc(doctorId);
        } else {
            found = alerts.findAllByOrderByCreatedAtDescIdDesc();
        }
        return found.stream()
                .filter(a -> !openOnly || !a.isAcknowledged())
                .map(AlertDto::from)
                .toList();
    }

    @Transactional
    public AlertDto acknowledge(Long id) {
        Alert alert = alerts.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alert not found"));
        alert.setAcknowledged(true);
        return AlertDto.from(alert);
    }
}
