package com.example.expensetracker;

import javafx.beans.binding.Bindings;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DashboardController {

    // UI elements from FXML
    @FXML private Label bankBalanceLabel;
    @FXML private Label gpayBalanceLabel;
    @FXML private PieChart pieChart;
    @FXML private TableView<ExpenseRow> expenseTable;
    @FXML private TableColumn<ExpenseRow, String> colDate;
    @FXML private TableColumn<ExpenseRow, String> colCategory;
    @FXML private TableColumn<ExpenseRow, String> colAmount;
    @FXML private TableColumn<ExpenseRow, String> colAccount;
    @FXML private TableColumn<ExpenseRow, String> colDescription;

    @FXML private ComboBox<String> accountCombo;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private TextField amountField;
    @FXML private TextField descriptionField;
    @FXML private Button payButton;
    @FXML private Label statusLabel;

    private final ExpenseManager manager = new ExpenseManager();
    private final ObservableList<ExpenseRow> tableData = FXCollections.observableArrayList();

    private final List<String> categories = Arrays.asList("Food", "Travel", "Shopping", "Utilities", "Bills", "Misc");

    private final NumberFormat inrFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        // Bind balances
        bankBalanceLabel.textProperty().bind(Bindings.createStringBinding(
                () -> inrFormat.format(manager.getBankAccount().getBalance()),
                manager.getBankAccount().balanceProperty()));

        gpayBalanceLabel.textProperty().bind(Bindings.createStringBinding(
                () -> inrFormat.format(manager.getGpayAccount().getBalance()),
                manager.getGpayAccount().balanceProperty()));

        // Account combo
        accountCombo.getItems().addAll(manager.getBankAccount().getName(), manager.getGpayAccount().getName());
        accountCombo.setValue(manager.getBankAccount().getName());

        // Category combo
        categoryCombo.getItems().addAll(categories);
        categoryCombo.setValue(categories.get(0));

        // Table columns setup
        colDate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amountFormatted"));
        colAccount.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        expenseTable.setItems(tableData);

        // load existing expenses
        loadExpensesToTable();

        // build pie chart
        refreshChart();

        // configure amount field to accept only numbers (simple)
        amountField.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("[0-9]*\\.?[0-9]*")) {
                amountField.setText(oldV);
            }
        });

        // pay button action
        payButton.setOnAction(e -> onPay());
    }

    private void loadExpensesToTable() {
        tableData.clear();
        for (Expense ex : manager.getExpenses()) {
            tableData.add(new ExpenseRow(ex));
        }
    }

    private void onPay() {
        String acc = accountCombo.getValue();
        String category = categoryCombo.getValue();
        String amtText = amountField.getText();
        String desc = descriptionField.getText();

        if (amtText == null || amtText.isBlank()) {
            setStatus("Enter an amount.", true);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amtText);
            if (amount <= 0) {
                setStatus("Amount must be greater than zero.", true);
                return;
            }
        } catch (NumberFormatException ex) {
            setStatus("Invalid amount format.", true);
            return;
        }

        FakeAccount account = acc.equals(manager.getGpayAccount().getName()) ? manager.getGpayAccount() : manager.getBankAccount();

        boolean success = account.debit(amount);
        if (!success) {
            setStatus("Insufficient funds in " + account.getName(), true);
            return;
        }

        // Add expense to manager (persisted), log immediately
        LocalDateTime now = LocalDateTime.now();
        manager.addExpense(now, category, amount, desc, account.getName());

        // add to UI
        Expense latest = manager.getExpenses().isEmpty() ? null : manager.getExpenses().get(0);
        if (latest != null) {
            tableData.add(0, new ExpenseRow(latest));
        } else {
            // fallback construct
            tableData.add(0, new ExpenseRow(new Expense(0, now, category, amount, desc, account.getName())));
        }

        // refresh chart & clear fields
        refreshChart();
        amountField.clear();
        descriptionField.clear();
        setStatus("Payment of " + inrFormat.format(amount) + " from " + account.getName() + " logged.", false);
    }

    private void refreshChart() {
        Map<String, Double> byCat = manager.getExpenses().stream()
                .collect(Collectors.groupingBy(Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)));

        // ensure categories appear even with zero
        for (String c : categories) {
            byCat.putIfAbsent(c, 0.0);
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (String c : categories) {
            double v = byCat.getOrDefault(c, 0.0);
            if (v > 0.0) pieData.add(new PieChart.Data(c + " " + inrFormat.format(v), v));
        }

        pieChart.setData(pieData);
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(10);
        pieChart.setLegendVisible(true);
    }

    private void setStatus(String text, boolean isError) {
        statusLabel.setText(text);
        statusLabel.getStyleClass().removeAll("status-ok", "status-error");
        statusLabel.getStyleClass().add(isError ? "status-error" : "status-ok");
    }

    // Small wrapper class for TableView rows (for formatted fields)
    public static class ExpenseRow {
        private final String timestamp;
        private final String category;
        private final String amountFormatted;
        private final String accountName;
        private final String description;

        public ExpenseRow(Expense e) {
            this.timestamp = e.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            this.category = e.getCategory();
            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en","IN"));
            this.amountFormatted = nf.format(e.getAmount());
            this.accountName = e.getAccountName();
            this.description = e.getDescription();
        }

        public String getTimestamp() { return timestamp; }
        public String getCategory() { return category; }
        public String getAmountFormatted() { return amountFormatted; }
        public String getAccountName() { return accountName; }
        public String getDescription() { return description; }
    }
}
