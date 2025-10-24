package com.splitwise.application.models.dtos.group;

import com.splitwise.application.models.entities.group.CostEntity;
import com.splitwise.application.models.entities.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CostResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal amount;
    private UserResponse creator;
    private Long creatorId;
    private List<UserResponse> involvedUsers;


    public CostResponse(CostEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.title = entity.getTitle();
            this.description = entity.getDescription();
            this.amount = entity.getAmount();
            this.creatorId = entity.getCreatorId();

            if (Hibernate.isInitialized(entity.getCreator()) && entity.getCreator() != null) {
                this.creator = new UserResponse(entity.getCreator());
            }

            if (Hibernate.isInitialized(entity.getInvolvedUsers()) && entity.getInvolvedUsers() != null && !entity.getInvolvedUsers().isEmpty()) {
                this.involvedUsers = entity.getInvolvedUsers().stream()
                        .map(UserResponse::new)
                        .toList();
            }
        }
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class UserResponse {
        private Long userId;
        private String name;

        public UserResponse(UserEntity user) {
            if (user != null) {
                this.userId = user.getId();
                this.name = user.getName();
            }
        }
    }

}
