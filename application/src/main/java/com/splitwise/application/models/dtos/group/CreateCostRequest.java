package com.splitwise.application.models.dtos.group;

import com.splitwise.application.models.entities.group.CostEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCostRequest {
    @NotNull
    private BigDecimal amount;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    @NotEmpty
    private Set<Long> involvedUsers;

    public CostEntity convertToEntity(CostEntity entity) {
        if (entity == null) {
            entity = new CostEntity();
            entity.setInvolvedUsers(new HashSet<>());
        }

        entity.setAmount(this.amount);
        entity.setTitle(this.title);
        entity.setDescription(this.description);
        return entity;
    }

}
