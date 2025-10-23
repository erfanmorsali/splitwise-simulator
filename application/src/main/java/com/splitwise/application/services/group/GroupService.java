package com.splitwise.application.services.group;

import com.splitwise.application.controllers.group.GroupFilter;
import com.splitwise.application.models.group.GroupResponse;

import java.util.List;

public interface GroupService {
    List<GroupResponse> getAll(GroupFilter filter);
}
