package com.splitwise.application.controllers.group;


import com.splitwise.application.models.group.GroupResponse;
import com.splitwise.application.security.JwtUser;
import com.splitwise.application.services.group.GroupService;
import com.splitwise.application.statics.Urls;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
