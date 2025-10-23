package com.splitwise.application.services.group;


import com.splitwise.application.controllers.group.GroupFilter;
import com.splitwise.application.models.group.GroupResponse;
import com.splitwise.application.repositories.group.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GroupResponse> getAll(GroupFilter filter) {
        return groupRepository.findAll(filter.toSpecification(), filter.toPageable()).stream()
                .map(GroupResponse::new)
                .toList();
    }
}
