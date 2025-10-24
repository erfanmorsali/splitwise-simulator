package com.splitwise.application.services.group;

import com.splitwise.application.controllers.group.GroupFilter;
import com.splitwise.application.models.dtos.group.CreateGroupRequest;
import com.splitwise.application.models.dtos.group.EditGroupRequest;
import com.splitwise.application.models.dtos.group.GroupInviteRequest;
import com.splitwise.application.models.dtos.group.GroupResponse;
import com.splitwise.application.models.entities.group.GroupEntity;

import java.util.List;

public interface GroupService {
    List<GroupResponse> getAll(GroupFilter filter);

    GroupResponse getById(Long id);

    GroupResponse create(CreateGroupRequest request);

    GroupResponse update(Long id, EditGroupRequest request);

    boolean inviteToGroup(Long id, GroupInviteRequest request);

    boolean acceptInvite(Long groupId);

    boolean rejectInvite(Long groupId);

    boolean delete(Long id);

    GroupEntity findByIdAndFetchUsersOrThrowException(Long id);
}
