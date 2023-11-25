package com.tcs.edu.banking.account.domain;

public class DepositAccount extends AbstractAccount {
    public DepositAccount() {
        super();
        this.type = AccountType.DEPOSIT;
    }

    /**
     * Переопределяет операцию снятия средств:
     * не позволяет снять более, чем текущее состояние счета.
     * @throws IllegalArgumentException "Not enough amount"
     */
    @Override
    public void withdraw(double amount) {
        if (amount > getAmount()) {
            throw new IllegalArgumentException("Not enough amount");
        }
        super.withdraw(amount);
    }
}
