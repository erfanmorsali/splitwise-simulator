package com.splitwise.application.services.group;


import com.splitwise.application.repositories.group.CostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CostServiceImpl {
    private final CostRepository costRepository;
}
