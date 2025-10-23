package com.splitwise.application.models.dtos.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserContextDto {
    private Long id;

    public UserContextDto(Long id) {
        this.id = id;
    }
}
