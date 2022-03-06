package com.elioth.awesome.message.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class User {
    public Long id;
    public String username;
    @JsonFormat(shape= STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSXXX")
    public Date created_at;

    public User(Long id, String username, Instant created_at) {
        this.id = id;
        this.username = username;
        this.created_at = Date.from(created_at);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Date getCreated_at() {
        return created_at;
    }
}
