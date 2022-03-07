package com.elioth.awesome.message.listeners;

import com.elioth.awesome.message.controller.request.MessageSendRequest;
import com.elioth.awesome.message.resource.Message;
import com.elioth.awesome.message.service.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageDeliveryListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDeliveryListener.class);

    private final MessagingService messagingService;

    public MessageDeliveryListener(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @RabbitListener(queues = "${message.delivery.queue}")
    public void processMessageDeliveryRequest(final MessageSendRequest messageSendRequest) {
        try {
            final Message message = messagingService.sendMessage(messageSendRequest);
            LOGGER.info("Message was delivered correctly to_user={}, from_user={}", message.getTo(), message.getFrom());
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException("Error Handler converted exception to fatal", e);
        }
    }
}
