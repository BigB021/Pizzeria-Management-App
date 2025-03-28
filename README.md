# Pizzeria-Management-App
A desktop application built with Java Swing to manage a pizzeria’s day-to-day operations. This project allows you to log in with appropriate credentials (e.g., Manager or Cashier), view dashboards, manage categories, products, staff, and handle kitchen operations.
# Features
## Login Panel

* Provides secure login for different roles (MANAGER/CASHIER).

* Includes simple validation against an Oracle database.

## Dashboard & Panels

* **Dshboard**: Overview of the pizzeria’s activities (placeholder in this snippet).

* **Categories**: Manage pizza categories or other menu sections.

* **Products**: Manage individual menu items (e.g., pizzas, sides, beverages).

* **Staff**: Manage staff information.

* **Kitchen**: Manage or monitor kitchen operations (e.g., order statuses).

## Side Menu Navigation

* A left-side panel with buttons to switch between different panels using CardLayout.

## Custom UI Utilities

* Common styling methods (in Utilities class) to unify the look and feel (e.g., fonts, colors, buttons, text fields).

# Prerequisites
1. Java 8+ (or a higher version)

2. Oracle Database (or modify the DatabaseConnection class to suit your own DB)

3. JDBC Driver

# Setup and Installation
1. Clone this repository (using Git or GitHub CLI):

```bash 
git clone https://github.com/BigB021/Pizzeria-Management-App.git
cd Pizzeria-Management-App
```

2. Open in your IDE

3. Configure the Database

* Update the connection details in DatabaseConnection.java if necessary.

4. Build and Run

* **Option A (IDE)**: Open the project in your IDE, locate PizzeriaApp.java, and click Run.

* **Option B (Command Line)**:

```bash
cd src
javac *.java
java PizzeriaApp
```
# Usage
1. Launch the Application

2. Login

* Enter a valid username and password stored in your STAFF table (Oracle DB).

* Only users with ROLE = MANAGER or CASHIER can proceed.

3. Navigate the Side Menu

* **Dashboard**: Placeholder for an overview of orders, sales, etc.

* **Categories**: Manage pizza categories or menu sections.

* **Products**: Manage individual menu items, their prices, descriptions, etc.

* **Staff**: Manage employees, roles, and login credentials.

* **Kitchen**: Track order preparation, status updates, etc.

4. Customize

* Update or create additional panels (e.g., ReceiptGeneratorPanel) in the src directory.

* Extend the UI with new features (e.g., reports, analytics, etc.).

# Database Configuration
* By default, the application looks for an Oracle database with:

    * URL: jdbc:oracle:thin:@localhost:1521:XE
    * USER: PAF
    * PASSWORD: PAF

* Adjust these settings in DatabaseConnection.java if you have different credentials or a different DB name.
```java
private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
private static final String USER = "PAF";
private static final String PASSWORD = "PAF";

```
* Ensure your Oracle instance is running and accessible from your local environment.

# License
This project is licensed under MIT LICENSE terms specified in the LICENSE file. Please review it for more information.


# Contributors
Youssef Aitbouddroub 
Khalid Zaikarani