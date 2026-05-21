package com.bank;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {

    protected String accountNumber;
    protected String accountHolder;
    protected double balance;

    protected List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber, String accountHolder, double balance) {

        if (balance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    public void deposit(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit must be greater than zero");
        }

        balance += amount;

        transactions.add(
                new Transaction("DEPOSIT", amount, "Amount deposited")
        );
    }

    public void withdraw(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid withdrawal");
        }

        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        balance -= amount;

        transactions.add(
                new Transaction("WITHDRAW", amount, "Amount withdrawn")
        );
    }

    public void transfer(Account target, double amount) {

        withdraw(amount);

        target.deposit(amount);

        transactions.add(
                new Transaction("TRANSFER", amount,
                        "Transferred to " + target.accountNumber)
        );
    }

    public abstract double calculateInterest();

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }
   public String getAccountHolder() {
    return accountHolder;
}

    public List<Transaction> getTransactions() {
        return transactions;
    }
}