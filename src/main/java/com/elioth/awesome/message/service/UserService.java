package com.elioth.awesome.message.service;

import com.elioth.awesome.message.controller.request.Target;
import com.elioth.awesome.message.controller.request.UserRequest;
import com.elioth.awesome.message.repository.MessageRepository;
import com.elioth.awesome.message.repository.UserRepository;
import com.elioth.awesome.message.repository.entity.MessageEntity;
import com.elioth.awesome.message.repository.entity.UserEntity;
import com.elioth.awesome.message.resource.Message;
import com.elioth.awesome.message.resource.User;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.elioth.awesome.message.controller.request.Target.SENT;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public UserService(final UserRepository userRepository, final MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public User createUser(final UserRequest userRequest) {
        final String requestedUsername = userRequest.getUsername();
        final Optional<UserEntity> maybeExistingUser = userRepository.findByUsername(requestedUsername);
        if (maybeExistingUser.isEmpty()) {
            final UserEntity newUser = new UserEntity();
            newUser.setUsername(requestedUsername);
            final UserEntity storedUser = userRepository.save(newUser);
            final Date createdAt = storedUser.getCreatedAt() != null ? Date.from(storedUser.getCreatedAt()) : new Date();
            return new User(storedUser.getId(), storedUser.getUsername(), createdAt);
        }
        return new User(maybeExistingUser.get().getId(), null, null);
    }

    public Optional<User> findUserByUsername(final String username) {
        final Optional<UserEntity> maybeExistingUser = userRepository.findByUsername(username);
        if(maybeExistingUser.isEmpty()) {
            return Optional.empty();
        }
        final UserEntity foundUser = maybeExistingUser.get();
        final Date createdAt = foundUser.getCreatedAt() != null ? Date.from(foundUser.getCreatedAt()) : new Date();
        return Optional.of(new User(foundUser.getId(), foundUser.getUsername(), createdAt));
    }

    public List<Message> findMessagesByUser(final Long userId, final Target target) {
        if(SENT == target) {
            final List<MessageEntity> messagesSentByUser = messageRepository.findAllMessagesSentByUser(userId);
            return messagesSentByUser.stream().map(message -> new Message(message.getMessage(), message.getFromUser().getUsername(), message.getToUser().getUsername())).collect(Collectors.toList());
        }
        final List<MessageEntity> messagesSentByUser = messageRepository.findAllMessagesReceivedByUser(userId);
        return messagesSentByUser.stream().map(message -> new Message(message.getMessage(), message.getFromUser().getUsername(), message.getToUser().getUsername())).collect(Collectors.toList());
    }
}
