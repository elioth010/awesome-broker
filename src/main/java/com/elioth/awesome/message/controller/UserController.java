package com.elioth.awesome.message.controller;

import com.elioth.awesome.message.controller.advice.DuplicateUserException;
import com.elioth.awesome.message.controller.request.UserRequest;
import com.elioth.awesome.message.resource.User;
import com.elioth.awesome.message.service.UserService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users", consumes = {APPLICATION_JSON_VALUE})
    public ResponseEntity<User> createUser(@Valid @RequestBody final UserRequest createUserRequest) {
        final User createdUser = userService.createUser(createUserRequest);
        if (null != createdUser) {
            return new ResponseEntity<>(createdUser, CREATED);
        } else {
            throw new DuplicateUserException();
        }
    }
}