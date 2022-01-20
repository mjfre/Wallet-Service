package com.website.WalletService.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Set;

public enum ApplicationUserRole {
    STUDENT,
    TEACHER,
    ADMIN,
    SUPERUSER;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + this.name()));
    }
}

