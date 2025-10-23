package com.splitwise.application.controllers.group;


import com.splitwise.shared.objects.PageableFilter;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

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
}
