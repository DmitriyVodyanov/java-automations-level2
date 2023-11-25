package com.tcs.edu.banking.transport.domain;

public abstract class FormatMessage implements Message {
    private final String body;
    private final Severity severity;

    public FormatMessage(String body, Severity severity) {
        this.body = body;
        this.severity = severity;
    }

    @Override
    public String toString() {
        return getDecorator() + " %s".formatted(getBody());
    }

    protected abstract String getDecorator();

    public Severity getSeverity() {
        return severity;
    }

    public String getBody() {
        return body;
    }
}
