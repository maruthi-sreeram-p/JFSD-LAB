package com.jfsd.ocms.service;

import com.jfsd.ocms.dto.ContentRequest;
import com.jfsd.ocms.dto.CourseDto;
import com.jfsd.ocms.dto.CourseRequest;
import com.jfsd.ocms.dto.StudentProgressDto;
import com.jfsd.ocms.model.Course;
import com.jfsd.ocms.model.CourseContent;
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
public class CourseService {

    private final CourseRepository courses;
    private final UserRepository users;
    private final EnrollmentRepository enrollments;
    private final NotificationService notifications;

    public CourseService(CourseRepository courses, UserRepository users,
                         EnrollmentRepository enrollments, NotificationService notifications) {
        this.courses = courses;
        this.users = users;
        this.enrollments = enrollments;
        this.notifications = notifications;
    }

    // ---- reading ----

    @Transactional(readOnly = true)
    public List<CourseDto> all() {
        return courses.findAllByOrderByIdDesc().stream().map(CourseDto::from).toList();
    }

    @Transactional(readOnly = true)
    public CourseDto get(Long id) {
        return CourseDto.from(find(id));
    }

    @Transactional(readOnly = true)
    public List<CourseDto> mine(CurrentUser me) {
        return courses.findByInstructorIdOrderByIdDesc(me.id()).stream().map(CourseDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<StudentProgressDto> students(CurrentUser me, Long courseId) {
        Course course = ownCourse(me, courseId);
        return enrollments.findByCourseId(course.getId()).stream().map(StudentProgressDto::from).toList();
    }

    // ---- changing ----

    @Transactional
    public CourseDto create(CurrentUser me, CourseRequest request) {
        User instructor = users.findById(me.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User no longer exists"));

        Course course = new Course();
        course.setInstructor(instructor);
        course.setTitle(request.title().trim());
        course.setDescription(request.description());
        course.setCategory(request.category().trim());
        courses.save(course);

        notifications.notifyAllStudents("New course available: \"" + course.getTitle() + "\" ("
                + course.getCategory() + ") by " + instructor.getName());
        return CourseDto.from(course);
    }

    @Transactional
    public CourseDto update(CurrentUser me, Long id, CourseRequest request) {
        Course course = ownCourse(me, id);
        course.setTitle(request.title().trim());
        course.setDescription(request.description());
        course.setCategory(request.category().trim());

        notifications.notifyEnrolledStudents(course.getId(), "Course updated: \"" + course.getTitle() + "\"");
        return CourseDto.from(course);
    }

    @Transactional
    public void delete(CurrentUser me, Long id) {
        Course course = ownCourse(me, id);
        enrollments.deleteByCourseId(course.getId());
        courses.delete(course);
    }

    @Transactional
    public CourseDto addContent(CurrentUser me, Long courseId, ContentRequest request) {
        Course course = ownCourse(me, courseId);

        CourseContent content = new CourseContent();
        content.setType(request.type());
        content.setTitle(request.title().trim());
        content.setUrl(request.url());
        course.addContent(content);
        courses.saveAndFlush(course);

        refreshEnrollments(course.getId());
        notifications.notifyEnrolledStudents(course.getId(), "New " + content.getType().name().toLowerCase()
                + " added to \"" + course.getTitle() + "\": " + content.getTitle());
        return CourseDto.from(course);
    }

    @Transactional
    public CourseDto removeContent(CurrentUser me, Long courseId, Long contentId) {
        Course course = ownCourse(me, courseId);

        boolean removed = course.getContents().removeIf(c -> c.getId().equals(contentId));
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found in this course");
        }
        courses.saveAndFlush(course);

        refreshEnrollments(course.getId());
        return CourseDto.from(course);
    }

    // ---- helpers ----

    /** The progress of every student changes when content is added or removed. */
    private void refreshEnrollments(Long courseId) {
        enrollments.findByCourseId(courseId).forEach(Enrollment::refreshStatus);
    }

    private Course find(Long id) {
        return courses.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
    }

    /** Only the instructor who created a course may change it. */
    private Course ownCourse(CurrentUser me, Long id) {
        Course course = find(id);
        if (!course.getInstructor().getId().equals(me.id())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only manage your own courses");
        }
        return course;
    }
}
