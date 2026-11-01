package com.jfsd.ocms.service;

import com.jfsd.ocms.dto.EnrollmentDto;
import com.jfsd.ocms.model.Course;
import com.jfsd.ocms.model.Enrollment;
import com.jfsd.ocms.model.User;
import com.jfsd.ocms.repository.CourseRepository;
import com.jfsd.ocms.repository.EnrollmentRepository;
import com.jfsd.ocms.repository.UserRepository;
import com.jfsd.ocms.security.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollments;
    private final CourseRepository courses;
    private final UserRepository users;
    private final NotificationService notifications;

    public EnrollmentService(EnrollmentRepository enrollments, CourseRepository courses,
                             UserRepository users, NotificationService notifications) {
        this.enrollments = enrollments;
        this.courses = courses;
        this.users = users;
        this.notifications = notifications;
    }

    @Transactional
    public EnrollmentDto enroll(CurrentUser me, Long courseId) {
        Course course = courses.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        if (enrollments.existsByStudentIdAndCourseId(me.id(), courseId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You are already enrolled in this course");
        }

        User student = users.findById(me.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User no longer exists"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.refreshStatus();
        enrollments.save(enrollment);

        // tell the instructor
        notifications.notifyUser(course.getInstructor(),
                student.getName() + " enrolled in \"" + course.getTitle() + "\"");
        return EnrollmentDto.from(enrollment);
    }

    /** The history of courses taken by the student, newest first. */
    @Transactional(readOnly = true)
    public List<EnrollmentDto> mine(CurrentUser me) {
        return enrollments.findByStudentIdOrderByEnrolledOnDescIdDesc(me.id())
                .stream().map(EnrollmentDto::from).toList();
    }

    /** Ticks (done = true) or unticks one content item of the student's course. */
    @Transactional
    public EnrollmentDto markContent(CurrentUser me, Long enrollmentId, Long contentId, boolean done) {
        Enrollment enrollment = enrollments.findById(enrollmentId)
                .filter(e -> e.getStudent().getId().equals(me.id()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment not found"));

        boolean inCourse = enrollment.getCourse().getContents().stream().anyMatch(c -> c.getId().equals(contentId));
        if (!inCourse) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "That item is not part of this course");
        }

        if (done) {
            enrollment.getCompletedContentIds().add(contentId);
        } else {
            enrollment.getCompletedContentIds().remove(contentId);
        }
        enrollment.refreshStatus();
        return EnrollmentDto.from(enrollment);
    }
}
