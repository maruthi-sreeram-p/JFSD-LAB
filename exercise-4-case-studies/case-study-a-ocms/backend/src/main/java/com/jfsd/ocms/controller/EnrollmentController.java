package com.jfsd.ocms.controller;

import com.jfsd.ocms.dto.EnrollmentDto;
import com.jfsd.ocms.dto.EnrollmentRequest;
import com.jfsd.ocms.security.CurrentUser;
import com.jfsd.ocms.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@PreAuthorize("hasRole('STUDENT')")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentDto enroll(@AuthenticationPrincipal CurrentUser me, @Valid @RequestBody EnrollmentRequest request) {
        return enrollmentService.enroll(me, request.courseId());
    }

    @GetMapping("/mine")
    public List<EnrollmentDto> mine(@AuthenticationPrincipal CurrentUser me) {
        return enrollmentService.mine(me);
    }

    @PostMapping("/{id}/contents/{contentId}/complete")
    public EnrollmentDto complete(@AuthenticationPrincipal CurrentUser me, @PathVariable Long id,
                                  @PathVariable Long contentId) {
        return enrollmentService.markContent(me, id, contentId, true);
    }

    @DeleteMapping("/{id}/contents/{contentId}/complete")
    public EnrollmentDto undo(@AuthenticationPrincipal CurrentUser me, @PathVariable Long id,
                              @PathVariable Long contentId) {
        return enrollmentService.markContent(me, id, contentId, false);
    }
}
