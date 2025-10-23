package com.splitwise.application.services.group;


import com.splitwise.application.controllers.group.GroupFilter;
import com.splitwise.application.models.dtos.group.CreateGroupRequest;
import com.splitwise.application.models.dtos.group.EditGroupRequest;
import com.splitwise.application.models.dtos.group.GroupInviteRequest;
import com.splitwise.application.models.dtos.group.GroupResponse;
import com.splitwise.application.models.entities.group.GroupEntity;
import com.splitwise.application.models.entities.group.GroupInviteEntity;
import com.splitwise.application.models.entities.user.UserEntity;
import com.splitwise.application.repositories.group.GroupInviteRepository;
import com.splitwise.application.repositories.group.GroupRepository;
import com.splitwise.application.security.JwtUser;
import com.splitwise.application.services.user.UserService;
import com.splitwise.shared.objects.ErrorCodes;
import com.splitwise.shared.objects.StatusCodes;
import com.splitwise.shared.objects.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;
    private final GroupInviteRepository groupInviteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GroupResponse> getAll(GroupFilter filter) {
        return groupRepository.findAll(filter.toSpecification(), filter.toPageable()).stream()
                .map(GroupResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GroupResponse getById(Long id) {
        GroupEntity group = findByIdAndFetchUsersOrThrowException(id);
        checkUserIsMemberOfGroup(group);
        return new GroupResponse(group);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    public GroupResponse update(Long id, EditGroupRequest request) {
        GroupEntity group = findByIdOrThrowException(id);
        checkGroupBelongsToUser(group);

        request.convertToEntity(group);
        GroupEntity entity = groupRepository.save(group);
        return new GroupResponse(entity);
    }

    @Override
    public void inviteToGroup(Long id, GroupInviteRequest request) {

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

        // TODO : send notif
        // TODO : check tests after sending notif
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectInvite(Long groupId) {
        Long userId = JwtUser.getAuthenticatedUser().getId();

        GroupInviteEntity invite = findPendingInvite(userId, groupId);
        invite.setAccepted(false);

        findByIdOrThrowException(groupId);
        groupInviteRepository.save(invite);
        // TODO : send notif
        // TODO : check tests after sending notif
        return false;
    }


    private GroupEntity findByIdAndFetchUsersOrThrowException(Long id) {
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
}
