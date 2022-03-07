package com.elioth.awesome.message.controller;

import com.elioth.awesome.message.controller.request.MessageSendRequest;
import com.elioth.awesome.message.controller.request.UserRequest;
import com.elioth.awesome.message.service.MessagingService;
import com.elioth.awesome.message.service.UserService;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.test.context.SpringRabbitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@SpringRabbitTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MessagingControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private MessagingService messagingService;
    @Autowired
    private ConnectionFactory connectionFactory;

    @BeforeEach
    public void initUserData() {
        final UserRequest userRequest = new UserRequest();
        final String user1 = "elioth1";
        final String user2 = "elioth2";
        userRequest.setUsername(user1);
        userService.createUser(userRequest);
        userRequest.setUsername(user2);
        userService.createUser(userRequest);
        final MessageSendRequest messageSendRequest = new MessageSendRequest();
        messageSendRequest.setMessage("Hello 1");
        messageSendRequest.setFrom(user1);
        messageSendRequest.setTo(user2);
        messagingService.sendMessage(messageSendRequest);
        messageSendRequest.setMessage("Hello 2");
        messagingService.sendMessage(messageSendRequest);
    }

    @Test
    void testSentMessageRequestExpectReturnAccepted202() {
        final MessageSendRequest sendMessageRequest = new MessageSendRequest();
        sendMessageRequest.setMessage("Hello World");
        sendMessageRequest.setFrom("elioth1");
        sendMessageRequest.setTo("elioth2");
        final ResponseEntity<String> sendMessageResponse = this.restTemplate.exchange("http://localhost:" + port + "/messages/send-message", POST, new HttpEntity<>(sendMessageRequest), String.class);
        assertThat(sendMessageResponse.getStatusCode()).isEqualTo(ACCEPTED);
    }

    @Test
    void testSendMessageToSameUserExpectReturnNotAcceptable406() {
        final MessageSendRequest sendMessageRequest = new MessageSendRequest();
        sendMessageRequest.setMessage("Hello World");
        sendMessageRequest.setFrom("elioth1");
        sendMessageRequest.setTo("elioth1");
        final ResponseEntity<String> sendMessageResponse = this.restTemplate.exchange("http://localhost:" + port + "/messages/send-message", POST, new HttpEntity<>(sendMessageRequest), String.class);
        assertThat(sendMessageResponse.getStatusCode()).isEqualTo(NOT_ACCEPTABLE);
        assertThat(sendMessageResponse.getBody()).contains("Users are not allowed to send messages to themselves");
    }

    @TestConfiguration
    static class RabbitTestConfiguration {

        @Bean
        @Primary
        public ConnectionFactory connectionFactory() throws IOException {
            ConnectionFactory factory = mock(ConnectionFactory.class);
            Connection connection = mock(Connection.class);
            Channel channel = mock(Channel.class);
            when(factory.createConnection()).thenReturn(connection);
            when(connection.createChannel(anyBoolean())).thenReturn(channel);
            given(channel.isOpen()).willReturn(true);
            return factory;
        }
    }
}