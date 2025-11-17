package com.cse.financeapp.models;

import java.time.LocalDate;

public class Expense {
    private final int id;
    private final String description;
    private final double amount;
    private final LocalDate date;
    private final int categoryId;
    private final String note;

    public Expense(int id, String description, double amount, LocalDate date, int categoryId, String note) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
        this.note = note;
    }

    public Expense(int id, String description, double amount, LocalDate date, int categoryId) {
        this(id, description, amount, date, categoryId, null);
    }

    public Expense(String description, double amount, LocalDate date, int categoryId) {
        this(-1, description, amount, date, categoryId, null);
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public int getCategoryId() { return categoryId; }
    public String getNote() { return note; }

    @Override
    public String toString() {
        return "Expense{id=" + id + ", description='" + description + "', amount=" + amount +
                ", date=" + date + ", categoryId=" + categoryId + ", note='" + note + "'}";
    }
}
