package com.elioth.awesome.message.controller;

import com.elioth.awesome.message.controller.exception.NoCircularMessageAllowedException;
import com.elioth.awesome.message.controller.request.MessageSendRequest;
import com.elioth.awesome.message.producers.MessageDeliveryProducer;
import com.elioth.awesome.message.resource.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create request to send message", description = "Message delivery system it will accept messages, but it will be processed async for delivery", tags = {"message"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Message delivery request was accepted ", content = @Content),
            @ApiResponse(responseCode = "406", description = "Messages can't be sent to the same user", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validations did not succeed", content = @Content)})
    @PostMapping(value = "/messages/send-message", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendMessage(@Valid @RequestBody final MessageSendRequest sendMessageRequest) {
        if (Objects.equals(sendMessageRequest.getFrom(), sendMessageRequest.getTo())) {
            throw new NoCircularMessageAllowedException();
        }

        messagingProducer.produceMessageDeliveryRequest(sendMessageRequest);
        return ResponseEntity.accepted().build();
    }


}
