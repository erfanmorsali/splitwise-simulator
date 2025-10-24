package com.splitwise.application.services.user;


import com.splitwise.application.models.dtos.user.UserFilter;
import com.splitwise.application.models.dtos.user.UserResponse;
import com.splitwise.application.models.entities.user.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findByMobile(String mobile);
    List<UserResponse> getAll(UserFilter filter);
    UserEntity save(UserEntity entity);
    Optional<UserEntity> findById(Long id);
    UserResponse getById(Long id);
    List<UserEntity> findByIds(List<Long> ids);
    List<UserEntity> findByGroupId(Long groupId);
}
