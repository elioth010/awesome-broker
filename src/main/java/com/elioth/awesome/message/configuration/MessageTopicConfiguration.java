package com.elioth.awesome.message.configuration;

import com.elioth.awesome.message.producers.MessageDeliveryProducer;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageTopicConfiguration {

    @Value("${message.exchange.delivery}")
    private String exchangeName;
    @Value("${message.delivery.queue}")
    private String messageQueue;
    @Value("${message.delivery.queue}.dl")
    private String messageQueueDeadLetter;
    @Value("${message.routing.key}")
    private String messageRoutingKey;

    @Bean
    public Exchange brokerMessageExchange(final AmqpAdmin amqpAdmin) {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue messageQueue() {
        return QueueBuilder.durable(messageQueue)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", messageQueueDeadLetter)
                .build();
    }

    @Bean
    public Queue messageQueueDL() {
        return new Queue(messageQueueDeadLetter);
    }

    @Bean
    public Binding messageQueueTopicBinding(final Queue messageQueue, final Exchange exchange) {
        return BindingBuilder.bind(messageQueue).to(exchange).with(messageRoutingKey).noargs();
    }

    @Bean
    public MessageDeliveryProducer messageDeliveryProducer(final RabbitTemplate rabbitTemplate) {
        return new MessageDeliveryProducer(exchangeName, rabbitTemplate);
    }
}
