package com.splitwise.shared.brokers.models;


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
