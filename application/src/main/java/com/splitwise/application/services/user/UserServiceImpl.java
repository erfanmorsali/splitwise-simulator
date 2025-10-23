package com.splitwise.application.services.user;


import com.splitwise.application.models.entities.user.UserEntity;
import com.splitwise.application.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<UserEntity> findByMobile(String mobile) {
        return userRepository.findByMobile(mobile);
    }

    @Override
    public UserEntity save(UserEntity entity) {
        return userRepository.save(entity);
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }
}
