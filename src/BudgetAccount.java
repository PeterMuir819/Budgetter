import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;

public class BudgetAccount {
    
    private String accountName;
    private double balance;
    private String month;
    private double spendingLimit = 1200.00;
    private double previousBalance;
    private ArrayList<Transaction> transactions;

    public BudgetAccount(String accountName, String month, double spendingLimit) {
        this.balance = 0.0;
        this.month = month;
        this.accountName = accountName;
        this.spendingLimit = spendingLimit;
        this.transactions = new ArrayList<>();
    }    

    public BudgetAccount(String accountName, String month, double spendingLimit, double previousBalance) {
        this.accountName = accountName;
        this.month = month;
        this.spendingLimit = spendingLimit;
        this.previousBalance = previousBalance;
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setMonth(String month) {
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

    public double getBalance() {
        double total = previousBalance;         // start with previous balance
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
        Transaction adjustment = new Transaction(type, Category.OTHER, "Manual balance adjustment", Math.abs(difference));
        addTransaction(adjustment);
    }


}
