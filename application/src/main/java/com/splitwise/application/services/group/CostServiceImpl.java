package com.splitwise.application.services.group;


import com.splitwise.application.models.dtos.group.CostEventMessage;
import com.splitwise.application.models.dtos.group.CostFilter;
import com.splitwise.application.models.dtos.group.CostResponse;
import com.splitwise.application.models.dtos.group.CreateCostRequest;
import com.splitwise.application.models.entities.group.CostDocumentEntity;
import com.splitwise.application.models.entities.group.CostEntity;
import com.splitwise.application.models.entities.group.GroupEntity;
import com.splitwise.application.models.entities.user.UserEntity;
import com.splitwise.application.repositories.group.CostRepository;
import com.splitwise.application.security.JwtUser;
import com.splitwise.application.services.event.EventService;
import com.splitwise.application.services.user.UserService;
import com.splitwise.application.statics.Caches;
import com.splitwise.application.statics.Topics;
import com.splitwise.shared.objects.ErrorCodes;
import com.splitwise.shared.objects.StatusCodes;
import com.splitwise.shared.objects.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CostServiceImpl implements CostService {
    private final CostRepository costRepository;
    private final GroupService groupService;
    private final UserService userService;
    private final EventService eventService;


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = Caches.COST_LIST, key = "#filter.hashCode()")
    public List<CostResponse> getAll(CostFilter filter) {
        return costRepository
                .findAll(filter.toSpecification(), filter.toPageable())
                .stream()
                .map(CostResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = Caches.COST, key = "#id")
    public CostResponse getById(Long id, Long userId, Long groupId) {
        CostEntity cost = costRepository.findByIdAndUserIdAndGroupId(id, userId, groupId)
                .orElseThrow(() -> new SystemException(StatusCodes.DATA_NOT_FOUND, ErrorCodes.COST_NOT_FOUND, id));
        return new CostResponse(cost);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = Caches.COST, key = "#result.id")
    public CostResponse create(CreateCostRequest request, Long groupId) {
        Long userId = JwtUser.getAuthenticatedUser().getId();

        request.getInvolvedUsers().remove(userId);
        findGroupByIdAndCheckUsersBelongToGroup(userId, request.getInvolvedUsers(), groupId);

        CostEntity costEntity = createCostEntity(request, userId, groupId);
        costRepository.save(costEntity);

        createCostDocuments(costEntity, request);
        eventService.createEvent(createEventPayloadForCost(costEntity, CostEventMessage.CostOperation.CREATED), Topics.COST);

        return new CostResponse(costEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {Caches.COST, Caches.COST_LIST}, allEntries = true)
    public boolean delete(Long id) {
        Long userId = JwtUser.getAuthenticatedUser().getId();
        CostEntity cost = findByIdOrThrowException(id);
        validateOwnership(cost, userId);
        cost.setDeleted(LocalDateTime.now());

        costRepository.save(cost);
        return true;
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

    private CostEntity createCostEntity(CreateCostRequest request, Long groupId, Long userId) {
        List<UserEntity> involvedUsers = userService.findByIds(new ArrayList<>(request.getInvolvedUsers()));

        CostEntity costEntity = request.convertToEntity(null);
        costEntity.setGroupId(groupId);
        costEntity.setCreatorId(userId);
        costEntity.setInvolvedUsers(new HashSet<>(involvedUsers));
        return costEntity;
    }

    private CostEntity findByIdOrThrowException(Long id) {
        return costRepository.findById(id)
                .orElseThrow(() -> new SystemException(StatusCodes.DATA_NOT_FOUND, ErrorCodes.COST_NOT_FOUND, id));
    }

    private void validateOwnership(CostEntity cost, Long userId) {
        if (!cost.getCreatorId().equals(userId)) {
            throw new SystemException(StatusCodes.ACCESS_DENIED, ErrorCodes.NOT_OWNER_OF_GROUP, "cant change this cost");
        }
    }

    private void createCostDocuments(CostEntity cost, CreateCostRequest request) {
        Set<Long> involvedUserIds = request.getInvolvedUsers();
        BigDecimal amount = cost.getAmount();
        BigDecimal amountPerUser = amount.divide(BigDecimal.valueOf(involvedUserIds.size()), RoundingMode.HALF_UP);

        Set<CostDocumentEntity> documents = involvedUserIds.stream()
                .map(userId -> createCostDocumentEntity(cost, amountPerUser, userId))
                .collect(Collectors.toSet());

        cost.setDocuments(documents);
    }

    private CostDocumentEntity createCostDocumentEntity(CostEntity cost, BigDecimal amountPerUser, Long userId) {
        Long creatorId = cost.getCreatorId();
        Long groupId = cost.getGroupId();

        CostDocumentEntity costDocumentEntity = new CostDocumentEntity();
        costDocumentEntity.setAmount(amountPerUser);
        costDocumentEntity.setGroupId(groupId);
        costDocumentEntity.setCreditorId(creatorId);
        costDocumentEntity.setDebtorId(userId);
        costDocumentEntity.setCost(cost);
        return costDocumentEntity;
    }

    private CostEventMessage createEventPayloadForCost(CostEntity entity, CostEventMessage.CostOperation costOperation) {
        CostEventMessage message = new CostEventMessage();
        message.setCreatorId(entity.getCreatorId());
        message.setInvolvedUserMobiles(entity.getInvolvedUsers().stream().map(UserEntity::getMobile).collect(Collectors.toList()));
        message.setAmount(entity.getAmount());
        message.setTitle(entity.getTitle());
        message.setCreatedAt(LocalDateTime.now());
        message.setOperation(costOperation);

        return message;
    }

}
