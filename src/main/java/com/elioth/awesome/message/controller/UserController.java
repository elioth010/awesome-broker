package com.elioth.awesome.message.controller;

import com.elioth.awesome.message.controller.exception.DuplicateUserException;
import com.elioth.awesome.message.controller.request.Target;
import com.elioth.awesome.message.controller.request.UserRequest;
import com.elioth.awesome.message.resource.Message;
import com.elioth.awesome.message.resource.User;
import com.elioth.awesome.message.service.UserService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users", consumes = {APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<User> createUser(@Valid @RequestBody final UserRequest createUserRequest) {
        final User createdUser = userService.createUser(createUserRequest);
        if (null != createdUser.getUsername()) {
            return new ResponseEntity<>(createdUser, CREATED);
        } else {
            throw new DuplicateUserException();
        }
    }

    @GetMapping(value = "/users/{id}/messages", produces = {APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<List<Message>> getMessagesByUser(@Valid @PathVariable("id") final Long userId,
                                                           @RequestParam(name = "target", defaultValue = "SENT") final Target target) {
        final List<Message> messageList = userService.findMessagesByUser(userId, target);
        return ResponseEntity.ok(messageList);
    }
}
