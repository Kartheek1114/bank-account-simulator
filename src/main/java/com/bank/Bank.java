package com.bank;

import java.util.HashMap;
import java.util.Map;

public class Bank {

    private Map<String, Account> accounts = new HashMap<>();

    private int accountCounter = 1000;

    public String generateAccountNumber() {
        return "ACC" + (++accountCounter);
    }

    public Account createSavingsAccount(String name, double amount) {

        String accNo = generateAccountNumber();

        Account account =
                new SavingsAccount(accNo, name, amount);

        accounts.put(accNo, account);

        return account;
    }

    public Account createCurrentAccount(String name, double amount) {

        String accNo = generateAccountNumber();

        Account account =
                new CurrentAccount(accNo, name, amount);

        accounts.put(accNo, account);

        return account;
    }

    public Account createFixedDepositAccount(String name, double amount) {

        String accNo = generateAccountNumber();

        Account account =
                new FixedDepositAccount(accNo, name, amount);

        accounts.put(accNo, account);

        return account;
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void applyInterestToAllAccounts() {

        for (Account account : accounts.values()) {

            double interest = account.calculateInterest();

            if (interest > 0) {
                account.deposit(interest);
            }
        }
    }
}