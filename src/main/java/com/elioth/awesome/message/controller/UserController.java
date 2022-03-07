package com.elioth.awesome.message.controller;

import com.elioth.awesome.message.controller.exception.DuplicateUserException;
import com.elioth.awesome.message.controller.request.Target;
import com.elioth.awesome.message.controller.request.UserRequest;
import com.elioth.awesome.message.resource.Message;
import com.elioth.awesome.message.resource.User;
import com.elioth.awesome.message.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create users", description = "Sign up user into message broker system", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User create operation successful", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "409", description = "Duplicated username within the system"),
            @ApiResponse(responseCode = "400", description = "Validation did not succeed with username", content = @Content)})
    @PostMapping(value = "/users", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@Valid @RequestBody final UserRequest createUserRequest) {
        final User createdUser = userService.createUser(createUserRequest);
        if (null != createdUser.getUsername()) {
            return new ResponseEntity<>(createdUser, CREATED);
        } else {
            throw new DuplicateUserException();
        }
    }

    @Operation(summary = "Get all messages for a particular user", description = "List all the messages sent or received for the given user", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request succeed", content = @Content(array = @ArraySchema( schema = @Schema(implementation = Message.class)))),
            @ApiResponse(responseCode = "400", description = "Validation did not succeed with username", content = @Content)})
    @GetMapping(value = "/users/{id}/messages", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Message>> getMessagesByUser(@Valid @PathVariable("id") final Long userId, @RequestParam(name = "target", defaultValue = "SENT") final Target target) {
        final List<Message> messageList = userService.findMessagesByUser(userId, target);
        return ResponseEntity.ok(messageList);
    }
}
