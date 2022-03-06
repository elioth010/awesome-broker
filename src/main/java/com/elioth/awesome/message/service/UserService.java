package com.elioth.awesome.message.service;

import com.elioth.awesome.message.controller.request.UserRequest;
import com.elioth.awesome.message.repository.UserRepository;
import com.elioth.awesome.message.repository.entity.UserEntity;
import com.elioth.awesome.message.resource.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(final UserRequest userRequest) {
        final String requestedUsername = userRequest.getUsername();
        final Optional<UserEntity> maybeExistingUser = userRepository.findByUsername(requestedUsername);
        if (maybeExistingUser.isEmpty()) {
            final UserEntity newUser = new UserEntity();
            newUser.setUsername(requestedUsername);
            final UserEntity storedUser = userRepository.save(newUser);
            return new User(storedUser.getId(), storedUser.getUsername(), storedUser.getCreatedAt());
        }
        return null;
    }
}
