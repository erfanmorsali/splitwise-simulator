package com.splitwise.application.services.user;


import com.splitwise.application.models.dtos.user.UserFilter;
import com.splitwise.application.models.dtos.user.UserResponse;
import com.splitwise.application.models.entities.user.UserEntity;
import com.splitwise.application.repositories.user.UserRepository;
import com.splitwise.shared.objects.ErrorCodes;
import com.splitwise.shared.objects.StatusCodes;
import com.splitwise.shared.objects.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Transactional(readOnly = true)
    public List<UserResponse> getAll(UserFilter filter) {
        return userRepository.findAll(filter.toSpecification(), filter.toPageable()).stream().map(UserResponse::new).collect(Collectors.toList());
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
    public UserResponse getById(Long id) {
        return new UserResponse(findByIdOrThrowException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> findByIds(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> findByGroupId(Long groupId) {
        return userRepository.findByGroupId(groupId);
    }

    private UserEntity findByIdOrThrowException(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new SystemException(StatusCodes.DATA_NOT_FOUND, ErrorCodes.USER_NOT_FOUND, id));
    }
}
