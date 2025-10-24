package com.splitwise.application.services.group;

import com.splitwise.application.models.dtos.group.CostFilter;
import com.splitwise.application.models.dtos.group.CostResponse;
import com.splitwise.application.models.dtos.group.CreateCostRequest;

import java.util.List;

public interface CostService {
    List<CostResponse> getAll(CostFilter filter);

    CostResponse getById(Long id, Long userId, Long groupId);

    CostResponse create(CreateCostRequest request, Long groupId);

}
