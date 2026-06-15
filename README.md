1. Project Overview

UniBudget is an Android-based budgeting and expense tracking application developed using Kotlin and Room Database. The application assists students in managing their finances by tracking expenses, categorising spending, setting budgets, and generating reports.

The application aims to encourage responsible financial management and provide students with a simple and intuitive budgeting solution.

2. Purpose of the Application

Many students struggle to manage their monthly spending and often exceed their budgets without realizing it.

UniBudget was developed to:

Track daily expenses
Categorise spending habits
Monitor budgets
Generate spending reports
Digitally capture receipt information
Improve financial awareness among students
3. Features
   User Authentication
   User registration
   User login
   Secure user data storage
   Expense Management
   Add expenses
   View expenses
   Edit expense information
   Categorise expenses
   Budget Management
   Set budget limits
   Track spending against budgets
   Reports
   View spending summaries
   Analyse expenditure patterns
   Receipt Scanning
   Capture receipt images using the device camera
   Extract numerical values using OCR (ML Kit)
4. Design Considerations

The following design principles were considered during development:

Usability

The interface was designed to be simple and easy to navigate for students with little technical experience.

Performance

Room Database was used to provide efficient local storage and quick retrieval of financial records.

Maintainability

The project follows a modular structure with separate activities, entities, DAO classes and database components.

Scalability

The architecture allows additional features such as cloud synchronisation and advanced reporting to be added in future versions.

Accessibility

Simple layouts and readable colour schemes were used to improve user experience.

5. Technologies Used
   Technology	Purpose
   Kotlin	Android application development
   Android Studio	Development environment
   Room Database	Local data persistence
   ML Kit Text Recognition	OCR receipt scanning
   Git	Version control
   GitHub	Repository hosting
   GitHub Actions	CI/CD automation
6. Database Design
   Entities
   User

Stores user registration details.

Expense

Stores:

Amount
Category
Description
Date
Budget

Stores:

Budget limits
Budget categories
7. GitHub Usage

GitHub was used for:

Source code management
Version control
Team collaboration
Backup of project files
Tracking project progress

Repository Features Used:

Commits
Branch management
Repository hosting
README documentation
8. GitHub Actions

GitHub Actions was implemented to automate project verification.

The workflow can be configured to:

Automatically build the Android project
Validate source code changes
Detect build failures
Improve software quality

Benefits include:

Continuous Integration (CI)
Early bug detection
Improved collaboration
Reduced deployment risks

10. Installation Instructions
    Clone the repository
    git clone https://github.com/YourUsername/OPSC6311-UniBudget.git
    Open the project in Android Studio
    Sync Gradle files
    Run the application on an Android device or emulator
11. Future Improvements
    Cloud database integration
    Data export functionality
    Budget notifications
    Multi-user support
    Advanced analytics
    Improved OCR receipt detection