package com.splitwise.application.services.group;

import com.splitwise.application.models.dtos.auth.UserContextDto;
import com.splitwise.application.models.entities.group.CostEntity;
import com.splitwise.application.repositories.group.CostRepository;
import com.splitwise.shared.objects.ErrorCodes;
import com.splitwise.shared.objects.SystemException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CostServiceImplTest {

    @Mock
    private CostRepository costRepository;
    @InjectMocks
    private CostServiceImpl costService;


    private final Long currentUserId = 1000L;


    @BeforeEach
    void setUp() {
        UserContextDto principal = new UserContextDto(currentUserId);
        var auth = new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getById_invalidCostOrGroupId() {
        when(costRepository.findByIdAndUserIdAndGroupId(1L, 1L, 1L)).thenReturn(Optional.empty());
        SystemException ex = assertThrows(SystemException.class, () -> costService.getById(1L, 1L, 1L));
        assertEquals(ErrorCodes.COST_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void getById_success() {
        when(costRepository.findByIdAndUserIdAndGroupId(1L, 1L, 1L)).thenReturn(Optional.of(createCostEntity()));
        assertDoesNotThrow(() -> costService.getById(1L, 1L, 1L));
    }



    private CostEntity createCostEntity() {
        CostEntity costEntity = new CostEntity();
        costEntity.setId(1L);
        return costEntity;
    }
}