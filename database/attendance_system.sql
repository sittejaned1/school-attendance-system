-- ============================================================
-- File: student_attendance_db.sql
-- Role: Database Engineer
-- Author: sittejaned1
-- Description: Full schema for the Student Attendance System.
--              Run this file in MySQL before launching the app.
-- ============================================================

CREATE DATABASE IF NOT EXISTS student_attendance_db;
USE student_attendance_db;

-- ============================================================
-- TABLE: teachers
-- ============================================================
CREATE TABLE IF NOT EXISTS teachers (
    teacher_id  INT           NOT NULL AUTO_INCREMENT,
    first_name  VARCHAR(100)  NOT NULL,
    last_name   VARCHAR(100)  NOT NULL,
    email       VARCHAR(100)  NULL,
    PRIMARY KEY (teacher_id),
    UNIQUE KEY email (email)
);

-- ============================================================
-- TABLE: classes
-- ============================================================
CREATE TABLE IF NOT EXISTS classes (
    class_id    INT           NOT NULL AUTO_INCREMENT,
    class_name  VARCHAR(100)  NOT NULL,
    grade_level INT(11)       NULL,
    section     VARCHAR(50)   NULL,
    capacity    INT(11)       NULL,
    teacher_id  INT(11)       NULL,
    PRIMARY KEY (class_id),
    CONSTRAINT fk_class_teacher FOREIGN KEY (teacher_id)
        REFERENCES teachers(teacher_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- ============================================================
-- TABLE: students
-- ============================================================
CREATE TABLE IF NOT EXISTS students (
    student_id      INT           NOT NULL AUTO_INCREMENT,
    first_name      VARCHAR(100)  NOT NULL,
    last_name       VARCHAR(100)  NOT NULL,
    email           VARCHAR(100)  NULL,
    enrollment_date DATE          NOT NULL,
    status          VARCHAR(20)   NULL DEFAULT 'active',
    class_id        INT(11)       NULL,
    contact_number  VARCHAR(20)   NULL,
    parent_name     VARCHAR(100)  NULL,
    PRIMARY KEY (student_id),
    UNIQUE KEY email (email),
    KEY class_id (class_id),
    CONSTRAINT fk_student_class FOREIGN KEY (class_id)
        REFERENCES classes(class_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- ============================================================
-- TABLE: attendances
-- ============================================================
CREATE TABLE IF NOT EXISTS attendances (
    attendance_id   INT     NOT NULL AUTO_INCREMENT,
    student_id      INT(11) NOT NULL,
    class_id        INT(11) NOT NULL,
    attendance_date DATE    NOT NULL,
    status          ENUM('Present','Absent','Late','Excused') NULL DEFAULT 'Absent',
    time_in         TIME    NULL,
    remarks         VARCHAR(255) NULL,
    PRIMARY KEY (attendance_id),
    UNIQUE KEY unique_attendance (student_id, class_id, attendance_date),
    KEY class_id (class_id),
    CONSTRAINT fk_att_student FOREIGN KEY (student_id)
        REFERENCES students(student_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_att_class FOREIGN KEY (class_id)
        REFERENCES classes(class_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ============================================================
-- TABLE: reports
-- ============================================================
CREATE TABLE IF NOT EXISTS reports (
    report_id             INT          NOT NULL AUTO_INCREMENT,
    student_id            INT(11)      NOT NULL,
    month                 INT(11)      NULL,
    year                  INT(11)      NULL,
    total_days            INT(11)      NULL,
    present_days          INT(11)      NULL,
    absent_days           INT(11)      NULL,
    late_days             INT(11)      NULL,
    attendance_percentage DECIMAL(5,2) NULL,
    PRIMARY KEY (report_id),
    KEY student_id (student_id),
    CONSTRAINT fk_report_student FOREIGN KEY (student_id)
        REFERENCES students(student_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ============================================================
-- SAMPLE DATA
-- ============================================================
INSERT INTO teachers (first_name, last_name, email) VALUES
('Juan',  'Delacruz', 'juandelacruz@gmail.com');

INSERT INTO classes (class_name, grade_level, section, capacity, teacher_id) VALUES
('Science', 7, 'Diamond', 40, 1);

INSERT INTO students (first_name, last_name, email, enrollment_date, status, class_id, contact_number, parent_name) VALUES
('Sitte Jane', 'Dapdap', 'sittejaned@gmail.com', '2025-07-14', 'active', 1, '09923152012', NULL);

INSERT INTO attendances (student_id, class_id, attendance_date, status, time_in) VALUES
(1, 1, '2026-05-16', 'Present', '09:00:00');

INSERT INTO reports (student_id, month, year, total_days, present_days, absent_days, late_days, attendance_percentage) VALUES
(1, 5, 2026, 1, 1, 0, 0, 100.00);
