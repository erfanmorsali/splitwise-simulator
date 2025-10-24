package com.splitwise.application.services.group;

import com.splitwise.application.models.dtos.auth.UserContextDto;
import com.splitwise.application.models.dtos.group.CreateGroupRequest;
import com.splitwise.application.models.dtos.group.EditGroupRequest;
import com.splitwise.application.models.dtos.group.GroupInviteRequest;
import com.splitwise.application.models.entities.group.GroupEntity;
import com.splitwise.application.models.entities.group.GroupInviteEntity;
import com.splitwise.application.models.entities.user.UserEntity;
import com.splitwise.application.repositories.group.GroupInviteRepository;
import com.splitwise.application.repositories.group.GroupRepository;
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
    @Mock
    private EventService eventService;
    @Mock
    private GroupInviteRepository groupInviteRepository;

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
    void getById_success() {
        Set<UserEntity> members = new HashSet<>(List.of(createUser(currentUserId)));
        GroupEntity group = createGroup(1L, currentUserId, members);
        when(groupRepository.findGroupByIdAndFetchUsers(1L)).thenReturn(Optional.of(group));

        assertDoesNotThrow(() -> groupService.getById(1L));
    }

    @Test
   void getGroupBalance_groupNotFound_exception() {
        when(groupRepository.findGroupByIdAndFetchUsers(1L)).thenReturn(Optional.empty());
        SystemException ex = assertThrows(SystemException.class, () -> groupService.getGroupBalance(1L));
        assertEquals(ErrorCodes.GROUP_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
   }

    @Test
    void getGroupBalance_userIsNotMemberOfGroup_exception() {
        GroupEntity group = createGroup(1L, currentUserId + 1, new HashSet<>());
        when(groupRepository.findGroupByIdAndFetchUsers(any())).thenReturn(Optional.of(group));
        SystemException ex = assertThrows(SystemException.class, () -> groupService.getGroupBalance(1L));
        assertEquals(ErrorCodes.NOT_MEMBER_OF_GROUP.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void create_userNotFound_exception() {
        when(userService.findById(currentUserId)).thenReturn(Optional.empty());
        CreateGroupRequest request = createGroupRequest();
        SystemException ex = assertThrows(SystemException.class, () -> groupService.create(request));
        assertEquals(ErrorCodes.USER_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void create_success() {
        when(userService.findById(currentUserId)).thenReturn(Optional.of(createUser(currentUserId)));
        CreateGroupRequest request = createGroupRequest();
        assertDoesNotThrow(() -> groupService.create(request));
        verify(groupRepository).save(any());
    }

    @Test
    void update_groupNotFound_exception() {
        when(groupRepository.findById(any())).thenReturn(Optional.empty());
        EditGroupRequest request = new EditGroupRequest();
        SystemException ex = assertThrows(SystemException.class, () -> groupService.update(1L, request));
        assertEquals(ErrorCodes.GROUP_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void update_userIsNotOwnerOfGroup_exception() {
        GroupEntity group = createGroup(1L, currentUserId + 1, new HashSet<>());
        when(groupRepository.findById(any())).thenReturn(Optional.of(group));
        EditGroupRequest request = new EditGroupRequest();
        SystemException ex = assertThrows(SystemException.class, () -> groupService.update(1L, request));
        assertEquals(ErrorCodes.NOT_OWNER_OF_GROUP.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void update_success() {
        GroupEntity group = createGroup(1L, currentUserId, new HashSet<>());
        when(groupRepository.findById(any())).thenReturn(Optional.of(group));
        EditGroupRequest request = new EditGroupRequest();
        assertDoesNotThrow(() -> groupService.update(1L, request));
        verify(groupRepository).save(any());
    }

    @Test
    void delete_groupNotFound_exception() {
        when(groupRepository.findById(any())).thenReturn(Optional.empty());
        SystemException ex = assertThrows(SystemException.class, () -> groupService.delete(1L));
        assertEquals(ErrorCodes.GROUP_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void delete_userIsNotOwnerOfGroup_exception() {
        GroupEntity group = createGroup(1L, currentUserId + 1, new HashSet<>());
        when(groupRepository.findById(any())).thenReturn(Optional.of(group));
        SystemException ex = assertThrows(SystemException.class, () -> groupService.delete(1L));
        assertEquals(ErrorCodes.NOT_OWNER_OF_GROUP.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void delete_success() {
        GroupEntity group = createGroup(1L, currentUserId, new HashSet<>());
        when(groupRepository.findById(any())).thenReturn(Optional.of(group));
        assertDoesNotThrow(() -> groupService.delete(1L));
        verify(groupRepository).save(any());
    }

    @Test
    void inviteToGroup_groupNotFound_exception() {
        when(groupRepository.findById(any())).thenReturn(Optional.empty());
        GroupInviteRequest request = new GroupInviteRequest();
        SystemException ex = assertThrows(SystemException.class, () -> groupService.inviteToGroup(1L, request));
        assertEquals(ErrorCodes.GROUP_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void inviteToGroup_groupNotBelongToUser_exception() {
        when(groupRepository.findById(any())).thenReturn(Optional.of(createGroup(1L, currentUserId + 1, new HashSet<>())));
        GroupInviteRequest request = new GroupInviteRequest();
        SystemException ex = assertThrows(SystemException.class, () -> groupService.inviteToGroup(1L, request));
        assertEquals(ErrorCodes.NOT_OWNER_OF_GROUP.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void inviteToGroup_userNotFound_exception() {
        when(groupRepository.findById(any())).thenReturn(Optional.of(createGroup(1L, currentUserId, new HashSet<>())));
        when(userService.findById(any())).thenReturn(Optional.empty());
        GroupInviteRequest request = new GroupInviteRequest();
        SystemException ex = assertThrows(SystemException.class, () -> groupService.inviteToGroup(1L, request));
        assertEquals(ErrorCodes.USER_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void inviteToGroup_inviteFoundButNotAcceptedOrRejected_exception() {
        GroupInviteEntity invite = createGroupInvite();
        invite.setAccepted(null);
        when(groupRepository.findById(any())).thenReturn(Optional.of(createGroup(1L, currentUserId, new HashSet<>())));
        when(userService.findById(any())).thenReturn(Optional.of(createUser(currentUserId)));
        when(groupInviteRepository.findFirstByInvitedIdAndGroupId(any(), any())).thenReturn(Optional.of(invite));
        GroupInviteRequest request = new GroupInviteRequest();
        SystemException ex = assertThrows(SystemException.class, () -> groupService.inviteToGroup(1L, request));
        assertEquals(ErrorCodes.USER_ALREADY_INVITED_TO_GROUP.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void inviteToGroup_inviteFoundAndAcceptedAlready_exception() {
        GroupInviteEntity invite = createGroupInvite();
        invite.setAccepted(true);
        when(groupRepository.findById(any())).thenReturn(Optional.of(createGroup(1L, currentUserId, new HashSet<>())));
        when(userService.findById(any())).thenReturn(Optional.of(createUser(currentUserId)));
        when(groupInviteRepository.findFirstByInvitedIdAndGroupId(any(), any())).thenReturn(Optional.of(invite));
        GroupInviteRequest request = new GroupInviteRequest();
        SystemException ex = assertThrows(SystemException.class, () -> groupService.inviteToGroup(1L, request));
        assertEquals(ErrorCodes.USER_ALREADY_MEMBER_OF_GROUP.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void inviteToGroup_success() {
        when(groupRepository.findById(any())).thenReturn(Optional.of(createGroup(1L, currentUserId, new HashSet<>())));
        when(userService.findById(any())).thenReturn(Optional.of(createUser(currentUserId)));
        when(groupInviteRepository.findFirstByInvitedIdAndGroupId(any(), any())).thenReturn(Optional.empty());
        GroupInviteRequest request = new GroupInviteRequest();
        assertDoesNotThrow(() -> groupService.inviteToGroup(1L, request));
        verify(groupInviteRepository).save(any());
    }

    @Test
    void acceptInvite_inviteNotFound_exception() {
        when(groupInviteRepository.findFirstByInvitedIdAndGroupId(any(), any())).thenReturn(Optional.empty());
        SystemException ex = assertThrows(SystemException.class, () -> groupService.acceptInvite(1L));
        assertEquals(ErrorCodes.GROUP_INVITE_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void acceptInvite_inviteIsAcceptedOrRejectedAlready_exception() {
        GroupInviteEntity invite = createGroupInvite();
        when(groupInviteRepository.findFirstByInvitedIdAndGroupId(any(), any())).thenReturn(Optional.of(invite));
        SystemException ex = assertThrows(SystemException.class, () -> groupService.acceptInvite(1L));
        assertEquals(ErrorCodes.GROUP_INVITE_NOT_CHANGEABLE.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void acceptInvite_groupNotFound_exception() {
        GroupInviteEntity invite = createGroupInvite();
        invite.setAccepted(null);
        when(groupInviteRepository.findFirstByInvitedIdAndGroupId(any(), any())).thenReturn(Optional.of(invite));
        when(groupRepository.findGroupByIdAndFetchUsers(any())).thenReturn(Optional.empty());
        SystemException ex = assertThrows(SystemException.class, () -> groupService.acceptInvite(1L));
        assertEquals(ErrorCodes.GROUP_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void acceptInvite_userNotFound_exception() {
        GroupInviteEntity invite = createGroupInvite();
        invite.setAccepted(null);
        when(groupInviteRepository.findFirstByInvitedIdAndGroupId(any(), any())).thenReturn(Optional.of(invite));
        when(groupRepository.findGroupByIdAndFetchUsers(any())).thenReturn(Optional.of(createGroup(1L, currentUserId, new HashSet<>())));
        when(userService.findById(any())).thenReturn(Optional.empty());
        SystemException ex = assertThrows(SystemException.class, () -> groupService.acceptInvite(1L));
        assertEquals(ErrorCodes.USER_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void acceptInvite_success() {
        GroupInviteEntity invite = createGroupInvite();
        invite.setAccepted(null);
        when(groupInviteRepository.findFirstByInvitedIdAndGroupId(any(), any())).thenReturn(Optional.of(invite));
        when(groupRepository.findGroupByIdAndFetchUsers(any())).thenReturn(Optional.of(createGroup(1L, currentUserId, new HashSet<>())));
        when(userService.findById(any())).thenReturn(Optional.of(createUser(currentUserId)));
        assertDoesNotThrow(() -> groupService.acceptInvite(1L));
        verify(groupRepository).save(any());
        verify(groupInviteRepository).save(any());
        verify(eventService).createEvent(any(), any());
    }

    @Test
    void rejectInvite_inviteNotFound_exception() {
        when(groupInviteRepository.findFirstByInvitedIdAndGroupId(any(), any())).thenReturn(Optional.empty());
        SystemException ex = assertThrows(SystemException.class, () -> groupService.rejectInvite(1L));
        assertEquals(ErrorCodes.GROUP_INVITE_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void rejectInvite_inviteIsAcceptedOrRejectedAlready_exception() {
        GroupInviteEntity invite = createGroupInvite();
        when(groupInviteRepository.findFirstByInvitedIdAndGroupId(any(), any())).thenReturn(Optional.of(invite));
        SystemException ex = assertThrows(SystemException.class, () -> groupService.rejectInvite(1L));
        assertEquals(ErrorCodes.GROUP_INVITE_NOT_CHANGEABLE.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void rejectInvite_groupNotFound_exception() {
        GroupInviteEntity invite = createGroupInvite();
        invite.setAccepted(null);
        when(groupInviteRepository.findFirstByInvitedIdAndGroupId(any(), any())).thenReturn(Optional.of(invite));
        when(groupRepository.findById(any())).thenReturn(Optional.empty());
        SystemException ex = assertThrows(SystemException.class, () -> groupService.rejectInvite(1L));
        assertEquals(ErrorCodes.GROUP_NOT_FOUND.getCode(), ex.getErrorCode().getCode());
    }

    @Test
    void rejectInvite_success() {
        GroupInviteEntity invite = createGroupInvite();
        invite.setAccepted(null);
        when(groupInviteRepository.findFirstByInvitedIdAndGroupId(any(), any())).thenReturn(Optional.of(invite));
        when(groupRepository.findGroupByIdAndFetchUsers(any())).thenReturn(Optional.of(createGroup(1L, currentUserId, new HashSet<>())));
        assertDoesNotThrow(() -> groupService.rejectInvite(1L));
        verify(groupInviteRepository).save(any());
        verify(eventService).createEvent(any(), any());
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

    private GroupInviteEntity createGroupInvite() {
        GroupInviteEntity invite = new GroupInviteEntity();
        invite.setAccepted(true);
        return invite;
    }
}