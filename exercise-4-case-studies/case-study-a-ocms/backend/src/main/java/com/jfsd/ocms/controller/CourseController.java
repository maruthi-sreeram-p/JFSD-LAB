package com.jfsd.ocms.controller;

import com.jfsd.ocms.dto.ContentRequest;
import com.jfsd.ocms.dto.CourseDto;
import com.jfsd.ocms.dto.CourseRequest;
import com.jfsd.ocms.dto.StudentProgressDto;
import com.jfsd.ocms.security.CurrentUser;
import com.jfsd.ocms.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Anyone logged in can read courses. Only instructors can create or change them
 * (@PreAuthorize), and only the instructor who owns the course (checked in CourseService).
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseDto> all() {
        return courseService.all();
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public List<CourseDto> mine(@AuthenticationPrincipal CurrentUser me) {
        return courseService.mine(me);
    }

    @GetMapping("/{id}")
    public CourseDto get(@PathVariable Long id) {
        return courseService.get(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDto create(@AuthenticationPrincipal CurrentUser me, @Valid @RequestBody CourseRequest request) {
        return courseService.create(me, request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public CourseDto update(@AuthenticationPrincipal CurrentUser me, @PathVariable Long id,
                            @Valid @RequestBody CourseRequest request) {
        return courseService.update(me, id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal CurrentUser me, @PathVariable Long id) {
        courseService.delete(me, id);
    }

    @PostMapping("/{id}/contents")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDto addContent(@AuthenticationPrincipal CurrentUser me, @PathVariable Long id,
                                @Valid @RequestBody ContentRequest request) {
        return courseService.addContent(me, id, request);
    }

    @DeleteMapping("/{id}/contents/{contentId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public CourseDto removeContent(@AuthenticationPrincipal CurrentUser me, @PathVariable Long id,
                                   @PathVariable Long contentId) {
        return courseService.removeContent(me, id, contentId);
    }

    @GetMapping("/{id}/students")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public List<StudentProgressDto> students(@AuthenticationPrincipal CurrentUser me, @PathVariable Long id) {
        return courseService.students(me, id);
    }
}
