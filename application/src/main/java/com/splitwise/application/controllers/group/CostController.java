package com.splitwise.application.controllers.group;


import com.splitwise.application.models.dtos.group.CostFilter;
import com.splitwise.application.models.dtos.group.CostResponse;
import com.splitwise.application.security.JwtUser;
import com.splitwise.application.services.group.CostService;
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
public class CostController {
    private final CostService service;


    @GetMapping(Urls.COST)
    public ResponseEntity<List<CostResponse>> getAll(@PathVariable(value = "groupId") Long groupId, @Valid CostFilter filter) {
        filter.putUserId(JwtUser.getAuthenticatedUser().getId());
        filter.putGroupId(groupId);
        return new ResponseEntity<>(service.getAll(filter), HttpStatus.OK);
    }

    @GetMapping(Urls.COST_ID)
    public ResponseEntity<CostResponse> getById(@PathVariable(value = "groupId") Long groupId, @PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(service.getById(id, JwtUser.getAuthenticatedUser().getId(), groupId), HttpStatus.OK);
    }
}
