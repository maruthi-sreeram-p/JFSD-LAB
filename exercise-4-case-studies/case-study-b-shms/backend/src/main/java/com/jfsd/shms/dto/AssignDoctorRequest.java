package com.jfsd.shms.dto;

/**
 * doctorId = null removes the doctor from the patient.
 */
public record AssignDoctorRequest(Long doctorId) {
}
