package com.jfsd.shms.service;

import com.jfsd.shms.dto.AlertDto;
import com.jfsd.shms.dto.DashboardDto;
import com.jfsd.shms.dto.DashboardDto.PatientSummary;
import com.jfsd.shms.dto.DoctorDto;
import com.jfsd.shms.dto.DoctorRequest;
import com.jfsd.shms.dto.PatientDto;
import com.jfsd.shms.dto.VitalDto;
import com.jfsd.shms.model.Doctor;
import com.jfsd.shms.model.Patient;
import com.jfsd.shms.model.VitalReading;
import com.jfsd.shms.repository.AlertRepository;
import com.jfsd.shms.repository.DoctorRepository;
import com.jfsd.shms.repository.PatientRepository;
import com.jfsd.shms.repository.VitalReadingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class DoctorService {

    // the dashboard lists the patients who need attention first
    private static final Map<String, Integer> STATUS_ORDER =
            Map.of("CRITICAL", 0, "WARNING", 1, "STABLE", 2, "NO_DATA", 3);

    private final DoctorRepository doctors;
    private final PatientRepository patients;
    private final VitalReadingRepository vitals;
    private final AlertRepository alerts;

    public DoctorService(DoctorRepository doctors, PatientRepository patients,
                         VitalReadingRepository vitals, AlertRepository alerts) {
        this.doctors = doctors;
        this.patients = patients;
        this.vitals = vitals;
        this.alerts = alerts;
    }

    @Transactional
    public DoctorDto add(DoctorRequest request) {
        Doctor doctor = new Doctor();
        doctor.setName(request.name().trim());
        doctor.setSpecialization(request.specialization());
        doctor.setContact(request.contact());
        return DoctorDto.from(doctors.save(doctor), 0);
    }

    @Transactional(readOnly = true)
    public List<DoctorDto> all() {
        return doctors.findAllByOrderByNameAsc().stream()
                .map(d -> DoctorDto.from(d, patients.countByDoctorId(d.getId())))
                .toList();
    }

    /** Assigned patients with their latest vitals, plus the alerts that nobody has acknowledged yet. */
    @Transactional(readOnly = true)
    public DashboardDto dashboard(Long doctorId) {
        Doctor doctor = doctors.findById(doctorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));

        List<Patient> assigned = patients.findByDoctorIdOrderByNameAsc(doctorId);

        List<PatientSummary> summaries = assigned.stream()
                .map(this::summarise)
                .sorted(Comparator.comparingInt((PatientSummary s) -> STATUS_ORDER.get(s.status())))
                .toList();

        List<AlertDto> open = alerts.findByPatientDoctorIdOrderByCreatedAtDescIdDesc(doctorId).stream()
                .filter(a -> !a.isAcknowledged())
                .map(AlertDto::from)
                .toList();

        return new DashboardDto(DoctorDto.from(doctor, assigned.size()), summaries, open);
    }

    private PatientSummary summarise(Patient patient) {
        VitalReading latest = vitals.findFirstByPatientIdOrderByRecordedAtDescIdDesc(patient.getId()).orElse(null);
        VitalDto latestDto = latest == null ? null : VitalDto.from(latest);
        String status = latestDto == null ? "NO_DATA" : latestDto.status();
        return new PatientSummary(PatientDto.from(patient), latestDto, status,
                alerts.countByPatientIdAndAcknowledgedFalse(patient.getId()));
    }
}
