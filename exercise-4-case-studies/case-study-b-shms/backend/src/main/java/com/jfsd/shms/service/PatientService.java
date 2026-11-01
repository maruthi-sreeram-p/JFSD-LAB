package com.jfsd.shms.service;

import com.jfsd.shms.dto.PatientDto;
import com.jfsd.shms.dto.PatientRequest;
import com.jfsd.shms.model.Doctor;
import com.jfsd.shms.model.Patient;
import com.jfsd.shms.repository.DoctorRepository;
import com.jfsd.shms.repository.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patients;
    private final DoctorRepository doctors;

    public PatientService(PatientRepository patients, DoctorRepository doctors) {
        this.patients = patients;
        this.doctors = doctors;
    }

    @Transactional(readOnly = true)
    public List<PatientDto> all() {
        return patients.findAllByOrderByNameAsc().stream().map(PatientDto::from).toList();
    }

    @Transactional(readOnly = true)
    public PatientDto get(Long id) {
        return PatientDto.from(find(id));
    }

    @Transactional
    public PatientDto register(PatientRequest request) {
        Patient patient = new Patient();
        copy(request, patient);
        return PatientDto.from(patients.save(patient));
    }

    @Transactional
    public PatientDto update(Long id, PatientRequest request) {
        Patient patient = find(id);
        copy(request, patient);
        return PatientDto.from(patient);
    }

    /** Gives the patient to a doctor; doctorId = null takes the patient away from the doctor. */
    @Transactional
    public PatientDto assignDoctor(Long patientId, Long doctorId) {
        Patient patient = find(patientId);
        if (doctorId == null) {
            patient.setDoctor(null);
        } else {
            Doctor doctor = doctors.findById(doctorId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));
            patient.setDoctor(doctor);
        }
        return PatientDto.from(patient);
    }

    public Patient find(Long id) {
        return patients.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
    }

    private void copy(PatientRequest request, Patient patient) {
        patient.setName(request.name().trim());
        patient.setAge(request.age());
        patient.setGender(request.gender());
        patient.setContact(request.contact());
        patient.setAddress(request.address());
        patient.setBloodGroup(request.bloodGroup());
        patient.setMedicalHistory(request.medicalHistory());
        patient.setAllergies(request.allergies());
    }
}
