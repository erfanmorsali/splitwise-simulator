package com.splitwise.application.services.group;


import com.splitwise.application.models.dtos.group.CostFilter;
import com.splitwise.application.models.dtos.group.CostResponse;
import com.splitwise.application.models.entities.group.CostEntity;
import com.splitwise.application.repositories.group.CostRepository;
import com.splitwise.shared.objects.ErrorCodes;
import com.splitwise.shared.objects.StatusCodes;
import com.splitwise.shared.objects.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CostServiceImpl implements CostService {
    private final CostRepository costRepository;

    @Override
    public List<CostResponse> getAll(CostFilter filter) {
        return costRepository
                .findAll(filter.toSpecification(), filter.toPageable())
                .stream()
                .map(CostResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public CostResponse getById(Long id, Long userId, Long groupId) {
        CostEntity cost = costRepository.findByIdAndUserIdAndGroupId(id, userId, groupId)
                .orElseThrow(() -> new SystemException(StatusCodes.DATA_NOT_FOUND, ErrorCodes.COST_NOT_FOUND, id));
        return new CostResponse(cost);
    }
}
