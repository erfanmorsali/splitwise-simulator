package com.splitwise.application.models.group;


import com.splitwise.application.models.entities.group.GroupEntity;
import com.splitwise.application.models.entities.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponse {

    private Long id;
    private String name;
    private String description;
    private Long creatorId;
    private UserResponse creator;
    private Set<UserResponse> members = new HashSet<>();

    public GroupResponse(GroupEntity groupEntity) {
        if (groupEntity != null) {
            this.id = groupEntity.getId();
            this.name = groupEntity.getName();
            this.description = groupEntity.getDescription();
            this.creatorId = groupEntity.getCreatorId();

            if (Hibernate.isInitialized(groupEntity.getCreator()) && groupEntity.getCreator() != null) {
                this.creator = new UserResponse(groupEntity.getCreator());
            }

            if (Hibernate.isInitialized(groupEntity.getUsers()) && !groupEntity.getUsers().isEmpty()) {
                members = groupEntity.getUsers().stream()
                        .map(UserResponse::new)
                        .collect(Collectors.toSet());
            }
        }

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class UserResponse {
        private Long id;
        private String name;

        public UserResponse(UserEntity user) {
            if (user != null) {
                this.id = user.getId();
                this.name = user.getName();
            }
        }
    }
}
