package com.bank;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Bank bank = new Bank();

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== BANK MENU =====");
            System.out.println("1. Create Savings Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Show Balance");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();

            switch (choice) {

                case 1:

                    scanner.nextLine();

                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter Initial Deposit: ");
                    double amount = scanner.nextDouble();

                    Account acc =
                            bank.createSavingsAccount(name, amount);

                    System.out.println(
                            "Account Created: "
                                    + acc.getAccountNumber()
                    );

                    break;

                case 2:

                    scanner.nextLine();

                    System.out.print("Enter Account Number: ");
                    String accNo = scanner.nextLine();

                    Account account = bank.getAccount(accNo);

                    if (account == null) {
                        System.out.println("Account not found");
                        break;
                    }

                    System.out.print("Enter Deposit Amount: ");
                    double dep = scanner.nextDouble();

                    account.deposit(dep);

                    System.out.println("Deposit Successful");

                    break;

                case 3:

                    scanner.nextLine();

                    System.out.print("Enter Account Number: ");
                    String wAcc = scanner.nextLine();

                    Account wAccount = bank.getAccount(wAcc);

                    if (wAccount == null) {
                        System.out.println("Account not found");
                        break;
                    }

                    System.out.print("Enter Withdrawal Amount: ");
                    double withdraw = scanner.nextDouble();

                    wAccount.withdraw(withdraw);

                    System.out.println("Withdrawal Successful");

                    break;

                case 4:

                    scanner.nextLine();

                    System.out.print("Enter Account Number: ");
                    String bAcc = scanner.nextLine();

                    Account bAccount = bank.getAccount(bAcc);

                    if (bAccount == null) {
                        System.out.println("Account not found");
                        break;
                    }

                    System.out.println(
                            "Balance: " + bAccount.getBalance()
                    );

                    break;

                case 5:

                    System.out.println("Exiting...");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
}