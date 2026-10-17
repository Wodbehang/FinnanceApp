package com.idk.finnanceapp;

import com.idk.finnanceapp.RecycleView.Date;

public class Transaction {
    private String title;
    private String amount;
    private Date date;
    private int type;
    private int id;
    private String category;
    private String note;

    public final int INCOME = 0;
    public final int EXPENSE = 1;


    public Transaction(int ID, String title, String amount, Date date, int type) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.id = ID;
    }
    public Transaction(int ID, String title, String amount, Date date, int type, String category, String note) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.id = ID;
        this.category = category;
        this.note = note;
    }

    public String getTitle() {
        return title;
    }
    public String getCategory() {
        return category;
    }
    public String getNote() {
        return note;
    }
    public int getID() {
        return id;
    }
    public String getAmount() {
        return amount;
    }

    public Date getDate() {
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

    public void setTitle(String title) {
        this.title = title;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setType(int type) {
        this.type = type;
    }
}

