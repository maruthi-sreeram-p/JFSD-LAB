package com.jfsd.ocms.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Links a student to a course. Keeps the enrolment date, the status and
 * which content items the student has finished.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDate enrolledOn = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status = EnrollmentStatus.ONGOING;

    @ElementCollection
    @CollectionTable(name = "enrollment_completed_content", joinColumns = @JoinColumn(name = "enrollment_id"))
    @Column(name = "content_id")
    private Set<Long> completedContentIds = new HashSet<>();

    public Long getId() {
        return id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getEnrolledOn() {
        return enrolledOn;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public Set<Long> getCompletedContentIds() {
        return completedContentIds;
    }

    public boolean isContentDone(Long contentId) {
        return completedContentIds.contains(contentId);
    }

    public int totalContent() {
        return course.getContents().size();
    }

    public int completedCount() {
        return (int) course.getContents().stream().filter(c -> isContentDone(c.getId())).count();
    }

    public int pendingCount() {
        return totalContent() - completedCount();
    }

    public int progressPercent() {
        int total = totalContent();
        return total == 0 ? 0 : Math.round(completedCount() * 100f / total);
    }

    /** Call after the progress or the course content changed. */
    public void refreshStatus() {
        boolean finished = totalContent() > 0 && pendingCount() == 0;
        status = finished ? EnrollmentStatus.COMPLETED : EnrollmentStatus.ONGOING;
    }
}
