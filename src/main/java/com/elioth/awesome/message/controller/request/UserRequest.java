package com.elioth.awesome.message.controller.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRequest {

    private final String username;

    @JsonCreator
    public UserRequest(@JsonProperty("username") final String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
