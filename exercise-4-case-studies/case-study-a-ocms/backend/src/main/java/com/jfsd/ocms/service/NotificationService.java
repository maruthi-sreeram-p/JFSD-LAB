package com.jfsd.ocms.service;

import com.jfsd.ocms.dto.NotificationDto;
import com.jfsd.ocms.model.Enrollment;
import com.jfsd.ocms.model.EnrollmentStatus;
import com.jfsd.ocms.model.Notification;
import com.jfsd.ocms.model.Role;
import com.jfsd.ocms.model.User;
import com.jfsd.ocms.repository.EnrollmentRepository;
import com.jfsd.ocms.repository.NotificationRepository;
import com.jfsd.ocms.repository.UserRepository;
import com.jfsd.ocms.security.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notifications;
    private final UserRepository users;
    private final EnrollmentRepository enrollments;

    public NotificationService(NotificationRepository notifications, UserRepository users,
                               EnrollmentRepository enrollments) {
        this.notifications = notifications;
        this.users = users;
        this.enrollments = enrollments;
    }

    // ---- used by the other services to raise notifications ----

    @Transactional
    public void notifyUser(User user, String message) {
        notifications.save(new Notification(user, message));
    }

    /** New course: every student gets an alert. */
    @Transactional
    public void notifyAllStudents(String message) {
        for (User student : users.findByRole(Role.STUDENT)) {
            notifications.save(new Notification(student, message));
        }
    }

    /** Course updated: only the students enrolled in it. */
    @Transactional
    public void notifyEnrolledStudents(Long courseId, String message) {
        for (Enrollment e : enrollments.findByCourseId(courseId)) {
            notifications.save(new Notification(e.getStudent(), message));
        }
    }

    /**
     * Reminds every student who has unfinished content in an ongoing course.
     * Called by the scheduler.
     *
     * @return how many reminders were created
     */
    @Transactional
    public int sendReminders() {
        int sent = 0;
        for (Enrollment e : enrollments.findByStatus(EnrollmentStatus.ONGOING)) {
            int pending = e.pendingCount();
            if (pending > 0) {
                notifications.save(new Notification(e.getStudent(),
                        "Reminder: " + pending + " item(s) are still pending in \"" + e.getCourse().getTitle() + "\""));
                sent++;
            }
        }
        return sent;
    }

    // ---- used by the notification API ----

    @Transactional(readOnly = true)
    public List<NotificationDto> mine(CurrentUser current) {
        return notifications.findByUserIdOrderByCreatedAtDescIdDesc(current.id())
                .stream().map(NotificationDto::from).toList();
    }

    @Transactional(readOnly = true)
    public long unreadCount(CurrentUser current) {
        return notifications.countByUserIdAndReadFalse(current.id());
    }

    @Transactional
    public void markRead(CurrentUser current, Long id) {
        Notification n = notifications.findById(id)
                .filter(x -> x.getUser().getId().equals(current.id()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
        n.setRead(true);
    }

    @Transactional
    public void markAllRead(CurrentUser current) {
        notifications.findByUserIdAndReadFalse(current.id()).forEach(n -> n.setRead(true));
    }
}
