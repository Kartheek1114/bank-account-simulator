package com.bank;

public class CurrentAccount extends Account {

    private static final double OVERDRAFT_LIMIT = 1000;

    public CurrentAccount(String accountNumber,
                          String accountHolder,
                          double balance) {

        super(accountNumber, accountHolder, balance);
    }

    @Override
    public void withdraw(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid withdrawal");
        }

        if ((balance - amount) < -OVERDRAFT_LIMIT) {
            throw new IllegalArgumentException("Overdraft exceeded");
        }

        balance -= amount;

        transactions.add(
                new Transaction("WITHDRAW", amount,
                        "Current account withdrawal")
        );
    }

    @Override
    public double calculateInterest() {
        return 0;
    }
}