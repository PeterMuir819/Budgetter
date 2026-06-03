package budgetapp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.YearMonth;

public class BudgetAccountStorage {
    
    public static void saveAccount(BudgetAccount account, String filePath) throws IOException{
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(account.getAccountName() + "," + account.getMonth() + "," + account.getSpendingLimit() + "," + account.getPreviousBalance());
            writer.newLine();
            for (Transaction t : account.getTransactions()) {
                writer.write(t.getType() + "," + t.getCategory() + "," + t.getDescription().replace(",", ";") + "," + t.getDate() + "," + t.getAmount());
                writer.newLine();
            }
        }
    }

    public static BudgetAccount loadAccount(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String headerLine = reader.readLine();
            String[] headerParts = headerLine.split(",");
            String accountName = headerParts[0];
            YearMonth month = YearMonth.parse(headerParts[1]);
            double spendingLimit = Double.parseDouble(headerParts[2]);
            double previousBalance =  Double.parseDouble(headerParts[3]);
            BudgetAccount account = new BudgetAccount(accountName, month, spendingLimit, previousBalance);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                TransactionType type = TransactionType.valueOf(parts[0]);
                Category category = Category.valueOf(parts[1]);
                String description = parts[2].replace(";", ",");
                double amount = Double.parseDouble(parts[4]);
                Transaction t = new Transaction(type, category, description, amount);
                t.setDate(LocalDate.parse(parts[3]));
                account.addTransaction(t);
            }
            return account;
        }
    }
}
