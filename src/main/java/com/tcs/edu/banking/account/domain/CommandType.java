package com.tcs.edu.banking.account.domain;

public enum CommandType {
    CREATE_ACCOUNT ("CREATE ACCOUNT"),
    REPORT ("REPORT"),
    TRANSFER ("TRANSFER");

    private final String command;

    CommandType(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "Commands{" +
                "Commands='" + command + '\'' +
                '}';
    }
}
