package com.splitwise.application.services.user;


import com.splitwise.application.models.entities.user.UserEntity;
import com.splitwise.application.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    @Transactional(readOnly = true)
    public Optional<UserEntity> findByMobile(String mobile) {
        return userRepository.findByMobile(mobile);
    }

    @Override
    @Transactional
    public UserEntity save(UserEntity entity) {
        return userRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> findByIds(List<Long> ids) {
        return userRepository.findAllById(ids);
    }
}
