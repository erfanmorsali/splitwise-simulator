package com.splitwise.application.services.group;


import com.splitwise.application.models.dtos.group.CostFilter;
import com.splitwise.application.models.dtos.group.CostResponse;
import com.splitwise.application.repositories.group.CostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CostServiceImpl implements CostService {
    private final CostRepository costRepository;

    @Override
    public List<CostResponse> getAll(CostFilter filter) {
        return List.of();
    }
}
