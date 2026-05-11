-- Student Attendance System Database Schema
-- Database: student_attendance_db

CREATE DATABASE IF NOT EXISTS student_attendance_db;
USE student_attendance_db;

CREATE TABLE classes (
  class_id INT(11) NOT NULL AUTO_INCREMENT,
  class_name VARCHAR(100) NOT NULL,
  grade_level INT(11) DEFAULT NULL,
  section VARCHAR(5) DEFAULT NULL,
  capacity INT(11) DEFAULT NULL,
  PRIMARY KEY (class_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE students (
  student_id INT(11) NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(100) DEFAULT NULL,
  enrollment_date DATE NOT NULL,
  status VARCHAR(20) DEFAULT 'active',
  class_id INT(11) DEFAULT NULL,
  contact_number VARCHAR(20) DEFAULT NULL,
  parent_name VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (student_id),
  UNIQUE KEY email (email),
  KEY class_id (class_id),
  CONSTRAINT students_ibfk_1 FOREIGN KEY (class_id) REFERENCES classes (class_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE teachers (
  teacher_id INT(11) NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (teacher_id),
  UNIQUE KEY email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE attendances (
  attendance_id INT(11) NOT NULL AUTO_INCREMENT,
  student_id INT(11) NOT NULL,
  class_id INT(11) NOT NULL,
  attendance_date DATE NOT NULL,
  status ENUM('Present','Absent','Late','Excused') DEFAULT 'Absent',
  time_in TIME DEFAULT NULL,
  remarks VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (attendance_id),
  UNIQUE KEY unique_attendance (student_id, class_id, attendance_date),
  KEY class_id (class_id),
  CONSTRAINT attendances_ibfk_1 FOREIGN KEY (student_id) REFERENCES students (student_id),
  CONSTRAINT attendances_ibfk_2 FOREIGN KEY (class_id) REFERENCES classes (class_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE reports (
  report_id INT(11) NOT NULL AUTO_INCREMENT,
  student_id INT(11) NOT NULL,
  month INT(11) DEFAULT NULL,
  year INT(11) DEFAULT NULL,
  total_days INT(11) DEFAULT NULL,
  present_days INT(11) DEFAULT NULL,
  absent_days INT(11) DEFAULT NULL,
  late_days INT(11) DEFAULT NULL,
  attendance_percentage DECIMAL(5,2) DEFAULT NULL,
  PRIMARY KEY (report_id),
  KEY student_id (student_id),
  CONSTRAINT reports_ibfk_1 FOREIGN KEY (student_id) REFERENCES students (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
