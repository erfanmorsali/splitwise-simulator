package com.splitwise.application.controllers.group;


import com.splitwise.application.models.entities.group.GroupEntity;
import com.splitwise.shared.objects.PageableFilter;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupFilter extends PageableFilter {
    private String name;
    private String description;
    @Hidden
    @Setter(AccessLevel.PRIVATE)
    private Long userId;

    public void putUserId(Long userId) {
        this.userId = userId;
    }


    public Specification<GroupEntity> toSpecification() {
        return Specification.<GroupEntity>where(null)
                .and(namePredicate())
                .and(descriptionPredicate())
                .and(userPredicate());
    }

    private Specification<GroupEntity> namePredicate() {
        return ((root, query, cb) -> {
            if (StringUtils.isBlank(name)) {
                return cb.conjunction();
            }
            return cb.like(root.get("name"), "%" + name + "%");
        });
    }

    private Specification<GroupEntity> descriptionPredicate() {
        return ((root, query, cb) -> {
            if (StringUtils.isBlank(description)) {
                return cb.conjunction();
            }
            return cb.like(root.get("description"), "%" + description + "%");
        });
    }

    private Specification<GroupEntity> userPredicate() {
        return ((root, query, cb) -> {
            if (userId == null) {
                return cb.conjunction();
            }

            Join<Object, Object> users = root.join("users", JoinType.LEFT);

            return cb.or(
                    cb.equal(users.get("id"), userId),
                    cb.equal(root.get("creatorId"), userId)
            );
        });
    }
}
