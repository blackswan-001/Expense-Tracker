package com.example.expensetracker;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExpenseManager {
    private final List<Expense> expenses = new ArrayList<>();
    private final File expensesFile = new File("expenses.txt");
    private final File accountsFile = new File("accounts.txt");
    private final AtomicInteger nextId = new AtomicInteger(1);

    // keep references to two accounts
    private FakeAccount bankAccount;
    private FakeAccount gpayAccount;

    public ExpenseManager() {
        // default starting values
        bankAccount = new FakeAccount("Bank", 500_000.0);
        gpayAccount = new FakeAccount("GPay", 50_000.0);
        loadAccounts();
        loadExpenses();
    }

    public FakeAccount getBankAccount() { return bankAccount; }
    public FakeAccount getGpayAccount() { return gpayAccount; }

    public List<Expense> getExpenses() { return Collections.unmodifiableList(expenses); }

    public synchronized void addExpense(LocalDateTime timestamp, String category, double amount, String description, String accountName) {
        int id = nextId.getAndIncrement();
        Expense e = new Expense(id, timestamp, category, amount, description, accountName);
        expenses.add(0, e); // newest first
        saveExpenses();
        saveAccounts(); // because account balances may have changed
    }

    // Save expenses as CSV lines
    private synchronized void saveExpenses() {
        try (BufferedWriter bw = Files.newBufferedWriter(expensesFile.toPath())) {
            for (Expense e : expenses) {
                bw.write(e.toCsvLine());
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private synchronized void loadExpenses() {
        if (!expensesFile.exists()) return;
        try (BufferedReader br = Files.newBufferedReader(expensesFile.toPath())) {
            String line;
            int maxId = 0;
            List<Expense> loaded = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                Expense e = Expense.fromCsvLine(line);
                if (e != null) {
                    loaded.add(e);
                    maxId = Math.max(maxId, e.getId());
                }
            }
            // show newest first
            Collections.reverse(loaded);
            expenses.clear();
            expenses.addAll(loaded);
            nextId.set(maxId + 1);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // accounts: simple format: name,balance per line
    private synchronized void saveAccounts() {
        try (BufferedWriter bw = Files.newBufferedWriter(accountsFile.toPath())) {
            bw.write(bankAccount.getName() + "," + bankAccount.getBalance());
            bw.newLine();
            bw.write(gpayAccount.getName() + "," + gpayAccount.getBalance());
            bw.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private synchronized void loadAccounts() {
        if (!accountsFile.exists()) return;
        try (BufferedReader br = Files.newBufferedReader(accountsFile.toPath())) {
            String line;
            Map<String, Double> map = new HashMap<>();
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", 2);
                if (p.length == 2) {
                    try {
                        map.put(p[0], Double.parseDouble(p[1]));
                    } catch (NumberFormatException ignored) {}
                }
            }
            if (map.containsKey(bankAccount.getName())) {
                bankAccount = new FakeAccount(bankAccount.getName(), map.get(bankAccount.getName()));
            }
            if (map.containsKey(gpayAccount.getName())) {
                gpayAccount = new FakeAccount(gpayAccount.getName(), map.get(gpayAccount.getName()));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
