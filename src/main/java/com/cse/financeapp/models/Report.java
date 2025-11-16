package com.cse.financeapp.models;

public class Report {

    private String category;
    private double totalSpent;

    public Report(String category, double totalSpent) {
        this.category = category;
        this.totalSpent = totalSpent;
    }

    public String getCategory() { return category; }

    public double getTotalSpent() { return totalSpent; }
}

