package com.tcs.edu.banking.account.domain;

public enum AccountType {
    CREDIT ("CREDIT"),
    DEPOSIT ("DEPOSIT");

    private final String accountType;

    AccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }
}
