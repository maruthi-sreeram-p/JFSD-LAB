package com.jfsd.shms.controller;

import com.jfsd.shms.dto.DashboardDto;
import com.jfsd.shms.dto.DoctorDto;
import com.jfsd.shms.dto.DoctorRequest;
import com.jfsd.shms.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public List<DoctorDto> all() {
        return doctorService.all();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto add(@Valid @RequestBody DoctorRequest request) {
        return doctorService.add(request);
    }

    @GetMapping("/{id}/dashboard")
    public DashboardDto dashboard(@PathVariable Long id) {
        return doctorService.dashboard(id);
    }
}
