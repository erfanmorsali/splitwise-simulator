package com.splitwise.application.services.group;

import com.splitwise.application.models.dtos.auth.UserContextDto;
import com.splitwise.application.models.dtos.group.CreateGroupRequest;
import com.splitwise.application.models.entities.group.GroupEntity;
import com.splitwise.application.models.entities.user.UserEntity;
import com.splitwise.application.repositories.group.GroupRepository;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private GroupServiceImpl groupService;


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
    void getById_invalidId_exception() {
        when(groupRepository.findGroupByIdAndFetchUsers(1L)).thenReturn(Optional.empty());
        SystemException ex = assertThrows(SystemException.class, () -> groupService.getById(1L));
        assertEquals(ErrorCodes.GROUP_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void getById_youAreNotMemberOfGroup_exception() {
        GroupEntity group = createGroup(1L, currentUserId, new HashSet<>());
        when(groupRepository.findGroupByIdAndFetchUsers(1L)).thenReturn(Optional.of(group));
        SystemException ex = assertThrows(SystemException.class, () -> groupService.getById(1L));
        assertEquals(ErrorCodes.NOT_MEMBER_OF_GROUP.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void getById_youAreMemberOfGroup_success() {
        Set<UserEntity> members = new HashSet<>(List.of(createUser(currentUserId)));
        GroupEntity group = createGroup(1L, currentUserId, members);
        when(groupRepository.findGroupByIdAndFetchUsers(1L)).thenReturn(Optional.of(group));

        assertDoesNotThrow(() -> groupService.getById(1L));
    }

    @Test
    void create_userNotFound_exception() {
        when(userService.findById(currentUserId)).thenReturn(Optional.empty());
        CreateGroupRequest request = createGroupRequest();
        SystemException ex = assertThrows(SystemException.class, () -> groupService.create(request));
        assertEquals(ErrorCodes.USER_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void create_userFound_success() {
        when(userService.findById(currentUserId)).thenReturn(Optional.of(createUser(currentUserId)));
        CreateGroupRequest request = createGroupRequest();
        assertDoesNotThrow(() -> groupService.create(request));
        verify(groupRepository).save(any());

    }


    private UserEntity createUser(Long id) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setName("Username : " + id);
        user.setMobile("mobile : " + id);
        return user;
    }

    private GroupEntity createGroup(Long id, Long creatorId, Set<UserEntity> members) {
        GroupEntity group = new GroupEntity();
        group.setId(id);
        group.setName("test");
        group.setDescription("test description");
        group.setCreatorId(creatorId);
        group.setUsers(members);
        UserEntity creator = createUser(creatorId);
        group.setCreator(creator);
        return group;
    }

    private CreateGroupRequest createGroupRequest() {
        CreateGroupRequest request = new CreateGroupRequest();
        request.setName("test");
        request.setDescription("test description");
        return request;
    }
}