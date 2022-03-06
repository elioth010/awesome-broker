package com.elioth.awesome.message.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.Instant;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User {
    public Long id;
    public String username;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSXXX")
    public Date createdAt;

    public User(@JsonProperty("id") Long id, @JsonProperty("name") String username, @JsonProperty("created_at") Date createdAt) {
        this.id = id;
        this.username = username;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
