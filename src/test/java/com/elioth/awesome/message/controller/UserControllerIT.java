package com.elioth.awesome.message.controller;

import com.elioth.awesome.message.controller.request.MessageSendRequest;
import com.elioth.awesome.message.controller.request.UserRequest;
import com.elioth.awesome.message.resource.Message;
import com.elioth.awesome.message.resource.User;
import com.elioth.awesome.message.service.MessagingService;
import com.elioth.awesome.message.service.UserService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private MessagingService messagingService;

    private Long insertedUserId = 1L;

    @BeforeEach
    public void setupData() {
        final UserRequest userRequest = new UserRequest();
        final String user1 = "elioth10";
        final String user2 = "elioth20";
        userRequest.setUsername(user1);
        final User storedUser = userService.createUser(userRequest);
        userRequest.setUsername(user2);
        userService.createUser(userRequest);
        final MessageSendRequest messageSendRequest = new MessageSendRequest();
        messageSendRequest.setMessage("Hello 1");
        messageSendRequest.setFrom(user1);
        messageSendRequest.setTo(user2);
        messagingService.sendMessage(messageSendRequest);
        messageSendRequest.setMessage("Hello 2");
        messagingService.sendMessage(messageSendRequest);
        messageSendRequest.setFrom(user2);
        messageSendRequest.setTo(user1);
        messagingService.sendMessage(messageSendRequest);
        insertedUserId = storedUser.getId();
    }

    @Test
    void testStoreNonExistingUserExpectToCreated201() {
        final UserRequest userRequest = new UserRequest();
        userRequest.setUsername("elioth1");

        final ResponseEntity<User> userCreateResponse = this.restTemplate.exchange("http://localhost:" + port + "/users", POST, new HttpEntity<>(userRequest), new ParameterizedTypeReference<>() {
        });
        assertThat(userCreateResponse.getStatusCode()).isEqualTo(CREATED);
        assertThat(userCreateResponse.getBody()).isNotNull();
        assertThat(userCreateResponse.getBody().getUsername()).isEqualTo(userRequest.getUsername());
    }

    @Test
    void testStoreExistingUserExpectToRejectUserCreatorConflict409() {
        final UserRequest userRequest = new UserRequest();
        userRequest.setUsername("elioth");

        final ResponseEntity<User> userCreateResponse = this.restTemplate.exchange("http://localhost:" + port + "/users", POST, new HttpEntity<>(userRequest), new ParameterizedTypeReference<>() {
        });
        assertThat(userCreateResponse.getStatusCode()).isEqualTo(CREATED);
        assertThat(userCreateResponse.getBody()).isNotNull();
        assertThat(userCreateResponse.getBody().getUsername()).isEqualTo(userRequest.getUsername());

        final ResponseEntity<String> userCreateResponseDuplicated = this.restTemplate.exchange("http://localhost:" + port + "/users", POST, new HttpEntity<>(userRequest), new ParameterizedTypeReference<>() {
        });

        assertThat(userCreateResponseDuplicated.getStatusCode()).isEqualTo(CONFLICT);
        assertThat(userCreateResponseDuplicated.getBody()).contains("User already exists");
    }

    @Test
    void testStoreNonExistingUserWithInvalidLengthExpectToRejectUserCreatorConflict400() {
        final UserRequest userRequest = new UserRequest();
        userRequest.setUsername("11");

        final ResponseEntity<String> userCreateResponse = this.restTemplate.exchange("http://localhost:" + port + "/users", POST, new HttpEntity<>(userRequest), new ParameterizedTypeReference<>() {
        });
        assertThat(userCreateResponse.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(userCreateResponse.getBody()).contains("username length must be between 4 and 100 characters long");
    }

    @Test
    void testStoreNonExistingUserWithBadPatternInsideExpectToRejectUserCreatorConflict400() {
        final UserRequest userRequest = new UserRequest();
        userRequest.setUsername("elioth$$$$");

        final ResponseEntity<String> userCreateResponse = this.restTemplate.exchange("http://localhost:" + port + "/users", POST, new HttpEntity<>(userRequest), new ParameterizedTypeReference<>() {
        });
        assertThat(userCreateResponse.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(userCreateResponse.getBody()).contains("username must match just any alphanumeric characters");
    }

    @Test
    void testGetMessageSentByUserExpectToReturn200() {
        final ResponseEntity<List<Message>> sendMessageResponse = this.restTemplate.exchange("http://localhost:" + port + "/users/" + insertedUserId + "/messages?target=SENT", GET, null, new ParameterizedTypeReference<>() {
        });
        assertThat(sendMessageResponse.getStatusCode()).isEqualTo(OK);
        assertThat(sendMessageResponse.getBody()).hasSize(2);
    }

    @Test
    void testGetMessageReceivedByUserExpectToReturn200() {
        final ResponseEntity<List<Message>> sendMessageResponse = this.restTemplate.exchange("http://localhost:" + port + "/users/" + insertedUserId + "/messages?target=RECEIVED", GET, null, new ParameterizedTypeReference<>() {
        });
        assertThat(sendMessageResponse.getStatusCode()).isEqualTo(OK);
        assertThat(sendMessageResponse.getBody()).hasSize(1);
    }
}