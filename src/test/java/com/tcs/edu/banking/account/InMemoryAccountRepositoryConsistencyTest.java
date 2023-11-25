package com.tcs.edu.banking.account;

import com.tcs.edu.banking.account.domain.CreditAccount;
import com.tcs.edu.banking.account.persist.InMemoryAccountRepository;
import org.junit.jupiter.api.Test;

import static java.util.Objects.isNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class InMemoryAccountRepositoryConsistencyTest {

    /**
     * В настоящий момент, неверно распределена ответственность по созданию аккаунтов,
     * создание аккаунтов должно быть делегировано AbstractAccount, т.к. все манипуляции с аккаунтами происходят
     * в этом классе. Так же необходимо инкапсулировать метод setId(), что бы ни кто не мог изменять аккаунт. Лучше
     * вообще отказаться от этого метода, и хранить каунтер аккаунтов и при создании нового аккаунта инкрементировать
     * его автоматически.
     */

    @Test
    public void shouldNotAccountDoublingWhenMutateId() {
        final var accountRepo = new InMemoryAccountRepository();
        final var account = new CreditAccount();
        account.setId(1);
        accountRepo.save(account);
        assumeFalse(isNull(accountRepo.findById(1)));
        assumeTrue(accountRepo.findAll().size() == 1);

        account.setId(2);
        accountRepo.save(account);

        assertTrue(accountRepo.findAll().size() == 1);

    }
}
