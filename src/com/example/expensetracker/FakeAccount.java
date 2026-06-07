package com.example.expensetracker;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class FakeAccount {
    private final String name;
    private final DoubleProperty balance; // property for binding

    public FakeAccount(String name, double startingBalance) {
        this.name = name;
        this.balance = new SimpleDoubleProperty(startingBalance);
    }

    public String getName() { return name; }
    public double getBalance() { return balance.get(); }
    public DoubleProperty balanceProperty() { return balance; }

    /**
     * Try to debit an amount. Returns true if successful (sufficient funds),
     * false otherwise (no overdraft).
     */
    public boolean debit(double amount) {
        if (amount < 0) return false;
        double current = balance.get();
        if (current >= amount) {
            balance.set(current - amount);
            return true;
        } else {
            return false;
        }
    }

    public void credit(double amount) {
        if (amount < 0) return;
        balance.set(balance.get() + amount);
    }
}
