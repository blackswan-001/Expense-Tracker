# Expense Tracker

## Overview

Expense Tracker is a desktop-based personal finance management application developed using **JavaFX** and **JDK 21**. The application helps users record, categorize, and monitor their daily expenses through an intuitive graphical interface.

The system maintains two virtual accounts — **Bank Account** and **GPay Account** — allowing users to track spending from different payment sources. Every transaction updates account balances in real time and is displayed in a transaction history table. A dynamic pie chart provides visual insights into spending patterns across various categories.

---

## Features

### Transaction Management

* Add new expenses with:

  * Amount
  * Category
  * Payment Source (Bank or GPay)
  * Description
* Automatic balance deduction from the selected account.
* Real-time balance updates.
* Transaction history with timestamp tracking.

### Expense Categorization

Expenses can be categorized into:

* Food
* Travel
* Shopping
* Utilities
* Bills
* Miscellaneous

### Dashboard Analytics

* Interactive pie chart for category-wise expense distribution.
* Real-time account balance display.
* Transaction history table.
* Automatic dashboard refresh after each transaction.

### User-Friendly Interface

* Clean and responsive JavaFX GUI.
* Easy navigation and intuitive controls.
* Instant visual feedback for all operations.

---

## Objectives

* Design a simple and user-friendly expense tracking application.
* Provide real-time account balance updates.
* Categorize and organize expenses efficiently.
* Visualize spending patterns using charts.
* Demonstrate JavaFX-based desktop application development.
* Encourage better financial awareness and management.

---

## System Requirements

### Hardware Requirements

| Component | Requirement                     |
| --------- | ------------------------------- |
| Processor | Intel Core i3 or higher         |
| RAM       | 4 GB minimum (8 GB recommended) |
| Storage   | 200 MB free space               |
| Display   | 1366 × 768 resolution or higher |

### Software Requirements

| Component        | Requirement                      |
| ---------------- | -------------------------------- |
| Operating System | Windows, Linux, or macOS         |
| Java Version     | JDK 21                           |
| JavaFX Version   | JavaFX SDK 21.0.8                |
| IDE/Editor       | VS Code or any Java IDE          |
| Build Tool       | javac / java or IDE build system |

---

## Project Structure

```text
ExpenseTracker/
│
├── src/
│   ├── controller/
│   ├── model/
│   ├── view/
│   └── main/
│
├── resources/
│   ├── fxml/
│   └── css/
│
├── README.md
└── pom.xml (optional)
```

---

## Functional Requirements

### Transaction Module

* Record expenses.
* Select expense category.
* Choose payment source.
* Validate transaction amount.
* Update account balances automatically.
* Store expense details with timestamp.

### Dashboard Module

* Display all transactions in tabular format.
* Generate category-wise expense pie chart.
* Show remaining account balances.
* Refresh dynamically after every transaction.

---

## Non-Functional Requirements

### Usability

Simple and intuitive interface for all users.

### Reliability

Accurate balance calculations and transaction tracking.

### Portability

Compatible with Windows, Linux, and macOS systems.

### Maintainability

Modular Java architecture for easy updates and feature additions.

### Performance

Instant response using JavaFX event-driven programming.

### Scalability

Can be extended with:

* Database integration
* Multiple account support
* Budget planning
* Expense reports
* Cloud synchronization

---

## Application Workflow

### Transaction Processing

1. User enters:

   * Amount
   * Category
   * Account Type
   * Description

2. System validates:

   * Amount validity
   * Available account balance

3. System deducts amount from selected account.

4. New expense record is created.

5. Transaction is added to the history table.

6. Pie chart updates automatically.

7. Success confirmation is displayed.

---

### Dashboard Update

1. Retrieve all expenses.
2. Group expenses by category.
3. Calculate total spending per category.
4. Generate pie chart segments.
5. Update account balances.
6. Refresh dashboard automatically.

---

## Modules

### 1. Transaction Module

Responsible for recording and managing expenses.

**Functions:**

* Expense entry
* Account validation
* Balance deduction
* Transaction logging
* Real-time updates

---

### 2. Dashboard Module

Provides visual and tabular representations of financial data.

**Functions:**

* Pie chart generation
* Transaction table display
* Account balance monitoring
* Automatic dashboard refresh

---

## Execution Environment

| Component | Details                           |
| --------- | --------------------------------- |
| Platform  | Desktop Application               |
| Frontend  | JavaFX GUI, FXML, CSS             |
| Backend   | Java Classes                      |
| Runtime   | Java Runtime Environment (JRE 21) |
| Tested On | Windows 10/11                     |

---

## Future Enhancements

* Database integration (MySQL/SQLite)
* User authentication
* Monthly budget tracking
* Expense filtering and search
* Export reports to PDF/Excel
* Cloud backup and synchronization
* Mobile application support

---

## Conclusion

The Expense Tracker application demonstrates the practical use of JavaFX and object-oriented programming principles for personal finance management. Through real-time balance updates, categorized expense tracking, and graphical expense visualization, the application offers users an efficient and organized way to manage their finances.

Its modular design allows future enhancements such as database support, budget planning, and cloud integration, making it a scalable foundation for advanced financial management systems.

---

## Technologies Used

* Java 21
* JavaFX 21.0.8
* FXML
* CSS
* VS Code
* Object-Oriented Programming (OOP)

---

## Author

Developed as an academic project to demonstrate desktop application development using JavaFX and modern Java technologies.
