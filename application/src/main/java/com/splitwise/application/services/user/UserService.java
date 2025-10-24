package com.splitwise.application.services.user;


import com.splitwise.application.models.entities.user.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findByMobile(String mobile);

    UserEntity save(UserEntity entity);
    Optional<UserEntity> findById(Long id);
    List<UserEntity> findByIds(List<Long> ids);
}
