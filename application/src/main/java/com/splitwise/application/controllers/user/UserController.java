package com.splitwise.application.controllers.user;

import com.splitwise.application.models.dtos.user.UserFilter;
import com.splitwise.application.models.dtos.user.UserResponse;
import com.splitwise.application.services.user.UserService;
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
public class UserController {
    private final UserService service;

    @GetMapping(Urls.USER)
    public ResponseEntity<List<UserResponse>> getAll( @Valid UserFilter filter) {
        return new ResponseEntity<>(service.getAll(filter), HttpStatus.OK);
    }

    @GetMapping(Urls.USER_ID)
    public ResponseEntity<UserResponse> getById(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }
}
