package com.elioth.awesome.message.controller;

import com.elioth.awesome.message.controller.request.UserRequest;
import com.elioth.awesome.message.resource.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testStoreNonExistingUserExpectToCreated201() {
        final UserRequest userRequest = new UserRequest();
        userRequest.setUsername("elioth1");

        final ResponseEntity<User> userCreateResponse = this.restTemplate.exchange("http://localhost:" + port + "/users", POST, new HttpEntity<>(userRequest), new ParameterizedTypeReference<>() {
        });
        assertThat(userCreateResponse.getStatusCode()).isEqualTo(CREATED);
        assertThat(userCreateResponse.getBody()).isNotNull();
        assertThat(userCreateResponse.getBody().getUsername()).isEqualTo(userRequest.getUsername());
    }

    @Test
    public void testStoreExistingUserExpectToRejectUserCreatorConflict409() {
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
    public void testStoreNonExistingUserWithInvalidLengthExpectToRejectUserCreatorConflict400() {
        final UserRequest userRequest = new UserRequest();
        userRequest.setUsername("1");

        final ResponseEntity<String> userCreateResponse = this.restTemplate.exchange("http://localhost:" + port + "/users", POST, new HttpEntity<>(userRequest), new ParameterizedTypeReference<>() {
        });
        assertThat(userCreateResponse.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(userCreateResponse.getBody()).contains("username length must be between 4 and 100 characters long");
    }

    @Test
    public void testStoreNonExistingUserWithBadPatternInsideExpectToRejectUserCreatorConflict400() {
        final UserRequest userRequest = new UserRequest();
        userRequest.setUsername("elioth$$$$");

        final ResponseEntity<String> userCreateResponse = this.restTemplate.exchange("http://localhost:" + port + "/users", POST, new HttpEntity<>(userRequest), new ParameterizedTypeReference<>() {
        });
        assertThat(userCreateResponse.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(userCreateResponse.getBody()).contains("username must match just any alphanumeric characters");
    }
}