package com.elioth.awesome.message.controller.request;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRequest {

    @Size(min = 4, max = 100, message = "username length must be between 4 and 100 characters long")
    @Pattern(regexp = "^(?=[a-zA-Z0-9-_]+$)(?=.*[_-]*)[^_-].*[^_-]$", message = "username must match just any alphanumeric characters allowing \"a-zA-Z0-0_-\"")
    private String username;

    public UserRequest() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
