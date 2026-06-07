package com.example.expensetracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Expense {
    private final int id;
    private final LocalDateTime timestamp;
    private final String category;
    private final double amount;
    private final String description;
    private final String accountName;

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Expense(int id, LocalDateTime timestamp, String category, double amount, String description, String accountName) {
        this.id = id;
        this.timestamp = timestamp;
        this.category = category;
        this.amount = amount;
        this.description = description == null ? "" : description;
        this.accountName = accountName;
    }

    public int getId() { return id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public String getAccountName() { return accountName; }

    public String toCsvLine() {
        // escape commas in description by replacing with &#44;
        String safeDesc = description.replace(",", "&#44;");
        return id + "," + timestamp.format(fmt) + "," + category + "," + amount + "," + safeDesc + "," + accountName;
    }

    public static Expense fromCsvLine(String line) {
        // id,timestamp,category,amount,description,account
        String[] parts = line.split(",", 6);
        if (parts.length < 6) return null;
        int id = Integer.parseInt(parts[0]);
        LocalDateTime ts = LocalDateTime.parse(parts[1], fmt);
        String category = parts[2];
        double amount = Double.parseDouble(parts[3]);
        String desc = parts[4].replace("&#44;", ",");
        String accountName = parts[5];
        return new Expense(id, ts, category, amount, desc, accountName);
    }
}
