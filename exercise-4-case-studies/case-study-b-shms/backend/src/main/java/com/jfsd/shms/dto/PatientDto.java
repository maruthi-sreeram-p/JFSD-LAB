package com.jfsd.shms.dto;

import com.jfsd.shms.model.Patient;

public record PatientDto(Long id, String name, int age, String gender, String contact, String address,
                         String bloodGroup, String medicalHistory, String allergies,
                         Long doctorId, String doctorName) {

    public static PatientDto from(Patient p) {
        return new PatientDto(p.getId(), p.getName(), p.getAge(), p.getGender(), p.getContact(), p.getAddress(),
                p.getBloodGroup(), p.getMedicalHistory(), p.getAllergies(),
                p.getDoctor() == null ? null : p.getDoctor().getId(),
                p.getDoctor() == null ? null : p.getDoctor().getName());
    }
}
