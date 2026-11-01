package com.jfsd.ocms.dto;

import com.jfsd.ocms.model.Role;
import com.jfsd.ocms.model.User;

public record UserDto(Long id, String name, String email, String contact, Role role) {

    public static UserDto from(User u) {
        return new UserDto(u.getId(), u.getName(), u.getEmail(), u.getContact(), u.getRole());
    }
}
