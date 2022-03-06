package com.elioth.awesome.message.controller.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserRequest {

    private final String username;

    @JsonCreator
    public UserRequest(@JsonProperty("username") @NotBlank @NotNull final String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
