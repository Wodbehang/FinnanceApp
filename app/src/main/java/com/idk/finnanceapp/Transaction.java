package com.idk.finnanceapp;

public class Transaction {
    private String title;
    private String amount;
    private String date;
    private static int type;

    public Transaction(String title, String amount, String date, int type) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public int getType() {
        return type;
    }
    public String toString(Transaction transaction){
        String STransaction = "Title: " + transaction.getTitle();
        if (type == 0){
            STransaction += "Amount: - " + transaction.getAmount();
        }else{
            STransaction += "Amount: + " + transaction.getAmount();
        }
        STransaction += "Date: " + transaction.getDate();

        return STransaction;
    }
}
