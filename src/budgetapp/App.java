package budgetapp;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.YearMonth;
import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;

public class App {

    ArrayList<BudgetAccount> accounts = new ArrayList<>();

    public static void main(String[] args) {

        App budgetApp = new App();
        budgetApp.run();
    }

    public void run() {
        Scanner scnr = new Scanner(System.in);
        boolean running = true;
        loadAllAccounts();
        while (running) {
            System.out.println();
            System.out.println("Please select from the following options: ");
            System.out.println("1. Create new budget account");
            System.out.println("2. Update existing budget account");
            System.out.println("3. View existing budget accounts");
            System.out.println("4. Exit");
            String choice = scnr.nextLine();
            try {
                int numberChoice = Integer.parseInt(choice);
                switch (numberChoice) {
                    case 1:
                        try {
                            System.out.println("\nEnter account name: ");
                            String accountName = scnr.nextLine();
                            System.out.println("Enter year: ");
                            int year = Integer.parseInt(scnr.nextLine());
                            System.out.println("Enter month (1-12): ");
                            int monthNumber = Integer.parseInt(scnr.nextLine());
                            YearMonth month = YearMonth.of(year, monthNumber);
                            if (findAccount(accountName, month) != null) {
                                System.out.println(
                                        "An account with that name and month already exists. Please try again.");
                                break;
                            }
                            System.out.println("Enter spending limit: ");
                            double spendingLimit = Double.parseDouble(scnr.nextLine());
                            System.out.println("Rollover from previous month? (y/n): ");
                            String rolloverChoice = scnr.nextLine();
                            YearMonth priorMonth = month.minusMonths(1);
                            double prevbal = 0.0;
                            if (rolloverChoice.equalsIgnoreCase("y")) {
                                BudgetAccount priorAccount = findAccount(accountName, priorMonth);
                                if (priorAccount != null) {
                                    prevbal = (priorAccount != null) ? priorAccount.getBalance() : 0.0;
                                } else {
                                    System.out.println(
                                            "No account found for previous month. Starting with $0.00 rollover");
                                }
                            }

                            BudgetAccount newBudget = new BudgetAccount(accountName, month, spendingLimit, prevbal); // created
                                                                                                                     // ONCE
                            accounts.add(newBudget);
                            if (prevbal > 0) {
                                System.out.printf("Budget account created successfully. $%.2f Rollover from %s.%n",
                                        prevbal, priorMonth);
                            } else {
                                System.out.println("Budget account created successfully. No rollover from last month.");
                            }
                        } catch (DateTimeException | NumberFormatException e) {
                            System.out.println(
                                    "Invalid input. Please check your entries and try again: (" + e.getMessage() + ")");
                        }
                        break;
                    case 2:
                        try {
                            System.out.println("\nEnter account name: ");
                            String existingAccountName = scnr.nextLine();
                            System.out.println("Enter year: ");
                            int existingYear = Integer.parseInt(scnr.nextLine());
                            System.out.println("Enter month (1-12): ");
                            int existingMonthNumber = Integer.parseInt(scnr.nextLine());
                            YearMonth existingMonth = YearMonth.of(existingYear, existingMonthNumber);
                            BudgetAccount existingAccount = findAccount(existingAccountName, existingMonth);
                            if (existingAccount != null) {
                                try {
                                    System.out.println(
                                            "Account found. Enter transaction type (EXPENSE, INCOME, or TRANSFER): ");
                                    String typeInput = scnr.nextLine();
                                    TransactionType type = TransactionType.valueOf(typeInput.trim().toUpperCase());
                                    System.out.println("Enter transaction amount: ");
                                    double transactionAmount = Double.parseDouble(scnr.nextLine());
                                    System.out.println(
                                            "Enter category (FOOD, RENT, TRANSPORTATION, ENTERTAINMENT, OTHER) or press Enter to skip: ");
                                    String categoryInput = scnr.nextLine();
                                    Category category = categoryInput.isEmpty() ? Category.OTHER
                                            : Category.valueOf(categoryInput.trim().toUpperCase());
                                    Transaction addedTransaction = new Transaction(type, category, "Manual transaction",
                                            transactionAmount);
                                    existingAccount.addTransaction(addedTransaction);
                                    System.out.println("Transaction added successfully.");
                                } catch (IllegalArgumentException e) {
                                    System.out.println(
                                            "Couldn't add transaction - check your entries: (" + e.getMessage() + ")");
                                }
                            } else {
                                System.out.println("Account not found. Please try again.");
                            }
                        } catch (DateTimeException | NumberFormatException e) {
                            System.out.println(
                                    "Invalid input. Please check your entries and try again: (" + e.getMessage() + ")");
                        }
                        break;
                    case 3:
                        if (accounts.isEmpty()) {
                            System.out.println("\nNo budget accounts found.");
                            break;
                        } else {
                            System.out.println("\nExisting budget accounts:");
                            for (BudgetAccount account : accounts) {
                                System.out.println(account);
                                System.out.println("\nTransactions: \n");
                                for (Transaction t : account.getTransactions()) {
                                    System.out.println(t);
                                }
                            }
                        }
                        break;
                    case 4:
                        saveAllAccounts();
                        System.out.println("\nExiting application. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("\nInvalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
            }
        }

    }

    private BudgetAccount findAccount(String targetName, YearMonth targetMonth) {
        for (BudgetAccount account : accounts) { // find the accoutn with the matching name and month
            if (account.getAccountName().equals(targetName) && account.getMonth().equals(targetMonth)) {
                return account; // if one matches both name and month, return it
            }
        }
        return null; // if the loop finishes with no match, return null
    }

    private String sanitizeFilename(String name){
        return name.replaceAll("[^a-zA-Z0-9-]", "_");

    }
    private void saveAllAccounts() {
        File dir = new File("data");
            if (!dir.exists()) {
            dir.mkdirs();   // creates the folder (and any parent folders)
            }
        for (BudgetAccount account : accounts) {
            String filename = "data/" + sanitizeFilename(account.getAccountName()) + "_" + account.getMonth() + ".csv";
            try {
                BudgetAccountStorage.saveAccount(account, filename);
            } catch (IOException e) {
                System.out.println("Failed to save account " + account.getAccountName() + ": " + e.getMessage());
            }
        }
    }

    private void loadAllAccounts() {
        File dir = new File("data");
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (!file.getName().endsWith(".csv")) {
                continue;
            }
            try {
                BudgetAccount account = BudgetAccountStorage.loadAccount(file.getPath());
                accounts.add(account);
            } catch (IOException e) {
                System.out.println("Failed to load " + file.getName() + ": " + e.getMessage());
            }
            }
        }
    }

