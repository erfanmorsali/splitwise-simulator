package com.splitwise.application.services.group;

import com.splitwise.application.controllers.group.GroupFilter;
import com.splitwise.application.models.group.CreateGroupRequest;
import com.splitwise.application.models.group.GroupResponse;

import java.util.List;

public interface GroupService {
    List<GroupResponse> getAll(GroupFilter filter);
    GroupResponse getById(Long id);
    GroupResponse create(CreateGroupRequest request);
}
