package com.elioth.awesome.message.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User {
    public final Long id;
    public final String username;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSXXX")
    public final Date createdAt;

    public User(@JsonProperty("id") final Long id, @JsonProperty("name") final String username, @JsonProperty("created_at") final Date createdAt) {
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
