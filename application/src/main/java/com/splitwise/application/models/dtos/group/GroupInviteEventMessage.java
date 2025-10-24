package com.splitwise.application.models.dtos.group;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupInviteEventMessage {
    private Long groupId;
    private Long userId;
    private String groupOwner;
    private GroupInviteOperation operation;


    public enum GroupInviteOperation {
        ACCEPT, REJECT
    }

}
