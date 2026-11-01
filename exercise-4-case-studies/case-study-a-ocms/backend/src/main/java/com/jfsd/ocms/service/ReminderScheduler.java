package com.jfsd.ocms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Runs on the schedule in application.properties (ocms.reminder.cron).
 */
@Component
public class ReminderScheduler {

    private static final Logger log = LoggerFactory.getLogger(ReminderScheduler.class);

    private final NotificationService notificationService;

    public ReminderScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "${ocms.reminder.cron}")
    public void sendPendingCourseworkReminders() {
        int sent = notificationService.sendReminders();
        log.info("Coursework reminders sent: {}", sent);
    }
}
