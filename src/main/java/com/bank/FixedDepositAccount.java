	package com.bank;

public class FixedDepositAccount extends Account {

    private static final double INTEREST_RATE = 0.08;

    public FixedDepositAccount(String accountNumber,
                               String accountHolder,
                               double balance) {

        super(accountNumber, accountHolder, balance);
    }

    @Override
    public void withdraw(double amount) {
        throw new UnsupportedOperationException(
                "Cannot withdraw from fixed deposit account"
        );
    }

    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }
}