package com.idk.finnanceapp;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utilitis {

    List<Transaction> transactions = new ArrayList<>();
    private String category;
    private String note;

    public void saveTransactionsToCVS(List<Transaction> transactions, String filePath) {
        File file = new File(filePath);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Name,Amount,Category,Notes\n");
            for (Transaction transaction : transactions) {
                writer.write(transaction.getTitle()  + "," + transaction.getAmount() + "," + transaction.getCategory() + "," + transaction.getNote() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
