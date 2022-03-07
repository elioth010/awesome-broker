package com.elioth.awesome.message.controller.request;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MessageSendRequest implements Serializable {

    @NotBlank
    private String from;
    @NotBlank
    private String to;
    @Size(min = 1, max = 1000, message = "Message must be from 1 up to 1000")
    private String message;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
