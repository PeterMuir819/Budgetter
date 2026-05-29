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
        this.transactions = new ArrayList<>();
    }

    public BudgetAccount(String accountName, String month, double spendingLimit, double previousBalance) {
        this.accountName = accountName;
        this.month = month;
        this.spendingLimit = spendingLimit;
        this.balance = previousBalance;
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

    public double getBalance() {
        double total = 0.0;
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


}
