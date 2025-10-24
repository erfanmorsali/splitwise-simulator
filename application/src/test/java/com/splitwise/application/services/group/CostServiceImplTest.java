package com.splitwise.application.services.group;

import com.splitwise.application.models.dtos.auth.UserContextDto;
import com.splitwise.application.models.dtos.group.CreateCostRequest;
import com.splitwise.application.models.entities.group.CostEntity;
import com.splitwise.application.models.entities.group.GroupEntity;
import com.splitwise.application.models.entities.user.UserEntity;
import com.splitwise.application.repositories.group.CostRepository;
import com.splitwise.application.services.event.EventService;
import com.splitwise.application.services.user.UserService;
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

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CostServiceImplTest {

    @Mock
    private CostRepository costRepository;
    @Mock
    private EventService eventService;
    @Mock
    private UserService userService;
    @Mock
    private GroupService groupService;
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

    @Test
    void create_thereIsSomeInvalidUserIdsInRequest_exception() {
        CreateCostRequest request = createCostRequest();
        when(groupService.findByIdAndFetchUsersOrThrowException(any())).thenReturn(createGroupEntity());
        SystemException ex = assertThrows(SystemException.class, () -> costService.create(request, 1L));
        assertEquals(ErrorCodes.NOT_MEMBER_OF_GROUP.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void create_success() {
        CreateCostRequest request = createCostRequest();
        request.setAmount(new BigDecimal("150000000"));
        GroupEntity group = createGroupEntity();
        UserEntity currentUser = new UserEntity();
        currentUser.setId(currentUserId);
        group.getUsers().add(currentUser);
        when(groupService.findByIdAndFetchUsersOrThrowException(any())).thenReturn(group);
        when(userService.findByIds(any())).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> costService.create(request, 1L));
        verify(costRepository).save(any());
        verify(eventService).createEvent(any(),any());
    }

    @Test
    void delete_costNotFound_exception() {
        when(costRepository.findById(any())).thenReturn(Optional.empty());
        SystemException ex = assertThrows(SystemException.class, () -> costService.delete(1L));
        assertEquals(ErrorCodes.COST_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void delete_userIsNotCreatorOfCost_exception() {
        CostEntity costEntity = createCostEntity();
        costEntity.setCreatorId(currentUserId + 1);
        when(costRepository.findById(any())).thenReturn(Optional.of(costEntity));
        SystemException ex = assertThrows(SystemException.class, () -> costService.delete(1L));
        assertEquals(ErrorCodes.NOT_OWNER_OF_GROUP.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void delete_success() {
        CostEntity costEntity = createCostEntity();
        costEntity.setCreatorId(currentUserId);
        when(costRepository.findById(any())).thenReturn(Optional.of(costEntity));
        assertDoesNotThrow(() -> costService.delete(1L));
        verify(costRepository).save(any());
    }


    private CostEntity createCostEntity() {
        CostEntity costEntity = new CostEntity();
        costEntity.setId(1L);
        return costEntity;
    }

    private CreateCostRequest createCostRequest() {
        CreateCostRequest createCostRequest = new CreateCostRequest();
        createCostRequest.setInvolvedUsers(new HashSet<>(List.of(1L, 2L, 3L)));
        return createCostRequest;
    }

    private GroupEntity createGroupEntity() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setUsers(createUsers(5));
        return groupEntity;
    }

    private Set<UserEntity> createUsers(int count) {
        Set<UserEntity> users = new HashSet<>();
        for (int i = 0; i < count; i++) {
            users.add(createUserEntity((long) i));
        }

        return users;
    }

    private UserEntity createUserEntity(Long id) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        return userEntity;
    }
}