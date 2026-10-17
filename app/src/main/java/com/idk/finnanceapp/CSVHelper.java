package com.idk.finnanceapp;

import android.content.Context;

import com.idk.finnanceapp.RecycleView.Date;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    List<Transaction> transactions = new ArrayList<>();
    private String category;
    private String note;
    public CSVHelper(){

    }

    private Context context;
    private SQLiteUserHelperDB sqLiteUserHelperDB;
    private String username;
    public void initializeCSV(String filePath) {
        if (filePath == null || filePath.isEmpty()) return;
        File file = new File(filePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Balance,0.0\n");
            writer.write("ID,Title,Amount,Date,Type,Category,Note\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasBalanceInCSV(String filePath) {
        if (filePath == null || filePath.isEmpty()) return false;
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.toLowerCase().startsWith("balance,")) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2 && !parts[1].trim().isEmpty()) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double readBalanceFromCSV(String filePath) {
        if (filePath == null || filePath.isEmpty()) return 0.0;
        File file = new File(filePath);
        if (!file.exists()) {
            return 0.0;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.toLowerCase().startsWith("balance,")) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        try {
                            return Double.parseDouble(parts[1].trim());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public List<Transaction> readTransactionsFromCSV(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        if (filePath == null || filePath.isEmpty()) return transactions;
        File file = new File(filePath);
        if (!file.exists()) {
            return transactions;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.toLowerCase().startsWith("balance,") || line.toLowerCase().startsWith("id,")) {
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length >= 5) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String title = parts[1].trim();
                        String amount = parts[2].trim();
                        Date date = parseDate(parts[3].trim());
                        int type = Integer.parseInt(parts[4].trim());
                        String category = parts.length > 5 ? parts[5].trim() : "";
                        String note = parts.length > 6 ? parts[6].trim() : "";

                        transactions.add(new Transaction(id, title, amount, date, type, category, note));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public void saveCSV(String filePath, double balance, List<Transaction> transactions) {
        if (filePath == null || filePath.isEmpty()) return;
        File file = new File(filePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Balance," + balance + "\n");
            writer.write("ID,Title,Amount,Date,Type,Category,Note\n");
            if (transactions != null) {
                for (Transaction transaction : transactions) {
                    String dateStr = formatDate(transaction.getDate());
                    String title = transaction.getTitle() != null ? transaction.getTitle() : "";
                    String amount = transaction.getAmount() != null ? transaction.getAmount() : "0";
                    String category = transaction.getCategory() != null ? transaction.getCategory() : "";
                    String note = transaction.getNote() != null ? transaction.getNote() : "";

                    writer.write(transaction.getID() + ","
                            + title + ","
                            + amount + ","
                            + dateStr + ","
                            + transaction.getType() + ","
                            + category + ","
                            + note + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBalanceToCSV(String filePath, double balance) {
        List<Transaction> transactions = readTransactionsFromCSV(filePath);
        saveCSV(filePath, balance, transactions);
    }

    public void saveTransactionsToCVS(List<Transaction> transactions, String filePath) {
        double balance = readBalanceFromCSV(filePath);
        saveCSV(filePath, balance, transactions);
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "00.00.00";
        }
        return date.getDay() + "." + date.getMonth() + "." + date.getYear();
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return new Date(00, 0, 0000);
        }
        String[] parts = dateStr.trim().split("[.\\s/]+");
        if (parts.length >= 3) {
            try {
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                return new Date(day, month, year);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return new Date(00, 00, 0000);
    }
}
