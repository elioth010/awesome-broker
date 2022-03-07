package com.elioth.awesome.message.producers;

import com.elioth.awesome.message.controller.request.MessageSendRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

public class MessageDeliveryProducer {

    @Value("${message.routing.key}")
    private String messageRoutingKey;
    private final RabbitTemplate rabbitTemplate;
    private final String exchange;

    public MessageDeliveryProducer(final String exchange, final RabbitTemplate rabbitTemplate) {
        this.exchange = exchange;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void produceMessageDeliveryRequest(final MessageSendRequest sendRequest) {
        rabbitTemplate.convertAndSend(exchange, messageRoutingKey, sendRequest);
    }
}
