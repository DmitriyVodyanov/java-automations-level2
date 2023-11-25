package com.tcs.edu.banking.account.domain;

public class CreditAccount extends AbstractAccount {
    public CreditAccount() {
        super();
        this.type = AccountType.CREDIT;
    }
}
