package com.jfsd.shms.dto;

import com.jfsd.shms.model.Doctor;

public record DoctorDto(Long id, String name, String specialization, String contact, long patientCount) {

    public static DoctorDto from(Doctor d, long patientCount) {
        return new DoctorDto(d.getId(), d.getName(), d.getSpecialization(), d.getContact(), patientCount);
    }
}
