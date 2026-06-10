package budgetapp;

import java.time.YearMonth;
import java.util.ArrayList;

public class BudgetAccount {

    private String accountName;
    private YearMonth month;
    private double spendingLimit = 1200.00;
    private double previousBalance;
    private ArrayList<Transaction> transactions;

    public BudgetAccount(String accountName, YearMonth month) {
        this.accountName = accountName;
        this.month = month;
        this.transactions = new ArrayList<>();
    }

    public BudgetAccount(String accountName, YearMonth month, double spendingLimit) {
        this.month = month;
        this.accountName = accountName;
        this.spendingLimit = spendingLimit;
        this.transactions = new ArrayList<>();
    }

    public BudgetAccount(String accountName, YearMonth month, double spendingLimit, double previousBalance) {
        this.accountName = accountName;
        this.month = month;
        this.spendingLimit = spendingLimit;
        this.previousBalance = previousBalance;
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        if (transaction.getCategory().getType() != transaction.getType()) {
            throw new IllegalArgumentException(transaction.getCategory() + " is not valid for " + transaction.getType());
        }
        transactions.add(transaction);
    }

    public double getTotalSpent() {
        double spent = 0.0; // local variable, not an instance field
        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.EXPENSE) {
                spent += t.getAmount();
            }
        }
        return spent;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setMonth(YearMonth month) {
        this.month = month;
    }

    public void setSpendingLimit(double spendingLimit) {
        this.spendingLimit = spendingLimit;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getAccountName() {
        return accountName;
    }

    public YearMonth getMonth() {
        return month;
    }

    public double getPreviousBalance() {
        return previousBalance;
    }

    public double getRemainingBudget() {
        return spendingLimit - getTotalSpent();
    }

    public boolean isOverLimit() {
        return getTotalSpent() > spendingLimit;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public double getSpendingLimit() {
        return spendingLimit;
    }

    public double getBalance() {
        double total = previousBalance; // start with previous balance
        for (Transaction t : transactions) {
            switch (t.getType()) {
                case INCOME:
                case TRANSFER:
                    total += t.getAmount();
                    break;
                case EXPENSE:
                    total -= t.getAmount();
                    break;
            }
        }
        return total;
    }

    public void adjustToBalance(double targetBalance) {
        double difference = targetBalance - getBalance();
        TransactionType type = difference > 0 ? TransactionType.INCOME : TransactionType.EXPENSE;  
        Category category = (type == TransactionType.INCOME) ? Category.OTHER_INCOME : Category.OTHER_EXPENSE;
        Transaction adjustment = new Transaction(type, category, "Manual balance adjustment", Math.abs(difference));
        addTransaction(adjustment);
    }

    @Override
    public String toString() {
        return String.format("%s | %s | Balance: $%.2f | Spent: $%.2f | Remaining: $%.2f", accountName, month,
                getBalance(), getTotalSpent(), getRemainingBudget());
    }

}
