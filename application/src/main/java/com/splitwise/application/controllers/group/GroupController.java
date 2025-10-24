package com.splitwise.application.controllers.group;


import com.splitwise.application.models.dtos.group.*;
import com.splitwise.application.security.JwtUser;
import com.splitwise.application.services.group.GroupService;
import com.splitwise.application.statics.Urls;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${rest.idn}")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService service;

    @GetMapping(Urls.GROUP)
    public ResponseEntity<List<GroupResponse>> getAll(@Valid GroupFilter filter) {
        filter.putUserId(JwtUser.getAuthenticatedUser().getId());
        return new ResponseEntity<>(service.getAll(filter), HttpStatus.OK);
    }

    @GetMapping(Urls.GROUP_ID)
    public ResponseEntity<GroupResponse> getById(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }

    @GetMapping(Urls.GROUP_MEMBERS)
    public ResponseEntity<List<GroupResponse.UserResponse>> getMembers(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(service.getGroupMembers(id), HttpStatus.OK);
    }

    @GetMapping(Urls.GROUP_BALANCE)
    public ResponseEntity<List<BalanceResponse>> getBalance(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(service.getGroupBalance(id), HttpStatus.OK);
    }

    @PostMapping(Urls.GROUP)
    public ResponseEntity<GroupResponse> create(@Valid @RequestBody CreateGroupRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.OK);
    }

    @PutMapping(Urls.GROUP_ID)
    public ResponseEntity<GroupResponse> update(@PathVariable(value = "id") Long id, @Valid @RequestBody EditGroupRequest request) {
        return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);
    }

    @PostMapping(Urls.GROUP_INVITE)
    public ResponseEntity<Boolean> inviteToGroup(@PathVariable(value = "id") Long id, @Valid @RequestBody GroupInviteRequest request) {
        return new ResponseEntity<>(service.inviteToGroup(id, request), HttpStatus.OK);
    }

    @PatchMapping(Urls.GROUP_INVITE_ACCEPT)
    public ResponseEntity<Boolean> acceptInvite(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(service.acceptInvite(id), HttpStatus.OK);
    }

    @PatchMapping(Urls.GROUP_INVITE_REJECT)
    public ResponseEntity<Boolean> rejectInvite(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(service.rejectInvite(id), HttpStatus.OK);
    }

    @DeleteMapping(Urls.GROUP_ID)
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(service.delete(id), HttpStatus.OK);
    }

}
