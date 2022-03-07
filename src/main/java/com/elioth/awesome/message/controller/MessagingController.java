package com.elioth.awesome.message.controller;

import com.elioth.awesome.message.controller.exception.NoCircularMessageAllowedException;
import com.elioth.awesome.message.controller.request.MessageSendRequest;
import com.elioth.awesome.message.producers.MessageDeliveryProducer;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class MessagingController {

    private final MessageDeliveryProducer messagingProducer;

    public MessagingController(final MessageDeliveryProducer messagingProducer) {
        this.messagingProducer = messagingProducer;
    }

    @PostMapping(value = "/messages/send-message", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> sendMessage(@Valid @RequestBody final MessageSendRequest sendMessageRequest) {
        if (Objects.equals(sendMessageRequest.getFrom(), sendMessageRequest.getTo())) {
            throw new NoCircularMessageAllowedException();
        }

        messagingProducer.produceMessageDeliveryRequest(sendMessageRequest);
        return ResponseEntity.accepted().build();
    }


}
