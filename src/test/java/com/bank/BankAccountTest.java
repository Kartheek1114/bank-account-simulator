package com.bank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    // =========================
    // Group 1: Account creation
    // =========================

    @Test
    void testSavingsAccountCreation() {

        SavingsAccount account =
                new SavingsAccount("ACC1001", "Kartheek", 5000);

        assertEquals("ACC1001", account.getAccountNumber());
        assertEquals("Kartheek", account.getAccountHolder());
        assertEquals(5000, account.getBalance());
    }

    @Test
    void testCurrentAccountCreation() {

        CurrentAccount account =
                new CurrentAccount("ACC1002", "Rahul", 3000);

        assertEquals("ACC1002", account.getAccountNumber());
        assertEquals("Rahul", account.getAccountHolder());
        assertEquals(3000, account.getBalance());
    }

    @Test
    void testFixedDepositCreation() {

        FixedDepositAccount account =
                new FixedDepositAccount("ACC1003", "Akhil", 10000);

        assertEquals("ACC1003", account.getAccountNumber());
        assertEquals("Akhil", account.getAccountHolder());
        assertEquals(10000, account.getBalance());
    }

    // =========================
    // Group 2: Deposits
    // =========================

    @Test
    void testValidDeposit() {

        SavingsAccount account =
                new SavingsAccount("ACC1004", "Test", 1000);

        account.deposit(500);

        assertEquals(1500, account.getBalance());
    }

    @Test
    void testZeroDepositThrows() {

        SavingsAccount account =
                new SavingsAccount("ACC1005", "Test", 1000);

        assertThrows(
                IllegalArgumentException.class,
                () -> account.deposit(0)
        );
    }

    @Test
    void testNegativeDepositThrows() {

        SavingsAccount account =
                new SavingsAccount("ACC1006", "Test", 1000);

        assertThrows(
                IllegalArgumentException.class,
                () -> account.deposit(-100)
        );
    }

    // =========================
    // Group 3: Withdrawals
    // =========================

    @Test
    void testValidWithdrawal() {

        SavingsAccount account =
                new SavingsAccount("ACC1007", "Test", 2000);

        account.withdraw(500);

        assertEquals(1500, account.getBalance());
    }

    @Test
    void testWithdrawExceedBalanceThrows() {

        SavingsAccount account =
                new SavingsAccount("ACC1008", "Test", 1000);

        assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(2000)
        );
    }

    @Test
    void testCurrentAccountOverdraft() {

        CurrentAccount account =
                new CurrentAccount("ACC1009", "Test", 1000);

        account.withdraw(1500);

        assertEquals(-500, account.getBalance());
    }

    @Test
    void testSavingsNoOverdraft() {

        SavingsAccount account =
                new SavingsAccount("ACC1010", "Test", 1000);

        assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(1500)
        );
    }

    // =========================
    // Group 4: Transfers
    // =========================

    @Test
    void testValidTransfer() {

        SavingsAccount sender =
                new SavingsAccount("ACC1011", "A", 5000);

        SavingsAccount receiver =
                new SavingsAccount("ACC1012", "B", 1000);

        sender.transfer(receiver, 2000);

        assertEquals(3000, sender.getBalance());
        assertEquals(3000, receiver.getBalance());
    }

    @Test
    void testTransferInsufficientFunds() {

        SavingsAccount sender =
                new SavingsAccount("ACC1013", "A", 1000);

        SavingsAccount receiver =
                new SavingsAccount("ACC1014", "B", 1000);

        assertThrows(
                IllegalArgumentException.class,
                () -> sender.transfer(receiver, 5000)
        );
    }

    @Test
    void testTransferUpdatesHistory() {

        SavingsAccount sender =
                new SavingsAccount("ACC1015", "A", 5000);

        SavingsAccount receiver =
                new SavingsAccount("ACC1016", "B", 1000);

        sender.transfer(receiver, 1000);

        assertTrue(sender.getTransactions().size() > 0);
    }

    // =========================
    // Group 5: Interest calculation
    // =========================

    @Test
    void testSavingsInterestRate() {

        SavingsAccount account =
                new SavingsAccount("ACC1017", "Test", 10000);

        double interest = account.calculateInterest();

        assertEquals(400, interest);
    }

    @Test
    void testFixedDepositInterestRate() {

        FixedDepositAccount account =
                new FixedDepositAccount("ACC1018", "Test", 10000);

        double interest = account.calculateInterest();

        assertEquals(800, interest);
    }

    @Test
    void testCurrentNoInterest() {

        CurrentAccount account =
                new CurrentAccount("ACC1019", "Test", 10000);

        double interest = account.calculateInterest();

        assertEquals(0, interest);
    }

    // =========================
    // Group 6: Transaction history
    // =========================

    @Test
    void testTransactionHistoryRecorded() {

        SavingsAccount account =
                new SavingsAccount("ACC1020", "Test", 1000);

        account.deposit(500);

        assertEquals(1, account.getTransactions().size());
    }

    @Test
    void testTransactionTimestamp() {

        SavingsAccount account =
                new SavingsAccount("ACC1021", "Test", 1000);

        account.deposit(500);

        assertNotNull(
                account.getTransactions().get(0).getTimestamp()
        );
    }

    @Test
    void testTransactionDescription() {

        SavingsAccount account =
                new SavingsAccount("ACC1022", "Test", 1000);

        account.deposit(500);

        assertEquals(
                "Amount deposited",
                account.getTransactions().get(0).getDescription()
        );
    }

    // =========================
    // Group 7: Edge cases
    // =========================

    @Test
    void testZeroInitialDeposit() {

        SavingsAccount account =
                new SavingsAccount("ACC1023", "Test", 0);

        assertEquals(0, account.getBalance());
    }

    @Test
    void testMultipleTransactions() {

        SavingsAccount account =
                new SavingsAccount("ACC1024", "Test", 1000);

        account.deposit(500);
        account.withdraw(200);
        account.deposit(100);

        assertEquals(3, account.getTransactions().size());
    }

    @Test
    void testBalanceAfterMultipleOps() {

        SavingsAccount account =
                new SavingsAccount("ACC1025", "Test", 1000);

        account.deposit(1000);
        account.withdraw(500);
        account.deposit(200);

        assertEquals(1700, account.getBalance());
    }
}