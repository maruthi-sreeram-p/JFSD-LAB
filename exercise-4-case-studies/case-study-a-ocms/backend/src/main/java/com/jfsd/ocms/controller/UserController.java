package com.jfsd.ocms.controller;

import com.jfsd.ocms.dto.UpdateProfileRequest;
import com.jfsd.ocms.dto.UserDto;
import com.jfsd.ocms.security.CurrentUser;
import com.jfsd.ocms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal CurrentUser me) {
        return userService.me(me);
    }

    @PutMapping("/me")
    public UserDto update(@AuthenticationPrincipal CurrentUser me, @Valid @RequestBody UpdateProfileRequest request) {
        return userService.update(me, request);
    }
}
