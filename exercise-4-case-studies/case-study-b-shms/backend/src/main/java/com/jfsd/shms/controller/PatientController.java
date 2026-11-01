package com.jfsd.shms.controller;

import com.jfsd.shms.dto.AssignDoctorRequest;
import com.jfsd.shms.dto.PatientDto;
import com.jfsd.shms.dto.PatientRequest;
import com.jfsd.shms.dto.VitalDto;
import com.jfsd.shms.dto.VitalRequest;
import com.jfsd.shms.dto.VitalResultDto;
import com.jfsd.shms.service.PatientService;
import com.jfsd.shms.service.VitalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;
    private final VitalService vitalService;

    public PatientController(PatientService patientService, VitalService vitalService) {
        this.patientService = patientService;
        this.vitalService = vitalService;
    }

    @GetMapping
    public List<PatientDto> all() {
        return patientService.all();
    }

    @GetMapping("/{id}")
    public PatientDto get(@PathVariable Long id) {
        return patientService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto register(@Valid @RequestBody PatientRequest request) {
        return patientService.register(request);
    }

    @PutMapping("/{id}")
    public PatientDto update(@PathVariable Long id, @Valid @RequestBody PatientRequest request) {
        return patientService.update(id, request);
    }

    @PutMapping("/{id}/doctor")
    public PatientDto assignDoctor(@PathVariable Long id, @RequestBody AssignDoctorRequest request) {
        return patientService.assignDoctor(id, request.doctorId());
    }

    // ---- vital statistics of a patient ----

    @PostMapping("/{id}/vitals")
    @ResponseStatus(HttpStatus.CREATED)
    public VitalResultDto recordVitals(@PathVariable Long id, @Valid @RequestBody VitalRequest request) {
        return vitalService.record(id, request);
    }

    @GetMapping("/{id}/vitals")
    public List<VitalDto> vitals(@PathVariable Long id, @RequestParam(defaultValue = "20") int limit) {
        return vitalService.history(id, Math.max(1, Math.min(limit, 200)));
    }
}
