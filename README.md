UniBudget Mobile Application

Overview

UniBudget is a mobile budgeting application developed using Android Studio and Kotlin. The purpose of the application is to help users manage their personal finances by tracking expenses, setting budget goals, organizing spending categories, and viewing financial reports.

The application was developed as part of an academic assignment to demonstrate Android mobile development concepts such as:

* Multi-activity navigation
* Local data persistence using Room Database
* User interaction and form validation
* Camera integration
* Data visualization and reporting



Features

User Authentication

* Users can log into the application using a username and password.

Expense Management

* Add expense entries
* Enter:
    * Amount
    * Category
    * Description
    * Date
    * Start time
    * End time
* Save expenses locally using Room Database

Category Management

* Create and manage expense categories
* Organize expenses into categories such as:
    * Food
    * Transport
    * Entertainment
    * Shopping
    * Bills

Budget Goals

* Set minimum monthly spending goals
* Set maximum monthly spending goals
* Track spending progress

Receipt Scanning

* Capture receipt images using the device camera
* Use Google ML Kit Text Recognition to scan receipt text
* Automatically detect and autofill expense amounts

Expense History

* View all saved expenses
* Display:
    * Amount
    * Category
    * Description
    * Date
    * Time information

Reports and Analytics

* View total spending summaries
* Display graphical reports using charts
* Analyze spending behaviour by category

Offline Data Storage

* All application data is stored locally using Room Database
* No internet connection is required



Technologies Used

Technology	Purpose
Kotlin	Main programming language
Android Studio	Development environment
Room Database	Local database storage
XML	User interface design
Google ML Kit	Receipt text recognition
MPAndroidChart	Graph and chart visualization
Coroutines	Background database operations



Application Screens

The application includes the following screens:

1. Login Screen
2. Dashboard Screen
3. Add Expense Screen
4. Category Management Screen
5. Budget Screen
6. View Expenses Screen
7. Reports Screen



Database Structure

The application uses Room Database for local storage.

Main Entities

* Expense
* Budget
* Category
* User

Database Components

* Entities
* DAO interfaces
* AppDatabase class



Camera and OCR Functionality

The application integrates Android Camera functionality using Activity Result Contracts.

Google ML Kit Text Recognition is used to:

* Scan receipts
* Detect text from captured images
* Extract expense amounts automatically



Installation Instructions

Requirements

* Android Studio
* Android device or emulator
* Minimum SDK: 24

Steps

1. Open the project in Android Studio
2. Sync Gradle files
3. Build the project
4. Run the application on a physical device or emulator



APK Generation

To generate the APK file:

1. Open Android Studio
2. Click:

Build → Generate App Bundles or APKs → Build APKs

3. Locate the generated APK in:

app/build/outputs/apk/debug/



Future Improvements

Possible future enhancements include:

* Firebase authentication
* Cloud data backup
* Dark mode support
* Advanced analytics
* Monthly spending notifications
* Export reports to PDF



Video Demonstration

YouTube Presentation Link:

https://youtu.be/ZMVAJJgp-Ks



Conclusion

UniBudget successfully demonstrates the implementation of a functional Android budgeting application.

The application meets the assignment requirements by providing:

* User authentication
* Expense tracking
* Category management
* Budget setting
* Receipt scanning
* Financial reporting
* Local database persistence

The project showcases practical Android development skills using modern Android technologies and design principles.
