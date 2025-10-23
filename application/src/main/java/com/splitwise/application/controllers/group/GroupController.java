package com.splitwise.application.controllers.group;


import com.splitwise.application.models.dtos.group.CreateGroupRequest;
import com.splitwise.application.models.dtos.group.EditGroupRequest;
import com.splitwise.application.models.dtos.group.GroupResponse;
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

    @PostMapping(Urls.GROUP)
    public ResponseEntity<GroupResponse> create(@Valid @RequestBody CreateGroupRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.OK);
    }

    @PutMapping(Urls.GROUP_ID)
    public ResponseEntity<GroupResponse> update(@PathVariable(value = "id") Long id, @Valid @RequestBody EditGroupRequest request) {
        return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);
    }

    @PatchMapping(Urls.GROUP_INVITE_ACCEPT)
    public void acceptInvite(@PathVariable(value = "id") Long id) {
        service.acceptInvite(id);
    }

    @PatchMapping(Urls.GROUP_INVITE_REJECT)
    public void rejectInvite(@PathVariable(value = "id") Long id) {
        service.rejectInvite(id);
    }

}
