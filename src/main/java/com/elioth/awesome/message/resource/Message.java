package com.elioth.awesome.message.resource;

public class Message {

    private final String text;
    private final String from;
    private final String to;

    public Message(final String text, final String from, final String to) {
        this.text = text;
        this.from = from;
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
