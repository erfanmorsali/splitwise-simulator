package com.splitwise.application.services.group;


import com.splitwise.application.models.dtos.group.CostFilter;
import com.splitwise.application.models.dtos.group.CostResponse;
import com.splitwise.application.models.dtos.group.CreateCostRequest;
import com.splitwise.application.models.entities.group.CostEntity;
import com.splitwise.application.models.entities.group.GroupEntity;
import com.splitwise.application.models.entities.user.UserEntity;
import com.splitwise.application.repositories.group.CostRepository;
import com.splitwise.application.security.JwtUser;
import com.splitwise.shared.objects.ErrorCodes;
import com.splitwise.shared.objects.StatusCodes;
import com.splitwise.shared.objects.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CostServiceImpl implements CostService {
    private final CostRepository costRepository;
    private final GroupService groupService;


    @Override
    @Transactional(readOnly = true)
    public List<CostResponse> getAll(CostFilter filter) {
        return costRepository
                .findAll(filter.toSpecification(), filter.toPageable())
                .stream()
                .map(CostResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CostResponse getById(Long id, Long userId, Long groupId) {
        CostEntity cost = costRepository.findByIdAndUserIdAndGroupId(id, userId, groupId)
                .orElseThrow(() -> new SystemException(StatusCodes.DATA_NOT_FOUND, ErrorCodes.COST_NOT_FOUND, id));
        return new CostResponse(cost);
    }

    @Override
    public CostResponse create(CreateCostRequest request, Long groupId) {
        Long userId = JwtUser.getAuthenticatedUser().getId();

        request.getInvolvedUsers().remove(userId);
        findGroupByIdAndCheckUsersBelongToGroup(userId, request.getInvolvedUsers(), groupId);
        return null;
    }


    private void findGroupByIdAndCheckUsersBelongToGroup(Long userId, Set<Long> userIds, Long groupId) {
        GroupEntity group = groupService.findByIdAndFetchUsersOrThrowException(groupId);

        Set<Long> groupUserIds = group.getUsers().stream()
                .map(UserEntity::getId)
                .collect(Collectors.toSet());

        Set<Long> invalidUserIds = new HashSet<>(userIds);
        invalidUserIds.add(userId);
        invalidUserIds.removeAll(groupUserIds);

        if (!invalidUserIds.isEmpty()) {
            throw new SystemException(StatusCodes.BAD_REQUEST, ErrorCodes.NOT_MEMBER_OF_GROUP, invalidUserIds);
        }
    }
}
