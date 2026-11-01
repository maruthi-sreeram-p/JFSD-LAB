package com.jfsd.ocms.dto;

import com.jfsd.ocms.model.ContentType;
import com.jfsd.ocms.model.Enrollment;
import com.jfsd.ocms.model.EnrollmentStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * One course as seen by the student who is enrolled in it.
 */
public record EnrollmentDto(Long id, Long courseId, String courseTitle, String category, String instructorName,
                            LocalDate enrolledOn, EnrollmentStatus status,
                            int progress, int completed, int total, List<Item> contents) {

    public record Item(Long id, ContentType type, String title, String url, boolean done) {
    }

    public static EnrollmentDto from(Enrollment e) {
        List<Item> items = e.getCourse().getContents().stream()
                .map(c -> new Item(c.getId(), c.getType(), c.getTitle(), c.getUrl(), e.isContentDone(c.getId())))
                .toList();
        return new EnrollmentDto(e.getId(), e.getCourse().getId(), e.getCourse().getTitle(),
                e.getCourse().getCategory(), e.getCourse().getInstructor().getName(),
                e.getEnrolledOn(), e.getStatus(),
                e.progressPercent(), e.completedCount(), e.totalContent(), items);
    }
}
