package com.cse.financeapp.models;

public class Budget {
    private final int id;
    private final int categoryId;
    private final double amount;
    private final String month; // flexible for now

    public Budget(int id, int categoryId, double amount, String month) {
        this.id = id;
        this.categoryId = categoryId;
        this.amount = amount;
        this.month = month;
    }

    public int getId() { return id; }
    public int getCategoryId() { return categoryId; }
    public double getAmount() { return amount; }
    public String getMonth() { return month; }
}
