package com.splitwise.application.models.dtos.group;

import com.splitwise.application.models.entities.group.GroupInviteEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupInviteRequest {
    @NotNull
    private Long invitedUserId;

    public GroupInviteEntity convertToEntity(GroupInviteEntity entity) {
        if (entity == null) {
            entity = new GroupInviteEntity();
        }

        entity.setInvitedId(this.invitedUserId);
        return entity;
    }
}
