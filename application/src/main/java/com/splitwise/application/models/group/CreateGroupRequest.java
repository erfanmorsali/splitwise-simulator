package com.splitwise.application.models.group;


import com.splitwise.application.models.entities.group.GroupEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
@AllArgsConstructor
public class CreateGroupRequest {
    @NotNull
    @Size(min = 1, max = 50)
    private String name;
    @NotNull
    @Size(min = 1, max = 100)
    private String description;


    public GroupEntity convertToEntity(GroupEntity entity) {
        if (entity == null) {
            entity = new GroupEntity();
            entity.setUsers(new HashSet<>());
        }

        entity.setName(name);
        entity.setDescription(description);
        return entity;
    }
}
