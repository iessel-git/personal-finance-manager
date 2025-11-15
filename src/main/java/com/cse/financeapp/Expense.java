package com.cse.financeapp;

public class Expense {

    private int id;
    private String category;
    private double amount;
    private String date;  // Store as text for now (YYYY-MM-DD)
    private String note;

    public Expense(int id, String category, double amount, String date, String note) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    public Expense(String category, double amount, String date, String note) {
        this(-1, category, amount, date, note);
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
