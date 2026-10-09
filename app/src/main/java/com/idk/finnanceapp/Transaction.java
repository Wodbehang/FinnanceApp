package com.idk.finnanceapp;

public class Transaction {
    private String title;
    private String amount;
    private String date;
    private int type;
    private int id;

    public final int INCOME = 0;
    public final int EXPENSE = 1;


    public Transaction(int ID, String title, String amount, String date, int type) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.id = ID;
    }

    public String getTitle() {
        return title;
    }
    public int getID() {
        return id;
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

        String STransaction = transaction.getID() + transaction.getTitle();
        if (type == 0){
            STransaction += "- " + transaction.getAmount() + transaction.getDate();
        }else{
            STransaction += "+ " + transaction.getAmount() + transaction.getDate();
        }

        return STransaction;
    }
}
