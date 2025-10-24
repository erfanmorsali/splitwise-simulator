package com.splitwise.application.models.dtos.user;

import com.splitwise.application.models.entities.user.UserEntity;
import com.splitwise.shared.objects.PageableFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFilter extends PageableFilter {
    private String name;
    private String mobile;

    public Specification<UserEntity> toSpecification() {
        return Specification.<UserEntity>where(null)
                .and(namePredicate())
                .and(mobilePredicate());
    }

    private Specification<UserEntity> namePredicate() {
        return ((root, query, cb) -> {
            if (StringUtils.isBlank(name)) {
                return cb.conjunction();
            }
            return cb.like(root.get("name"), "%" + name + "%");
        });
    }

    private Specification<UserEntity> mobilePredicate() {
        return ((root, query, cb) -> {
            if (StringUtils.isBlank(mobile)) {
                return cb.conjunction();
            }
            return cb.like(root.get("mobile"), "%" + mobile + "%");
        });
    }
}
