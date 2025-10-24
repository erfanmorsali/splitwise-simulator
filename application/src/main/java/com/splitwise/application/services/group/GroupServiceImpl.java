package com.splitwise.application.services.group;


import com.splitwise.application.controllers.group.GroupFilter;
import com.splitwise.application.models.dtos.group.*;
import com.splitwise.application.models.entities.group.GroupEntity;
import com.splitwise.application.models.entities.group.GroupInviteEntity;
import com.splitwise.application.models.entities.user.UserEntity;
import com.splitwise.application.repositories.group.GroupInviteRepository;
import com.splitwise.application.repositories.group.GroupRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;
    private final GroupInviteRepository groupInviteRepository;
    private final EventService eventService;


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = Caches.GROUP_LIST, key = "#filter.hashCode()")
    public List<GroupResponse> getAll(GroupFilter filter) {
        return groupRepository.findAll(filter.toSpecification(), filter.toPageable()).stream()
                .map(GroupResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = Caches.GROUP, key = "#id")
    public GroupResponse getById(Long id) {
        GroupEntity group = findByIdAndFetchUsersOrThrowException(id);
        checkUserIsMemberOfGroup(group);
        return new GroupResponse(group);
    }

    @Transactional(readOnly = true)
    public List<BalanceResponse> getGroupBalance(Long id) {
        GroupEntity group = findByIdAndFetchUsersOrThrowException(id);
        return new ArrayList<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = Caches.GROUP, key = "#result.id")
    public GroupResponse create(CreateGroupRequest request) {
        Long userId = JwtUser.getAuthenticatedUser().getId();
        UserEntity user = findUserByIdOrThrowException(userId);

        GroupEntity newEntity = request.convertToEntity(null);
        newEntity.setCreatorId(userId);
        newEntity.getUsers().add(user);

        GroupEntity entity = groupRepository.save(newEntity);
        return new GroupResponse(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = Caches.GROUP, key = "#id")
    public GroupResponse update(Long id, EditGroupRequest request) {
        GroupEntity group = findByIdOrThrowException(id);
        checkGroupBelongsToUser(group);

        request.convertToEntity(group);
        GroupEntity entity = groupRepository.save(group);
        return new GroupResponse(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {Caches.GROUP, Caches.GROUP_LIST}, allEntries = true)
    public boolean delete(Long id) {
        GroupEntity group = findByIdOrThrowException(id);
        checkGroupBelongsToUser(group);
        group.setDeleted(LocalDateTime.now());
        groupRepository.save(group);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean inviteToGroup(Long id, GroupInviteRequest request) {
        GroupEntity group = findByIdOrThrowException(id);
        checkGroupBelongsToUser(group);

        Long invitedUserId = request.getInvitedUserId();
        findUserByIdOrThrowException(invitedUserId);

        checkInviteExistsOrInvitedUserIsMemberOfGroup(invitedUserId, group.getId());

        GroupInviteEntity newInvite = request.convertToEntity(new GroupInviteEntity());
        newInvite.setGroupId(id);
        newInvite.setInviterId(JwtUser.getAuthenticatedUser().getId());

        groupInviteRepository.save(newInvite);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean acceptInvite(Long groupId) {
        Long userId = JwtUser.getAuthenticatedUser().getId();

        GroupInviteEntity invite = findPendingInvite(userId, groupId);
        invite.setAccepted(true);

        GroupEntity group = findByIdAndFetchUsersOrThrowException(groupId);
        UserEntity user = findUserByIdOrThrowException(userId);
        group.getUsers().add(user);

        groupInviteRepository.save(invite);
        groupRepository.save(group);
        eventService.createEvent(
                createEventPayloadForInvite(groupId, userId, group.getCreator(), GroupInviteEventMessage.GroupInviteOperation.ACCEPT),
                Topics.GROUP_INVITE
        );
        // TODO : check tests after sending notif
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectInvite(Long groupId) {
        Long userId = JwtUser.getAuthenticatedUser().getId();

        GroupInviteEntity invite = findPendingInvite(userId, groupId);
        invite.setAccepted(false);

        GroupEntity group = findByIdAndFetchUsersOrThrowException(groupId);
        groupInviteRepository.save(invite);
        eventService.createEvent(
                createEventPayloadForInvite(groupId, userId, group.getCreator(), GroupInviteEventMessage.GroupInviteOperation.REJECT),
                Topics.GROUP_INVITE
        );
        // TODO : check tests after sending notif
        return false;
    }


    public GroupEntity findByIdAndFetchUsersOrThrowException(Long id) {
        return findGroupByIdAndFetchUsers(id)
                .orElseThrow(() -> new SystemException(StatusCodes.DATA_NOT_FOUND, ErrorCodes.GROUP_NOT_FOUND, id));
    }


    private Optional<GroupEntity> findGroupByIdAndFetchUsers(Long id) {
        return groupRepository.findGroupByIdAndFetchUsers(id);
    }


    private GroupEntity findByIdOrThrowException(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new SystemException(StatusCodes.DATA_NOT_FOUND, ErrorCodes.GROUP_NOT_FOUND, id));
    }

    private void checkUserIsMemberOfGroup(GroupEntity group) {
        Long userId = JwtUser.getAuthenticatedUser().getId();
        if (group.getUsers().stream()
                .noneMatch(user -> user.getId().equals(userId))) {
            throw new SystemException(StatusCodes.ACCESS_DENIED, ErrorCodes.NOT_MEMBER_OF_GROUP, "user is not member of this group");
        }
    }

    private UserEntity findUserByIdOrThrowException(Long userId) {
        return userService.findById(userId)
                .orElseThrow(() -> new SystemException(StatusCodes.DATA_NOT_FOUND, ErrorCodes.USER_NOT_FOUND, userId));
    }

    private void checkGroupBelongsToUser(GroupEntity group) {
        Long userId = JwtUser.getAuthenticatedUser().getId();

        if (!userId.equals(group.getCreatorId())) {
            throw new SystemException(StatusCodes.ACCESS_DENIED, ErrorCodes.NOT_OWNER_OF_GROUP, group.getId());
        }
    }

    private GroupInviteEntity findPendingInvite(Long userId, Long groupId) {
        GroupInviteEntity invite = groupInviteRepository.findFirstByInvitedIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new SystemException(StatusCodes.DATA_NOT_FOUND, ErrorCodes.GROUP_INVITE_NOT_FOUND, "group Invite not found"));

        if (invite.getAccepted() != null) {
            throw new SystemException(StatusCodes.BAD_REQUEST, ErrorCodes.GROUP_INVITE_NOT_CHANGEABLE, "Cannot change status of this invite request");
        }
        return invite;
    }

    private void checkInviteExistsOrInvitedUserIsMemberOfGroup(Long userId, Long groupId) {
        Optional<GroupInviteEntity> existingInvite = groupInviteRepository.findFirstByInvitedIdAndGroupId(userId, groupId);

        if (existingInvite.isPresent()) {
            GroupInviteEntity invite = existingInvite.get();

            if (invite.getAccepted() == null) {
                throw new SystemException(StatusCodes.BAD_REQUEST, ErrorCodes.USER_ALREADY_INVITED_TO_GROUP, "user already has invite for this group");
            }
            if (invite.getAccepted()) {
                throw new SystemException(StatusCodes.BAD_REQUEST, ErrorCodes.USER_ALREADY_MEMBER_OF_GROUP, "user already in this group");
            }
        }
    }

    private GroupInviteEventMessage createEventPayloadForInvite(Long groupId, Long userId, UserEntity creator, GroupInviteEventMessage.GroupInviteOperation operation) {
        GroupInviteEventMessage groupInviteMessage = new GroupInviteEventMessage();
        groupInviteMessage.setGroupId(groupId);
        groupInviteMessage.setUserId(userId);
        groupInviteMessage.setOperation(operation);
        groupInviteMessage.setGroupOwner(creator.getMobile());

        return groupInviteMessage;
    }
}
