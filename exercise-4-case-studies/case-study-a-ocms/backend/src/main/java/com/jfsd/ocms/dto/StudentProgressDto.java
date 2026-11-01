package com.jfsd.ocms.dto;

import com.jfsd.ocms.model.Enrollment;
import com.jfsd.ocms.model.EnrollmentStatus;

import java.time.LocalDate;

/**
 * What an instructor sees about one enrolled student.
 */
public record StudentProgressDto(Long enrollmentId, Long studentId, String name, String email, String contact,
                                 LocalDate enrolledOn, EnrollmentStatus status,
                                 int progress, int completed, int total) {

    public static StudentProgressDto from(Enrollment e) {
        return new StudentProgressDto(e.getId(), e.getStudent().getId(), e.getStudent().getName(),
                e.getStudent().getEmail(), e.getStudent().getContact(),
                e.getEnrolledOn(), e.getStatus(),
                e.progressPercent(), e.completedCount(), e.totalContent());
    }
}
