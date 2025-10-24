package com.splitwise.application.models.dtos.group;


import com.splitwise.application.models.entities.group.CostEntity;
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
public class CostFilter extends PageableFilter {

    private String title;
    private String description;
    @Hidden
    @Setter(AccessLevel.PRIVATE)
    private Long userId;
    @Hidden
    @Setter(AccessLevel.PRIVATE)
    private Long groupId;

    public Specification<CostEntity> toSpecification() {
        return Specification.<CostEntity>where(null)
                .and(titlePredicate())
                .and(groupPredicate())
                .and(descriptionPredicate())
                .and(userPredicate());
    }

    private Specification<CostEntity> titlePredicate() {
        return ((root, query, cb) -> {
            if (StringUtils.isBlank(title)) {
                return cb.conjunction();
            }
            return cb.like(root.get("title"), "%" + title + "%");
        });
    }

    private Specification<CostEntity> descriptionPredicate() {
        return ((root, query, cb) -> {
            if (StringUtils.isBlank(description)) {
                return cb.conjunction();
            }
            return cb.like(root.get("description"), "%" + description + "%");
        });
    }

    private Specification<CostEntity> groupPredicate() {
        return ((root, query, cb) -> {
            if (groupId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("groupId"), groupId);
        });
    }

    private Specification<CostEntity> userPredicate() {
        return ((root, query, cb) -> {
            if (userId == null) {
                return cb.conjunction();
            }

            query.distinct(true);
            Join<Object, Object> group = root.join("group", JoinType.LEFT);
            Join<Object, Object> users = group.join("users", JoinType.LEFT);

            return cb.or(
                    cb.equal(users.get("id"), userId),
                    cb.equal(root.get("creatorId"), userId)
            );
        });
    }

    public void putUserId(Long userId) {
        this.userId = userId;
    }

    public void putGroupId(Long groupId) {
        this.groupId = groupId;
    }

}
