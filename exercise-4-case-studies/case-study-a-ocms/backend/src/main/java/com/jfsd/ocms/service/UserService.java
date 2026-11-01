package com.jfsd.ocms.service;

import com.jfsd.ocms.dto.UpdateProfileRequest;
import com.jfsd.ocms.dto.UserDto;
import com.jfsd.ocms.model.User;
import com.jfsd.ocms.repository.UserRepository;
import com.jfsd.ocms.security.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    @Transactional(readOnly = true)
    public UserDto me(CurrentUser current) {
        return UserDto.from(find(current));
    }

    @Transactional
    public UserDto update(CurrentUser current, UpdateProfileRequest request) {
        User user = find(current);
        String email = request.email().trim().toLowerCase();

        if (users.existsByEmailAndIdNot(email, user.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email is used by another account");
        }
        user.setName(request.name().trim());
        user.setEmail(email);
        user.setContact(request.contact());
        return UserDto.from(user);
    }

    private User find(CurrentUser current) {
        return users.findById(current.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User no longer exists"));
    }
}
