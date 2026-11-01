package com.jfsd.shms.dto;

import java.util.List;

/**
 * Everything the doctor's dashboard shows, in one response so the page can refresh with a single call.
 */
public record DashboardDto(DoctorDto doctor, List<PatientSummary> patients, List<AlertDto> openAlerts) {

    /** status: CRITICAL, WARNING, STABLE or NO_DATA (nothing recorded yet) */
    public record PatientSummary(PatientDto patient, VitalDto latest, String status, long openAlerts) {
    }
}
