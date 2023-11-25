package com.tcs.edu.banking.transport.domain;

public class RoundFormatMessage extends FormatMessage {

    public RoundFormatMessage(String body, Severity severity) {
        super(body, severity);
    }

    protected String getDecorator() {
        return "(%s)".formatted(getSeverity().toString());
    }
}
