package com.bank;

public class SavingsAccount extends Account {

    private static final double INTEREST_RATE = 0.04;

    public SavingsAccount(String accountNumber,
                          String accountHolder,
                          double balance) {

        super(accountNumber, accountHolder, balance);
    }

    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }
}