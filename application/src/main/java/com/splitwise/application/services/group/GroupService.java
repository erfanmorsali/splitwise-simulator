package com.splitwise.application.services.group;

import com.splitwise.application.models.dtos.group.*;
import com.splitwise.application.models.entities.group.GroupEntity;

import java.util.List;

public interface GroupService {
    List<GroupResponse> getAll(GroupFilter filter);

    GroupResponse getById(Long id);

    List<GroupResponse.UserResponse> getGroupMembers(Long id);


    GroupResponse create(CreateGroupRequest request);

    GroupResponse update(Long id, EditGroupRequest request);

    List<BalanceResponse> getGroupBalance(Long id);

    boolean inviteToGroup(Long id, GroupInviteRequest request);

    boolean acceptInvite(Long groupId);

    boolean rejectInvite(Long groupId);

    boolean delete(Long id);

    GroupEntity findByIdAndFetchUsersOrThrowException(Long id);
}
