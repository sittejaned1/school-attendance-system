// ============================================================
// File: CRUDOperations.java
// Role: Backend Logic Developer
// Author: gilchita50-creator
// Description: Implements full CRUD operations using JDBC
//              PreparedStatements for all 5 tables in the
//              Student Attendance System.
//              Includes transaction-based delete (commit/rollback).
// ============================================================

mport java.sql.*;

public class CRUDOperations {

    // ============================================================
    // ===================== TEACHER CRUD =========================
    // ============================================================

    public static void addTeacher() {
        String firstName = InputHelper.readString("First name     : ");
        String lastName  = InputHelper.readString("Last name      : ");
        String email     = InputHelper.readEmail("Email (optional): ");

        String sql = "INSERT INTO teachers (first_name, last_name, email) VALUES (?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, email.isEmpty() ? null : email);
            pst.executeUpdate();
            System.out.println("[✓] Teacher added successfully.");
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to add teacher: " + e.getMessage());
        }
    }
// READ operations-retrives all students with class info via JOIN 
    public static void viewAllTeachers() {
        String sql = "SELECT * FROM teachers ORDER BY last_name, first_name";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("\n" + "=".repeat(65));
            System.out.printf("%-5s %-20s %-20s %-25s%n", "ID", "First Name", "Last Name", "Email");
            System.out.println("=".repeat(65));
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-20s %-20 %-25s%n",
                    rs.getInt("teacher_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email") != null ? rs.getString("email") : "N/A");
            }
            if (!found) System.out.println("  No teacher records found.");
            System.out.println("=".repeat(65));
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void searchTeacherById() {
        int id = InputHelper.readInt("Enter Teacher ID: ");
        String sql = "SELECT * FROM teachers WHERE teacher_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\n--- Teacher Found ---");
                    System.out.println("ID         : " + rs.getInt("teacher_id"));
                    System.out.println("Name       : " + rs.getString("first_name") + " " + rs.getString("last_name"));
                    System.out.println("Email      : " + (rs.getString("email") != null ? rs.getString("email") : "N/A"));
                } else {
                    System.out.println("[!] No teacher found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void updateTeacher() {
        int id = InputHelper.readInt("Enter Teacher ID to update: ");
        if (!recordExists("teachers", "teacher_id", id)) { System.out.println("[!] Teacher not found."); return; }

        System.out.println("(Press Enter to keep current value)");
        String firstName = InputHelper.readOptionalString("New first name : ");
        String lastName  = InputHelper.readOptionalString("New last name  : ");
        String email     = InputHelper.readOptionalString("New email      : ");

        if (firstName.isEmpty() && lastName.isEmpty() && email.isEmpty()) {
            System.out.println("[!] No changes made.");
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE teachers SET ");
        if (!firstName.isEmpty()) sql.append("first_name = ?, ");
        if (!lastName.isEmpty())  sql.append("last_name = ?, ");
        if (!email.isEmpty())     sql.append("email = ?, ");
        String query = sql.toString().replaceAll(", $", "") + " WHERE teacher_id = ?";

        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            int idx = 1;
            if (!firstName.isEmpty()) pst.setString(idx++, firstName);
            if (!lastName.isEmpty())  pst.setString(idx++, lastName);
            if (!email.isEmpty())     pst.setString(idx++, email);
            pst.setInt(idx, id);
            pst.executeUpdate();
            System.out.println("[✓] Teacher updated successfully.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void deleteTeacher() {
        int id = InputHelper.readInt("Enter Teacher ID to delete: ");
        if (!recordExists("teachers", "teacher_id", id)) { System.out.println("[!] Teacher not found."); return; }
        if (!InputHelper.confirm("Delete teacher ID " + id + "?")) { System.out.println("[!] Cancelled."); return; }

        String sql = "DELETE FROM teachers WHERE teacher_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("[✓] Teacher deleted.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ============================================================
    // ====================== CLASS CRUD ==========================
    // ============================================================

    public static void addClass() {
        String className = InputHelper.readString("Class name     : ");
        int gradeLevel   = InputHelper.readInt("Grade level    : ");
        String section   = InputHelper.readString("Section        : ");
        int capacity     = InputHelper.readInt("Capacity       : ");
        int teacherId    = InputHelper.readInt("Teacher ID     : ");

        if (!recordExists("teachers", "teacher_id", teacherId)) {
            System.out.println("[!] Teacher ID " + teacherId + " does not exist.");
            return;
        }

        String sql = "INSERT INTO classes (class_name, grade_level, section, capacity, teacher_id) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, className);
            pst.setInt(2, gradeLevel);
            pst.setString(3, section);
            pst.setInt(4, capacity);
            pst.setInt(5, teacherId);
            pst.executeUpdate();
            System.out.println("[✓] Class added successfully.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void viewAllClasses() {
        String sql = "SELECT c.*, CONCAT(t.first_name, ' ', t.last_name) AS teacher_name " +
                     "FROM classes c LEFT JOIN teachers t ON c.teacher_id = t.teacher_id " +
                     "ORDER BY c.grade_level, c.section";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("\n" + "=".repeat(75));
            System.out.printf("%-5s %-20s %-7s %-12s %-8s %-20s%n",
                "ID", "Class Name", "Grade", "Section", "Cap", "Teacher");
            System.out.println("=".repeat(75));
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-20s %-7d %-12s %-8d %-20s%n",
                    rs.getInt("class_id"),
                    rs.getString("class_name"),
                    rs.getInt("grade_level"),
                    rs.getString("section"),
                    rs.getInt("capacity"),
                    rs.getString("teacher_name") != null ? rs.getString("teacher_name") : "N/A");
            }
            if (!found) System.out.println("  No class records found.");
            System.out.println("=".repeat(75));
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void searchClassById() {
        int id = InputHelper.readInt("Enter Class ID: ");
        String sql = "SELECT c.*, CONCAT(t.first_name, ' ', t.last_name) AS teacher_name " +
                     "FROM classes c LEFT JOIN teachers t ON c.teacher_id = t.teacher_id " +
                     "WHERE c.class_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\n--- Class Found ---");
                    System.out.println("ID          : " + rs.getInt("class_id"));
                    System.out.println("Class Name  : " + rs.getString("class_name"));
                    System.out.println("Grade Level : " + rs.getInt("grade_level"));
                    System.out.println("Section     : " + rs.getString("section"));
                    System.out.println("Capacity    : " + rs.getInt("capacity"));
                    System.out.println("Teacher     : " + (rs.getString("teacher_name") != null ? rs.getString("teacher_name") : "N/A"));
                } else {
                    System.out.println("[!] No class found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void updateClass() {
        int id = InputHelper.readInt("Enter Class ID to update: ");
        if (!recordExists("classes", "class_id", id)) { System.out.println("[!] Class not found."); return; }

        System.out.println("(Press Enter to keep current value)");
        String className = InputHelper.readOptionalString("New class name : ");
        String section   = InputHelper.readOptionalString("New section    : ");

        if (className.isEmpty() && section.isEmpty()) {
            System.out.println("[!] No changes made.");
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE classes SET ");
        if (!className.isEmpty()) sql.append("class_name = ?, ");
        if (!section.isEmpty())   sql.append("section = ?, ");
        String query = sql.toString().replaceAll(", $", "") + " WHERE class_id = ?";

        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            int idx = 1;
            if (!className.isEmpty()) pst.setString(idx++, className);
            if (!section.isEmpty())   pst.setString(idx++, section);
            pst.setInt(idx, id);
            pst.executeUpdate();
            System.out.println("[✓] Class updated successfully.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void deleteClass() {
        int id = InputHelper.readInt("Enter Class ID to delete: ");
        if (!recordExists("classes", "class_id", id)) { System.out.println("[!] Class not found."); return; }
        if (!InputHelper.confirm("Delete class ID " + id + "?")) { System.out.println("[!] Cancelled."); return; }

        String sql = "DELETE FROM classes WHERE class_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("[✓] Class deleted.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ============================================================
    // ===================== STUDENT CRUD =========================
    // ============================================================

    public static void addStudent() {
        String firstName      = InputHelper.readString("First name        : ");
        String lastName       = InputHelper.readString("Last name         : ");
        String email          = InputHelper.readEmail("Email (optional)  : ");
        String enrollmentDate = InputHelper.readDate("Enrollment date (yyyy-MM-dd): ");
        String status         = InputHelper.readStudentStatus("Status (active/inactive)    : ");
        int classId           = InputHelper.readInt("Class ID          : ");
        String contactNumber  = InputHelper.readContactNumber("Contact # (09xxxxxxxxx, optional): ");
        String parentName     = InputHelper.readOptionalString("Parent name (optional): ");

        if (!recordExists("classes", "class_id", classId)) {
            System.out.println("[!] Class ID " + classId + " does not exist.");
            return;
        }

        String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date, status, class_id, contact_number, parent_name) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, email.isEmpty() ? null : email);
            pst.setString(4, enrollmentDate);
            pst.setString(5, status);
            pst.setInt(6, classId);
            pst.setString(7, contactNumber.isEmpty() ? null : contactNumber);
            pst.setString(8, parentName.isEmpty() ? null : parentName);
            pst.executeUpdate();
            System.out.println("[✓] Student added successfully.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void viewAllStudents() {
        String sql = "SELECT s.*, c.class_name, c.section FROM students s " +
                     "LEFT JOIN classes c ON s.class_id = c.class_id " +
                     "ORDER BY s.last_name, s.first_name";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("\n" + "=".repeat(85));
            System.out.printf("%-5s %-18s %-18s %-10s %-15s %-10s%n",
                "ID", "First Name", "Last Name", "Status", "Class", "Section");
            System.out.println("=".repeat(85));
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-18s %-18s %-10s %-15s %-10s%n",
                    rs.getInt("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("status"),
                    rs.getString("class_name") != null ? rs.getString("class_name") : "N/A",
                    rs.getString("section") != null ? rs.getString("section") : "N/A");
            }
            if (!found) System.out.println("  No student records found.");
            System.out.println("=".repeat(85));
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void searchStudentById() {
        int id = InputHelper.readInt("Enter Student ID: ");
        String sql = "SELECT s.*, c.class_name, c.section FROM students s " +
                     "LEFT JOIN classes c ON s.class_id = c.class_id WHERE s.student_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\n--- Student Found ---");
                    System.out.println("ID             : " + rs.getInt("student_id"));
                    System.out.println("Name           : " + rs.getString("first_name") + " " + rs.getString("last_name"));
                    System.out.println("Email          : " + (rs.getString("email") != null ? rs.getString("email") : "N/A"));
                    System.out.println("Enrolled       : " + rs.getString("enrollment_date"));
                    System.out.println("Status         : " + rs.getString("status"));
                    System.out.println("Class          : " + (rs.getString("class_name") != null ? rs.getString("class_name") : "N/A"));
                    System.out.println("Section        : " + (rs.getString("section") != null ? rs.getString("section") : "N/A"));
                    System.out.println("Contact #      : " + (rs.getString("contact_number") != null ? rs.getString("contact_number") : "N/A"));
                    System.out.println("Parent Name    : " + (rs.getString("parent_name") != null ? rs.getString("parent_name") : "N/A"));
                } else {
                    System.out.println("[!] No student found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void updateStudent() {
        int id = InputHelper.readInt("Enter Student ID to update: ");
        if (!recordExists("students", "student_id", id)) { System.out.println("[!] Student not found."); return; }

        System.out.println("(Press Enter to keep current value)");
        String firstName     = InputHelper.readOptionalString("New first name    : ");
        String lastName      = InputHelper.readOptionalString("New last name     : ");
        String status        = InputHelper.readOptionalString("New status (active/inactive): ");
        String contactNumber = InputHelper.readOptionalString("New contact #     : ");
        String parentName    = InputHelper.readOptionalString("New parent name   : ");

        if (firstName.isEmpty() && lastName.isEmpty() && status.isEmpty() &&
            contactNumber.isEmpty() && parentName.isEmpty()) {
            System.out.println("[!] No changes made.");
            return;
        }

        // Validate status if provided
        if (!status.isEmpty() && !status.equalsIgnoreCase("active") && !status.equalsIgnoreCase("inactive")) {
            System.out.println("[!] Invalid status. Must be active or inactive.");
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE students SET ");
        if (!firstName.isEmpty())     sql.append("first_name = ?, ");
        if (!lastName.isEmpty())      sql.append("last_name = ?, ");
        if (!status.isEmpty())        sql.append("status = ?, ");
        if (!contactNumber.isEmpty()) sql.append("contact_number = ?, ");
        if (!parentName.isEmpty())    sql.append("parent_name = ?, ");
        String query = sql.toString().replaceAll(", $", "") + " WHERE student_id = ?";

        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            int idx = 1;
            if (!firstName.isEmpty())     pst.setString(idx++, firstName);
            if (!lastName.isEmpty())      pst.setString(idx++, lastName);
            if (!status.isEmpty())        pst.setString(idx++, status.toLowerCase());
            if (!contactNumber.isEmpty()) pst.setString(idx++, contactNumber);
            if (!parentName.isEmpty())    pst.setString(idx++, parentName);
            pst.setInt(idx, id);
            pst.executeUpdate();
            System.out.println("[✓] Student updated successfully.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // --------------------------------------------------------
    // deleteStudent()
    // TRANSACTION: deletes attendances + reports + student.
    // Uses commit/rollback to ensure all-or-nothing deletion.
    // --------------------------------------------------------
    public static void deleteStudent() {
        int id = InputHelper.readInt("Enter Student ID to delete: ");
        if (!recordExists("students", "student_id", id)) { System.out.println("[!] Student not found."); return; }
        if (!InputHelper.confirm("Delete student ID " + id + " and ALL their records?")) {
            System.out.println("[!] Cancelled.");
            return;
        }

        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try {
            conn.setAutoCommit(false); // BEGIN TRANSACTION

            // Step 1: Delete reports
            try (PreparedStatement p1 = conn.prepareStatement("DELETE FROM reports WHERE student_id = ?")) {
                p1.setInt(1, id); p1.executeUpdate();
            }
            // Step 2: Delete attendances
            try (PreparedStatement p2 = conn.prepareStatement("DELETE FROM attendances WHERE student_id = ?")) {
                p2.setInt(1, id); p2.executeUpdate();
            }
            // Step 3: Delete student
            try (PreparedStatement p3 = conn.prepareStatement("DELETE FROM students WHERE student_id = ?")) {
                p3.setInt(1, id); p3.executeUpdate();
            }

            conn.commit(); // COMMIT
            System.out.println("[✓] Student and all related records deleted successfully.");

        } catch (SQLException e) {
            try {
                conn.rollback(); // ROLLBACK on error
                System.out.println("[!] Transaction rolled back. No data was deleted.");
            } catch (SQLException re) {
                System.out.println("[ERROR] Rollback failed: " + re.getMessage());
            }
            System.out.println("[ERROR] Delete failed: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { /* ignore */ }
        }
    }

    // ============================================================
    // =================== ATTENDANCE CRUD ========================
    // ============================================================

    public static void addAttendance() {
        int studentId    = InputHelper.readInt("Student ID      : ");
        if (!recordExists("students", "student_id", studentId)) { System.out.println("[!] Student not found."); return; }
        int classId      = InputHelper.readInt("Class ID        : ");
        if (!recordExists("classes", "class_id", classId)) { System.out.println("[!] Class not found."); return; }
        String date      = InputHelper.readDate("Date (yyyy-MM-dd): ");
        String status    = InputHelper.readAttendanceStatus("Status (Present/Absent/Late/Excused): ");
        String timeIn    = InputHelper.readTimeIn("Time in (HH:mm:ss, Enter to skip): ");
        String remarks   = InputHelper.readOptionalString("Remarks (optional): ");

        String sql = "INSERT INTO attendances (student_id, class_id, attendance_date, status, time_in, remarks) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, studentId);
            pst.setInt(2, classId);
            pst.setString(3, date);
            pst.setString(4, status);
            pst.setString(5, timeIn);
            pst.setString(6, remarks.isEmpty() ? null : remarks);
            pst.executeUpdate();
            System.out.println("[✓] Attendance recorded successfully.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void viewAllAttendances() {
        String sql = "SELECT a.*, CONCAT(s.first_name,' ',s.last_name) AS student_name, c.class_name " +
                     "FROM attendances a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN classes c ON a.class_id = c.class_id " +
                     "ORDER BY a.attendance_date DESC";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("\n" + "=".repeat(85));
            System.out.printf("%-5s %-22s %-15s %-12s %-10s %-10s%n",
                "ID", "Student", "Class", "Date", "Status", "Time In");
            System.out.println("=".repeat(85));
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-22s %-15s %-12s %-10s %-10s%n",
                    rs.getInt("attendance_id"),
                    rs.getString("student_name"),
                    rs.getString("class_name"),
                    rs.getString("attendance_date"),
                    rs.getString("status"),
                    rs.getString("time_in") != null ? rs.getString("time_in") : "N/A");
            }
            if (!found) System.out.println("  No attendance records found.");
            System.out.println("=".repeat(85));
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void searchAttendanceById() {
        int id = InputHelper.readInt("Enter Attendance ID: ");
        String sql = "SELECT a.*, CONCAT(s.first_name,' ',s.last_name) AS student_name, c.class_name " +
                     "FROM attendances a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN classes c ON a.class_id = c.class_id " +
                     "WHERE a.attendance_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\n--- Attendance Record ---");
                    System.out.println("ID       : " + rs.getInt("attendance_id"));
                    System.out.println("Student  : " + rs.getString("student_name"));
                    System.out.println("Class    : " + rs.getString("class_name"));
                    System.out.println("Date     : " + rs.getString("attendance_date"));
                    System.out.println("Status   : " + rs.getString("status"));
                    System.out.println("Time In  : " + (rs.getString("time_in") != null ? rs.getString("time_in") : "N/A"));
                    System.out.println("Remarks  : " + (rs.getString("remarks") != null ? rs.getString("remarks") : "N/A"));
                } else {
                    System.out.println("[!] No record found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void updateAttendance() {
        int id = InputHelper.readInt("Enter Attendance ID to update: ");
        if (!recordExists("attendances", "attendance_id", id)) { System.out.println("[!] Record not found."); return; }

        String newStatus  = InputHelper.readAttendanceStatus("New status (Present/Absent/Late/Excused): ");
        String newTimeIn  = InputHelper.readTimeIn("New time in (HH:mm:ss, Enter to skip): ");
        String newRemarks = InputHelper.readOptionalString("New remarks (Enter to skip): ");

        String sql = "UPDATE attendances SET status = ?, time_in = ?, remarks = ? WHERE attendance_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, newStatus);
            pst.setString(2, newTimeIn);
            pst.setString(3, newRemarks.isEmpty() ? null : newRemarks);
            pst.setInt(4, id);
            pst.executeUpdate();
            System.out.println("[✓] Attendance updated successfully.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void deleteAttendance() {
        int id = InputHelper.readInt("Enter Attendance ID to delete: ");
        if (!recordExists("attendances", "attendance_id", id)) { System.out.println("[!] Record not found."); return; }
        if (!InputHelper.confirm("Delete attendance record ID " + id + "?")) { System.out.println("[!] Cancelled."); return; }

        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement("DELETE FROM attendances WHERE attendance_id = ?")) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("[✓] Attendance record deleted.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ============================================================
    // ====================== REPORT CRUD =========================
    // ============================================================

    public static void addReport() {
        int studentId  = InputHelper.readInt("Student ID          : ");
        if (!recordExists("students", "student_id", studentId)) { System.out.println("[!] Student not found."); return; }
        int month      = InputHelper.readMonth("Month (1-12)        : ");
        int year       = InputHelper.readYear("Year (e.g. 2026)    : ");
        int totalDays  = InputHelper.readInt("Total days          : ");
        int presentDays = InputHelper.readIntAllowZero("Present days        : ");
        int absentDays  = InputHelper.readIntAllowZero("Absent days         : ");
        int lateDays    = InputHelper.readIntAllowZero("Late days           : ");

        double percentage = totalDays > 0 ? ((double) presentDays / totalDays) * 100 : 0;

        String sql = "INSERT INTO reports (student_id, month, year, total_days, present_days, absent_days, late_days, attendance_percentage) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, studentId);
            pst.setInt(2, month);
            pst.setInt(3, year);
            pst.setInt(4, totalDays);
            pst.setInt(5, presentDays);
            pst.setInt(6, absentDays);
            pst.setInt(7, lateDays);
            pst.setDouble(8, percentage);
            pst.executeUpdate();
            System.out.printf("[✓] Report added. Attendance: %.2f%%%n", percentage);
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void viewAllReports() {
        String sql = "SELECT r.*, CONCAT(s.first_name,' ',s.last_name) AS student_name " +
                     "FROM reports r JOIN students s ON r.student_id = s.student_id " +
                     "ORDER BY r.year DESC, r.month DESC";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("\n" + "=".repeat(90));
            System.out.printf("%-5s %-22s %-6s %-5s %-7s %-8s %-7s %-6s %-10s%n",
                "ID", "Student", "Month", "Year", "Total", "Present", "Absent", "Late", "Percentage");
            System.out.println("=".repeat(90));
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-22s %-6d %-5d %-7d %-8d %-7d %-6d %-10.2f%%%n",
                    rs.getInt("report_id"),
                    rs.getString("student_name"),
                    rs.getInt("month"),
                    rs.getInt("year"),
                    rs.getInt("total_days"),
                    rs.getInt("present_days"),
                    rs.getInt("absent_days"),
                    rs.getInt("late_days"),
                    rs.getDouble("attendance_percentage"));
            }
            if (!found) System.out.println("  No report records found.");
            System.out.println("=".repeat(90));
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void searchReportById() {
        int id = InputHelper.readInt("Enter Report ID: ");
        String sql = "SELECT r.*, CONCAT(s.first_name,' ',s.last_name) AS student_name " +
                     "FROM reports r JOIN students s ON r.student_id = s.student_id " +
                     "WHERE r.report_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\n--- Report Found ---");
                    System.out.println("Report ID    : " + rs.getInt("report_id"));
                    System.out.println("Student      : " + rs.getString("student_name"));
                    System.out.println("Period       : Month " + rs.getInt("month") + " / " + rs.getInt("year"));
                    System.out.println("Total Days   : " + rs.getInt("total_days"));
                    System.out.println("Present      : " + rs.getInt("present_days"));
                    System.out.println("Absent       : " + rs.getInt("absent_days"));
                    System.out.println("Late         : " + rs.getInt("late_days"));
                    System.out.printf("Percentage   : %.2f%%%n", rs.getDouble("attendance_percentage"));
                } else {
                    System.out.println("[!] No report found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void updateReport() {
        int id         = InputHelper.readInt("Enter Report ID to update: ");
        if (!recordExists("reports", "report_id", id)) { System.out.println("[!] Report not found."); return; }
        int totalDays  = InputHelper.readInt("New total days   : ");
        int presentDays = InputHelper.readIntAllowZero("New present days : ");
        int absentDays  = InputHelper.readIntAllowZero("New absent days  : ");
        int lateDays    = InputHelper.readIntAllowZero("New late days    : ");
        double percentage = totalDays > 0 ? ((double) presentDays / totalDays) * 100 : 0;

        String sql = "UPDATE reports SET total_days=?, present_days=?, absent_days=?, late_days=?, attendance_percentage=? WHERE report_id=?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, totalDays);
            pst.setInt(2, presentDays);
            pst.setInt(3, absentDays);
            pst.setInt(4, lateDays);
            pst.setDouble(5, percentage);
            pst.setInt(6, id);
            pst.executeUpdate();
            System.out.printf("[✓] Report updated. Attendance: %.2f%%%n", percentage);
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void deleteReport() {
        int id = InputHelper.readInt("Enter Report ID to delete: ");
        if (!recordExists("reports", "report_id", id)) { System.out.println("[!] Report not found."); return; }
        if (!InputHelper.confirm("Delete report ID " + id + "?")) { System.out.println("[!] Cancelled."); return; }

        Connection conn = DBConnection.getConnection();
        if (conn == null) { System.out.println("[ERROR] No database connection."); return; }

        try (PreparedStatement pst = conn.prepareStatement("DELETE FROM reports WHERE report_id = ?")) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("[✓] Report deleted.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ============================================================
    // ==================== HELPER METHOD =========================
    // ============================================================

    // --------------------------------------------------------
    // recordExists(table, column, id)
    // Reusable existence check for any table and ID column.
    // --------------------------------------------------------
    private static boolean recordExists(String table, String column, int id) {
        String sql = "SELECT 1 FROM " + table + " WHERE " + column + " = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
