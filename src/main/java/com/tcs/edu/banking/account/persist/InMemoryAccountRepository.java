package com.tcs.edu.banking.account.persist;

import com.tcs.edu.banking.account.domain.Account;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Простейшая in-memory реализация.
 * @see java.util.Map
 * @see java.util.HashMap
 * @see java.util.Map#put
 * @see java.util.Map#get
 * @see java.util.Map#values
 */
public class InMemoryAccountRepository implements AccountRepository {
    private int idSequence;
    private final Map<Integer, Account> accountStorage = new HashMap<>();
    @Override
    public int create(Account account) {
        int id = ++idSequence;
        account.setId(id);
        save(account);
        return id;
    }
    @Override
    public Account findById(int fromId) {
        return accountStorage.get(fromId);
    }
    @Override
    public Collection<Account> findAll() {
        return accountStorage.values();
    }
    @Override
    public void save(Account account) {
        accountStorage.put(account.getId(), account);
    }
}
