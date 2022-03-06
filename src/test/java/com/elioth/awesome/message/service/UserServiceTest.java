package com.elioth.awesome.message.service;

import com.elioth.awesome.message.controller.request.UserRequest;
import com.elioth.awesome.message.repository.UserRepository;
import com.elioth.awesome.message.repository.entity.UserEntity;
import com.elioth.awesome.message.resource.User;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final Random RANDOM_ID_GENERATOR = new Random(10);
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUserWithNoExistingUser() {
        final UserRequest userRequest = new UserRequest();
        userRequest.setUsername("test");
        final UserEntity user = new UserEntity();
        user.setUsername(userRequest.getUsername());
        when(userRepository.save(eq(user))).thenReturn(getStoredUser(user));
        final User newUser = userService.createUser(userRequest);
        assertThat(newUser).isNotNull();
        assertThat(newUser.getUsername()).isEqualTo(userRequest.getUsername());
        assertThat(newUser.getCreatedAt()).isNotNull();
        assertThat(newUser.getId()).isNotNull();
    }

    @Test
    public void testCreateUserWithExistingUserName() {
        final UserRequest userRequest = new UserRequest();
        userRequest.setUsername("test");
        final UserEntity user = new UserEntity();
        user.setUsername(userRequest.getUsername());
        when(userRepository.findByUsername(eq(userRequest.getUsername()))).thenReturn(Optional.of(getStoredUser(user)));
        final User newUser = userService.createUser(userRequest);
        assertThat(newUser).isNull();
    }

    private UserEntity getStoredUser(UserEntity user) {
        final UserEntity storedUser =  new UserEntity();
        storedUser.setUsername(user.getUsername());
        storedUser.setCreatedAt(Instant.now());
        storedUser.setId(RANDOM_ID_GENERATOR.nextLong());
        return storedUser;
    }
}