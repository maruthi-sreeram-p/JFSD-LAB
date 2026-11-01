package com.jfsd.shms.service;

import com.jfsd.shms.dto.AlertDto;
import com.jfsd.shms.dto.VitalDto;
import com.jfsd.shms.dto.VitalRequest;
import com.jfsd.shms.dto.VitalResultDto;
import com.jfsd.shms.model.Alert;
import com.jfsd.shms.model.Patient;
import com.jfsd.shms.model.VitalReading;
import com.jfsd.shms.repository.AlertRepository;
import com.jfsd.shms.repository.VitalReadingRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VitalService {

    private final VitalReadingRepository vitals;
    private final AlertRepository alerts;
    private final PatientService patientService;

    public VitalService(VitalReadingRepository vitals, AlertRepository alerts, PatientService patientService) {
        this.vitals = vitals;
        this.alerts = alerts;
        this.patientService = patientService;
    }

    /** Saves a reading and raises an alert for every value that is outside the safe range. */
    @Transactional
    public VitalResultDto record(Long patientId, VitalRequest request) {
        Patient patient = patientService.find(patientId);

        VitalReading reading = new VitalReading();
        reading.setPatient(patient);
        reading.setHeartRate(request.heartRate());
        reading.setSystolic(request.systolic());
        reading.setDiastolic(request.diastolic());
        reading.setOxygenLevel(request.oxygenLevel());
        reading.setTemperature(request.temperature());
        vitals.save(reading);

        List<AlertDto> raised = SafeRanges.check(reading).stream()
                .map(f -> alerts.save(new Alert(patient, reading, f.parameter(), f.value(), f.message(), f.severity())))
                .map(AlertDto::from)
                .toList();

        return new VitalResultDto(VitalDto.from(reading), raised);
    }

    /** Newest readings first. */
    @Transactional(readOnly = true)
    public List<VitalDto> history(Long patientId, int limit) {
        patientService.find(patientId);
        return vitals.findByPatientIdOrderByRecordedAtDescIdDesc(patientId, PageRequest.of(0, limit))
                .stream().map(VitalDto::from).toList();
    }
}
