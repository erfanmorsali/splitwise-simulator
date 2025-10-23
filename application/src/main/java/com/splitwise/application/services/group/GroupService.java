package com.splitwise.application.services.group;

import com.splitwise.application.controllers.group.GroupFilter;
import com.splitwise.application.models.dtos.group.CreateGroupRequest;
import com.splitwise.application.models.dtos.group.EditGroupRequest;
import com.splitwise.application.models.dtos.group.GroupResponse;

import java.util.List;

public interface GroupService {
    List<GroupResponse> getAll(GroupFilter filter);

    GroupResponse getById(Long id);

    GroupResponse create(CreateGroupRequest request);

    GroupResponse update(Long id, EditGroupRequest request);

    boolean acceptInvite(Long groupId);

    boolean rejectInvite(Long groupId);
}
