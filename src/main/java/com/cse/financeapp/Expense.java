package com.cse.financeapp;

import java.time.LocalDate;

public class Expense {

    private int id;
    private String category;
    private double amount;
    private LocalDate date; 
    private String note;

    // Full constructor (used when reading from database)
    public Expense(int id, String category, double amount, LocalDate date, String note) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    // Constructor for new expenses (ID auto-generated)
    public Expense(String category, double amount, LocalDate date, String note) {
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

    public LocalDate getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    // Optional setters
    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", note='" + note + '\'' +
                '}';
    }
}
