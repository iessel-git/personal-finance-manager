package com.example.financeapp.models;

import java.time.LocalDate;

public class Expense {

    private int id;
    private String description;
    private double amount;
    private LocalDate date;
    private int categoryId;

    public Expense(int id, String description, double amount, LocalDate date, int categoryId) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
    }

    public Expense(String description, double amount, LocalDate date, int categoryId) {
        this(-1, description, amount, date, categoryId);
    }

    public int getId() { return id; }

    public String getDescription() { return description; }

    public double getAmount() { return amount; }

    public LocalDate getDate() { return date; }

    public int getCategoryId() { return categoryId; }

    @Override
    public String toString() {
        return "Expense{id=" + id +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", categoryId=" + categoryId +
                '}';
    }
}

