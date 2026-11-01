package com.jfsd.ocms.security;

import com.jfsd.ocms.model.Role;

/**
 * The logged in user, rebuilt from the token on every request.
 * Controllers receive it with @AuthenticationPrincipal.
 */
public record CurrentUser(Long id, Role role) {
}
