package com.splitwise.application.models.dtos.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;


public class JwtOutputAuthentication implements Authentication {

    private final UserContextDto userContextDto;

    public JwtOutputAuthentication(UserContextDto user) {
        this.userContextDto = user;
    }

    @Override
    public Object getPrincipal() {
        return this.userContextDto;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
