package com.cse.financeapp.models;

public class Budget {

    private int id;
    private int categoryId;
    private double limitAmount;

    public Budget(int id, int categoryId, double limitAmount) {
        this.id = id;
        this.categoryId = categoryId;
        this.limitAmount = limitAmount;
    }

    public Budget(int categoryId, double limitAmount) {
        this(-1, categoryId, limitAmount);
    }

    public int getId() { return id; }

    public int getCategoryId() { return categoryId; }

    public double getLimitAmount() { return limitAmount; }
}

