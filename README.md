# UniBudget Mobile Application

## Overview

UniBudget is a mobile budgeting application developed using Android Studio. The purpose of the app is to help users manage their personal finances by tracking expenses, setting budgets, and viewing financial reports.

This application was developed as part of an academic assignment to demonstrate mobile development concepts such as multi-activity navigation, local data storage using Room Database, and user interaction.

---

## Features

* User Registration and Login (Authentication)
* Add Expenses
* View All Expenses
* Set Budget Goals
* Category Management (Basic implementation)
* Reports (Total spending overview)
* Camera Integration (Capture feature)
* Dashboard Navigation (Multiple screens)

---

## Technologies Used

* Kotlin (Programming Language)
* Android Studio
* Room Database (Local storage)
* XML (UI Design)
* Coroutines (Background operations)

---

## System Architecture

The application follows a simple modular structure:

* Activities handle user interface and interaction
* Room Database handles data storage
* DAO (Data Access Object) manages database queries
* Entities represent data models (User, Expense, Category)

---

## Key Components

* LoginActivity – Handles user authentication
* DashboardActivity – Main navigation hub
* AddExpenseActivity – Adds new expenses
* ViewExpensesActivity – Displays saved expenses
* BudgetActivity – Allows setting a budget
* ReportsActivity – Displays total spending
* CategoryActivity – Basic category feature

---

## Testing

The application was tested by:

* Registering and logging in users
* Adding and retrieving expenses
* Navigating between activities
* Verifying database persistence

---

## Camera Feature

The application includes camera functionality which allows users to capture images. This demonstrates the integration of device hardware into the application.

---

## Known Limitations

* Category feature is basic
* Reports are simplified (total spending only)
* UI can be further enhanced

---
## How to Run the App

1. Open the project in Android Studio
2. Allow Gradle to sync
3. Run the application using:

    * Android Emulator OR
    * Physical Android Device


---
## Author / Team

Developed as part of a group academic project.


---
## Conclusion

UniBudget demonstrates core mobile application development concepts including user authentication, database integration, multiscreen navigation, and feature implementation within a structured Android application.
