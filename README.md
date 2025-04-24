
# 🎬 Movie Ticket Booking System

## Team Members
1. Inshika Gupta  
2. Shalini Kumara  
3. Manisha Sharma  

---

## 📖 Overview
A Java-based Movie Ticket Booking System with a graphical interface built using **Swing**, and database integration using **JDBC with MySQL**. This project allows users to **register**, **log in**, **browse movies**, **book tickets**, and **make payments**—all through a user-friendly GUI.

---

## 🚀 Features
✅ User Registration & Login  
✅ Movie Listing and Booking  
✅ Seat Selection Interface  
✅ Card Payment Integration  
✅ Receipt Generation  
✅ Form Validation & Password Strength Indicator  

---

## 🛠️ Technologies Used
- **JDBC**
- **Java Swing** – For the GUI components  
- **MySQL** – For database connectivity  
- **Object-Oriented Programming (OOP)** – Clean modular design  
- **Multithreading** – For smooth GUI updates and database interactions  

---

## 📂 Project Structure
```
📦 MovieBookingSystem
 ┣ 📂 Source Packages
 ┃ ┗ 📂 <default package>
 ┃   ┣ 📄 WelcomeForm.java           // Entry screen
 ┃   ┣ 📄 LoginForm.java             // Login screen
 ┃   ┣ 📄 RegisterForm.java          // New user registration
 ┃   ┣ 📄 BookingForm.java           // Movie and seat selection
 ┃   ┣ 📄 CardPaymentForm.java       // Card payment processing
 ┃   ┣ 📄 Receipt.java               // Booking receipt display
 ┃   ┣ 📄 Database.java              // MySQL connection and queries
 ┃   ┣ 📄 DatabaseUtil.java          // DB utility methods
 ┃   ┣ 📄 Server.java                // Server-side communication
 ┃   ┣ 📄 Client.java                // Client-side logic
 ┃   ┣ 📄 JCalendar.class            // Calendar picker component
 ┃   ┣ 📄 TestDB.java                // Database connection testing
 ┃   ┗ 📄 tempCodeRunnerFile.java    // Temporary runner (for testing)
 ┣ 📂 moviebookingsystem
 ┣ 📄 README.md
```

---

## ⚙️ Setup Instructions

### 1️⃣ Database Setup
1. Install and start **MySQL Server**.  
2. Create a database named `moviebookingsystem`.  
3. Run the SQL script (not provided here) to create necessary tables (like users, bookings, payments).  

### 2️⃣ Configure Database Connection
Edit `Database.java` to set:
```java
String url = "jdbc:mysql://localhost:3306/moviebookingsystem";
String user = "yourUsername";
String password = "yourPassword";
```

### 3️⃣ Compile & Run the Project
Use any Java IDE (like NetBeans or IntelliJ IDEA):
- Open the project folder.
- Ensure your **JDK** and **MySQL JDBC Driver** are correctly configured.
- Start by running `WelcomeForm.java`.

---

## 📌 Main Classes Explained
- **WelcomeForm.java** – Entry point GUI with options for Login/Register.  
- **LoginForm.java** – Authenticates user credentials.  
- **RegisterForm.java** – Allows new users to sign up with form validations.  
- **BookingForm.java** – Displays movie listings and handles seat selection.  
- **CardPaymentForm.java** – Processes card details securely.  
- **Receipt.java** – Generates a receipt after booking.  
- **Database.java / DatabaseUtil.java** – Handle database connections and queries.  
- **Server.java / Client.java** – For any networking-related future extensions.  

---

## 💡 Future Improvements
- Admin Panel to Manage Movies and Bookings  
- Real-time Seat Availability Update  

---
