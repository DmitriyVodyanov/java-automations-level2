package com.tcs.edu.banking.account.service;

import com.tcs.edu.banking.account.domain.Account;
import com.tcs.edu.banking.account.persist.AccountRepository;

public class ReportingService {
    private final AccountRepository accountRepository;
    public ReportingService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * @return report according Requirements.
     */
    public String generate() {
        StringBuilder report = new StringBuilder();
        for (Account account : accountRepository.findAll()) {
            report.append(account.getId())
                    .append(": ")
                    .append(account.getType().getAccountType())
                    .append(" ")
                    .append(account.getAmount())
                    .append(System.lineSeparator());
        }
        return report.toString();
    }
}
