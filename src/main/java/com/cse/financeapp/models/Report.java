package com.cse.financeapp.models;

public class Report {
    private final String label;
    private final double value;

    public Report(String label, double value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() { return label; }
    public double getValue() { return value; }
}
