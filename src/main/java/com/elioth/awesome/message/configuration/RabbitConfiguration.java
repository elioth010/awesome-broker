package com.elioth.awesome.message.configuration;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
public class RabbitConfiguration {

    private String exchangeName = "broker-exchange";

    @Value("${rabbit.host}")
    private String rabbitHost;
    @Value("${rabbit.port:5672}")
    private int rabbitPort;

    @Bean
    public ConnectionFactory connectionFactory () {
        return new CachingConnectionFactory("rabbitHost", rabbitPort);
    }

    @Bean
    public AmqpAdmin amqpAdmin(final ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory){
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public Exchange brokerMessageExchange() {
        return new TopicExchange(exchangeName);
    }
}
