# Student Attendance System

A console-based CRUD application built with Java and MySQL (JDBC).

## Group Members
- sittejaned1 - Database Engineer
- Dominosula - Input & Validation Engineer
- gilchita50-creator - Backend Logic Developer
- mendozadarlene-dotcom - Error Handling & Menu Developer

## System Features
- Add, View, Search, Update, Delete Students
- Mark, View, Update, Delete Attendance
- Add, View, Search, Update, Delete Classes
- View Teachers
- View Attendance Reports


## How to Run
1. Make sure MySQL/XAMPP is running
2. Open terminal in project folder
3. Compile: javac -cp "lib\mysql-connector-j-9.7.0.jar;out" src\*.java -d out
4. Run: java -cp "lib\mysql-connector-j-9.7.0.jar;out" src\*.java -d out
5. Run: java -cp "lib\mysql-connector-j-9.7.0.jar;out" Main

## Database
- Database name: student_attendance_db
- Tables: students, classes, attendances, teachers, reports

## Technologies Used
- Java (Procedural)
- MySQL
- JDBC with PreparedStatement
- Transaction Management (commit/rollback)
- GitHub for version control

## Project Structure
- src/DBConnection.java - Database connection handler
- src/CRUDOperations.java - All SQL CRUD operations
- src/InputHelper.java - Input validation and formatting
- src/Main.java - Menu system and program entry point
- database/attendance_system.sql - Database schema
- lib/mysql-connector-j-9.7.0.jar - MySQL JDBC driver
