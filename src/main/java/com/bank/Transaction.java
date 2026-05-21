package com.bank;

import java.time.LocalDateTime;

public class Transaction {

    private String type;
    private double amount;
    private String description;
    private LocalDateTime timestamp;

    public Transaction(String type, double amount, String description) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return timestamp + " | " + type + " | " + amount + " | " + description;
    }
}