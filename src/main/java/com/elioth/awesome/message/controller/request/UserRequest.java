package com.elioth.awesome.message.controller.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRequest {

    private final String username;

    @JsonCreator
    public UserRequest(@JsonProperty("username")
                           @NotBlank @NotNull
                           @Size(min = 2, max = 100, message = "Name must be between 2 and 32 characters long")
    @Pattern(regexp = "[a-zA-Z0-9_-]")
    final String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
