package com.elioth.awesome.message.controller;

import com.elioth.awesome.message.resource.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagingController {
    @GetMapping("/message")
    @ResponseBody
    public Message getMessage(){
        return new Message("Hello World");
    }
}
