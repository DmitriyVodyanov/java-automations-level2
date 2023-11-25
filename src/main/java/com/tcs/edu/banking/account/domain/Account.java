package com.tcs.edu.banking.account.domain;

public interface Account {
    int getId();
    void setId(int id);
    double getAmount();
    void withdraw(double amount);
    void deposit(double amount);
    AccountType getType();
}
