package com.tcs.edu.banking.account.persist;

import com.tcs.edu.banking.account.domain.Account;

import java.util.Collection;

public interface AccountRepository {

    int create(Account account);
    Account findById(int fromId);
    Collection<Account> findAll();
    void save(Account account);
}
