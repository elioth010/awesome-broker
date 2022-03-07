package com.elioth.awesome.message.service;

import com.elioth.awesome.message.controller.exception.UserNotFoundException;
import com.elioth.awesome.message.controller.request.MessageSendRequest;
import com.elioth.awesome.message.repository.MessageRepository;
import com.elioth.awesome.message.repository.entity.MessageEntity;
import com.elioth.awesome.message.repository.entity.UserEntity;
import com.elioth.awesome.message.resource.Message;
import com.elioth.awesome.message.resource.User;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {
    private static final String ERROR_TEMPLATE = "User %s not found, unable to deliver the message";

    private final MessageRepository messageRepository;
    private final UserService userService;

    public MessagingService(final MessageRepository messageRepository, final UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    public Message sendMessage(final MessageSendRequest messageSendRequest) {
        final Optional<User> fromUser = userService.findUserByUsername(messageSendRequest.getFrom());
        checkIfUserExists(messageSendRequest.getFrom(), fromUser.isEmpty());

        final Optional<User> toUser = userService.findUserByUsername(messageSendRequest.getTo());
        checkIfUserExists(messageSendRequest.getTo(), toUser.isEmpty());

        final MessageEntity message = prepareMessageToStore(fromUser.get(), toUser.get(), messageSendRequest.getMessage());
        final MessageEntity storedMessage = messageRepository.save(message);
        return new Message(storedMessage.getMessage(), String.valueOf(storedMessage.getFromUser().getId()), String.valueOf(storedMessage.getToUser().getId()));
    }

    private MessageEntity prepareMessageToStore(final User fromUser, final User toUser, final String messageText) {
        final MessageEntity message = new MessageEntity();
        message.setMessage(messageText);
        final UserEntity fromUserEntity = new UserEntity();
        fromUserEntity.setId(fromUser.getId());
        message.setFromUser(fromUserEntity);
        final UserEntity toUserEntity = new UserEntity();
        toUserEntity.setId(toUser.getId());
        message.setToUser(toUserEntity);
        return message;
    }

    private void checkIfUserExists(String username, boolean isUserPresent) {
        if (isUserPresent) {
            throw new UserNotFoundException(String.format(ERROR_TEMPLATE, username));
        }
    }
}
