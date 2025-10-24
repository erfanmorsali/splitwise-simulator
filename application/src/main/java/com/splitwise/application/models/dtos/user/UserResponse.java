package com.splitwise.application.models.dtos.user;


import com.splitwise.application.models.entities.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String mobile;
    private String name;

    public UserResponse(UserEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.mobile = entity.getMobile();
        }

    }
}
