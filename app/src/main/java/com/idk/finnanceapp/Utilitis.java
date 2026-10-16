package com.idk.finnanceapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;
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

    private Context context;


    public void saveTransactionsToCVS(List<Transaction> transactions, String filePath) {
        File file = new File(filePath);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Date,Name,Amount,Category,Notes\n");
            for (Transaction transaction : transactions) {
                writer.write(transaction.getDate() + "," + transaction.getTitle()  + "," + transaction.getAmount() + "," + transaction.getCategory() + "," + transaction.getNote() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
