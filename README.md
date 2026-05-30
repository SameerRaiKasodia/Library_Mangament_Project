# Library Management System

A console-based Library Management System built with Java and MySQL for understand OOPs concept and implementation in project.

## Features
- Add, view, and search books
- Register members (Student & Faculty)
- Borrow books with automatic due dates
  - Student: 14 days, max 3 books
  - Faculty: 30 days, max 5 books
- Return books with automatic fine calculation
  - ₹2 per day for overdue books
- View transaction history

## OOP Concepts Used
- Encapsulation
- Polymorphism
- Singleton Pattern
- Separation of Concerns (Model-DAO-Service)

## Tech Stack
- Java
- MySQL
- JDBC

## Project Structure
- model/ - Book, Member, Transaction classes
- dao/ - Database operations
- service/ - Business logic
- util/ - Database connection (Singleton)

## Setup
1. Create MySQL database: library_db
2. Create tables using SQL below
3. Add MySQL Connector JAR to classpath
4. Create db.properties in src/ with:
   db.url=jdbc:mysql://localhost:3306/library_db
   db.username=root
   db.password=your_password
5. Run LibraryApp.java

## Author
Sameer Rai Kasodia
