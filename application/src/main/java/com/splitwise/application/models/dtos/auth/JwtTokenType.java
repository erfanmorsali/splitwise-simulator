package com.splitwise.application.models.dtos.auth;

import lombok.Getter;

@Getter
public enum JwtTokenType {
    ACCESS_TOKEN, REFRESH_TOKEN
}
