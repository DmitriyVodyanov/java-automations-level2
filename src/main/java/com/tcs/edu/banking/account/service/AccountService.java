package com.tcs.edu.banking.account.service;

import com.tcs.edu.banking.account.domain.Account;
import com.tcs.edu.banking.account.persist.AccountRepository;

public class AccountService {

    private final AccountRepository accountsRepository;

    public AccountService(AccountRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    /**
     * @param account to create
     * @return id of created account
     */
    public int create(Account account) {
        return accountsRepository.create(account);
    }
    public void transfer(int from, int to, double amount) {
        final Account fromAccount = accountsRepository.findById(from);
        final Account toAccount = accountsRepository.findById(to);

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        accountsRepository.save(fromAccount);
        accountsRepository.save(toAccount);
    }
}
