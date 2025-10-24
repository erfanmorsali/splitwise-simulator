package com.splitwise.application.models.dtos.group;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CostEventMessage {
    private Long creatorId;
    private List<String> involvedUserMobiles;
    private String title;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private CostOperation operation;

    public enum CostOperation {
        CREATED, UPDATED
    }

}
