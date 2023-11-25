package com.tcs.edu.banking.account.domain;

import java.util.Objects;

/**
 * Инкапсулирует:
 * - id: идентификатор счета
 * - amount: состояние счета
 * - общую для счетов всех типов реализацию: мутабельное свойство id, чтение состояния, снятия и зачисления средств.
 */
public abstract class AbstractAccount implements Account {
    private int id;
    private double amount;
    protected AccountType type;

    public AbstractAccount(int id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    public AbstractAccount() {
        this(0, 0);
    }

    @Override
    public void withdraw(double amount) {
        this.amount -= amount;
    }

    @Override
    public void deposit(double amount) {
        this.amount += amount;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public AccountType getType() {
        return type;
    }

    /**
     * Абстрактный класс AbstractAccount является базовым классом для работы с аккаунтами.
     * Реализует общую функциональность и содержит методы для взаимодействия с аккаунтами.
     * Поэтому класс AbstractAccount был выбран для переопределения методов equals() и hashCode().
     * Метод hashCode() используется для генерации хеш-представления объекта аккаунта.
     * Хеш-представление используется для уникальной идентификации аккаунтов и проверки целостности данных.
     * Метод equals() используется для сравнения двух объектов аккаунтов по их значениям.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractAccount that = (AbstractAccount) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
