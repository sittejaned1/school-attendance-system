Student Attendance System
A console-based CRUD application built with pure Java (Procedural) and JDBC, connected to MySQL.
👥 Group Members & Roles
Member	GitHub	Role
sittejaned1	@sittejaned1
Database Engineer
gilchita50-creator	@gilchita50-creator
Backend Logic Developer
Dominosula	@Dominosula
Input & Validation Engineer
mendozadarlene-dotcom	@mendozadarlene-dotcom
System Integrator / Menu Developer
⚙️ Tech Stack
•	Language: Java (Procedural — no OOP, no frameworks)
•	Database: MySQL
•	Connectivity: JDBC with PreparedStatement
🗄️ Database Schema
teachers       → teacher_id, first_name, last_name, email
classes        → class_id, class_name, grade_level, section, capacity, teacher_id (FK)
students       → student_id, first_name, last_name, email, enrollment_date,
                 status, class_id (FK), contact_number, parent_name
attendances    → attendance_id, student_id (FK), class_id (FK),
                 attendance_date, status, time_in, remarks
reports        → report_id, student_id (FK), month, year, total_days,
                 present_days, absent_days, late_days, attendance_percentage
🚀 How to Run
1. Set up the database
Open MySQL and run:
source student_attendance_db.sql
2. Configure the connection
Open DBConnection.java and set your password:
private static final String PASSWORD = "your_password_here";
3. Compile and run
javac -cp ".;lib/mysql-connector-j-9.7.0.jar" src/*.java -d src/
java  -cp ".;lib/mysql-connector-j-9.7.0.jar;src" Main
✅ Features
•	Full CRUD for: Teachers, Classes, Students, Attendances, Reports
•	Search by ID on all 5 tables
•	Transaction-based student delete (commit/rollback)
•	Input validation: dates, emails, contact numbers, status fields
•	Graceful error handling (try-catch-finally)
•	Menu-driven console interface
📁 File Structure
school-attendance-system/
├── src/
│   ├── Main.java             ← Menu system (mendozadarlene-dotcom)
│   ├── DBConnection.java     ← Database connection (sittejaned1)
│   ├── CRUDOperations.java   ← SQL operations (gilchita50-creator)
│   └── InputHelper.java      ← Input validation (Dominosula)
├── database/
│   └── student_attendance_db.sql
├── lib/
│   └── mysql-connector-j-9.7.0.jar
└── README.md

