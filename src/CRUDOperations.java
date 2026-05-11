import java.sql.*;

public class CRUDOperations {

    // ══════════════════════════════════════════════════════════════════
    //  STUDENTS
    // ══════════════════════════════════════════════════════════════════

    public static void addStudent(String firstName, String lastName, String email,
                                  String enrollmentDate, String status,
                                  Integer classId, String contactNumber, String parentName) {
        String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date, " +
                     "status, class_id, contact_number, parent_name) VALUES (?,?,?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);              // may be null
            pstmt.setString(4, enrollmentDate);
            pstmt.setString(5, status);
            if (classId != null) pstmt.setInt(6, classId);
            else                 pstmt.setNull(6, Types.INTEGER);
            pstmt.setString(7, contactNumber);      // may be null
            pstmt.setString(8, parentName);         // may be null

            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("[OK] Student added successfully.");
            else          System.out.println("[!] No student was added.");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("[ERROR] Duplicate email — a student with that email already exists.");
        } catch (SQLException e) {
            System.out.println("[ERROR] addStudent: " + e.getMessage());
        } finally {
            closeResources(pstmt, null);
            DBConnection.closeConnection(conn);
        }
    }
//READ operation-retrives all students with class info via JOIN
    public static void viewAllStudents() {
        String sql = "SELECT s.student_id, s.first_name, s.last_name, s.email, " +
                     "s.enrollment_date, s.status, c.class_name, s.contact_number, s.parent_name " +
                     "FROM students s LEFT JOIN classes c ON s.class_id = c.class_id " +
                     "ORDER BY s.student_id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return;

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            InputHelper.printSeparator(100);
            System.out.printf("%-5s %-15s %-15s %-25s %-12s %-10s %-15s%n",
                    "ID", "First Name", "Last Name", "Email", "Enrolled", "Status", "Class");
            InputHelper.printSeparator(100);

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-15s %-15s %-25s %-12s %-10s %-15s%n",
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email") != null ? rs.getString("email") : "-",
                        rs.getString("enrollment_date"),
                        rs.getString("status"),
                        rs.getString("class_name") != null ? rs.getString("class_name") : "-");
            }
            if (!found) System.out.println("  No students found.");
            InputHelper.printSeparator(100);

        } catch (SQLException e) {
            System.out.println("[ERROR] viewAllStudents: " + e.getMessage());
        } finally {
            closeResources(pstmt, rs);
            DBConnection.closeConnection(conn);
        }
    }
// Search Operation - finds single student record by primary key
    public static void searchStudentById(int studentId) {
        String sql = "SELECT s.*, c.class_name FROM students s " +
                     "LEFT JOIN classes c ON s.class_id = c.class_id " +
                     "WHERE s.student_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n--- Student Details ---");
                System.out.println("ID           : " + rs.getInt("student_id"));
                System.out.println("Name         : " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("Email        : " + (rs.getString("email") != null ? rs.getString("email") : "-"));
                System.out.println("Enrolled     : " + rs.getString("enrollment_date"));
                System.out.println("Status       : " + rs.getString("status"));
                System.out.println("Class        : " + (rs.getString("class_name") != null ? rs.getString("class_name") : "-"));
                System.out.println("Contact      : " + (rs.getString("contact_number") != null ? rs.getString("contact_number") : "-"));
                System.out.println("Parent/Guardian: " + (rs.getString("parent_name") != null ? rs.getString("parent_name") : "-"));
            } else {
                System.out.println("[!] Student with ID " + studentId + " not found.");
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] searchStudentById: " + e.getMessage());
        } finally {
            closeResources(pstmt, rs);
            DBConnection.closeConnection(conn);
        }
    }
//UPDATE operation- modifies existing student record by ID
    public static void updateStudent(int studentId, String firstName, String lastName,
                                     String email, String status,
                                     Integer classId, String contactNumber, String parentName) {
        String sql = "UPDATE students SET first_name=?, last_name=?, email=?, status=?, " +
                     "class_id=?, contact_number=?, parent_name=? WHERE student_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, status);
            if (classId != null) pstmt.setInt(5, classId);
            else                 pstmt.setNull(5, Types.INTEGER);
            pstmt.setString(6, contactNumber);
            pstmt.setString(7, parentName);
            pstmt.setInt(8, studentId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("[OK] Student updated successfully.");
            else          System.out.println("[!] Student ID " + studentId + " not found.");

        } catch (SQLException e) {
            System.out.println("[ERROR] updateStudent: " + e.getMessage());
        } finally {
            closeResources(pstmt, null);
            DBConnection.closeConnection(conn);
        }
    }
// DELETE operation- removes student record permanently 
   
public static void deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("[OK] Student deleted successfully.");
            else          System.out.println("[!] Student ID " + studentId + " not found.");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("[ERROR] Cannot delete — student has existing attendance records.");
        } catch (SQLException e) {
            System.out.println("[ERROR] deleteStudent: " + e.getMessage());
        } finally {
            closeResources(pstmt, null);
            DBConnection.closeConnection(conn);
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  ATTENDANCE  (includes transaction with commit/rollback)
    // ══════════════════════════════════════════════════════════════════

    public static void markAttendance(int studentId, int classId,
                                      String date, String status,
                                      String timeIn, String remarks) {
        String insertSql = "INSERT INTO attendances " +
                           "(student_id, class_id, attendance_date, status, time_in, remarks) " +
                           "VALUES (?,?,?,?,?,?)";
        String updateReportSql =
                "INSERT INTO reports (student_id, month, year, total_days, present_days, " +
                "absent_days, late_days, attendance_percentage) " +
                "VALUES (?, ?, ?, 1, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "total_days = total_days + 1, " +
                "present_days = present_days + ?, " +
                "absent_days  = absent_days  + ?, " +
                "late_days    = late_days    + ?, " +
                "attendance_percentage = ROUND((present_days / total_days) * 100, 2)";

        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return;

            // ── Begin transaction ──
            conn.setAutoCommit(false);

            // 1) Insert attendance record
            pstmt1 = conn.prepareStatement(insertSql);
            pstmt1.setInt(1, studentId);
            pstmt1.setInt(2, classId);
            pstmt1.setString(3, date);
            pstmt1.setString(4, status);
            pstmt1.setString(5, timeIn);    // may be null
            pstmt1.setString(6, remarks);   // may be null
            pstmt1.executeUpdate();

            // 2) Update monthly report
            int month = Integer.parseInt(date.substring(5, 7));
            int year  = Integer.parseInt(date.substring(0, 4));
            int isPresent = status.equals("Present") ? 1 : 0;
            int isAbsent  = status.equals("Absent")  ? 1 : 0;
            int isLate    = status.equals("Late")     ? 1 : 0;
            double pct    = isPresent * 100.0;

            pstmt2 = conn.prepareStatement(updateReportSql);
            pstmt2.setInt(1, studentId);
            pstmt2.setInt(2, month);
            pstmt2.setInt(3, year);
            pstmt2.setInt(4, isPresent);
            pstmt2.setInt(5, isAbsent);
            pstmt2.setInt(6, isLate);
            pstmt2.setDouble(7, pct);
            // ON DUPLICATE KEY UPDATE params
            pstmt2.setInt(8, isPresent);
            pstmt2.setInt(9, isAbsent);
            pstmt2.setInt(10, isLate);
            pstmt2.executeUpdate();

            // ── Commit transaction ──
            conn.commit();
            System.out.println("[OK] Attendance marked and report updated successfully.");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("[ERROR] Attendance already exists for this student on that date/class.");
            rollback(conn);
        } catch (SQLException e) {
            System.out.println("[ERROR] markAttendance: " + e.getMessage());
            rollback(conn);   // ── Rollback on any failure ──
        } finally {
            closeResources(pstmt1, null);
            closeResources(pstmt2, null);
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ignored) {}
            DBConnection.closeConnection(conn);
        }
    }

    public static void viewAllAttendance() {
        String sql = "SELECT a.attendance_id, s.first_name, s.last_name, c.class_name, " +
                     "a.attendance_date, a.status, a.time_in, a.remarks " +
                     "FROM attendances a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN classes  c ON a.class_id   = c.class_id " +
                     "ORDER BY a.attendance_date DESC, s.last_name";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return;

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            InputHelper.printSeparator(110);
            System.out.printf("%-5s %-20s %-15s %-12s %-10s %-8s %-20s%n",
                    "ID", "Student", "Class", "Date", "Status", "Time In", "Remarks");
            InputHelper.printSeparator(110);

            boolean found = false;
            while (rs.next()) {
                found = true;
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                System.out.printf("%-5d %-20s %-15s %-12s %-10s %-8s %-20s%n",
                        rs.getInt("attendance_id"),
                        name,
                        rs.getString("class_name"),
                        rs.getString("attendance_date"),
                        rs.getString("status"),
                        rs.getString("time_in") != null ? rs.getString("time_in") : "-",
                        rs.getString("remarks") != null ? rs.getString("remarks") : "-");
            }
            if (!found) System.out.println("  No attendance records found.");
            InputHelper.printSeparator(110);

        } catch (SQLException e) {
            System.out.println("[ERROR] viewAllAttendance: " + e.getMessage());
        } finally {
            closeResources(pstmt, rs);
            DBConnection.closeConnection(conn);
        }
    }

    public static void searchAttendanceByStudentId(int studentId) {
        String sql = "SELECT a.attendance_id, a.attendance_date, c.class_name, " +
                     "a.status, a.time_in, a.remarks " +
                     "FROM attendances a " +
                     "JOIN classes c ON a.class_id = c.class_id " +
                     "WHERE a.student_id = ? ORDER BY a.attendance_date DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            rs = pstmt.executeQuery();

            InputHelper.printSeparator(90);
            System.out.printf("%-5s %-12s %-15s %-10s %-8s %-20s%n",
                    "ID", "Date", "Class", "Status", "Time In", "Remarks");
            InputHelper.printSeparator(90);

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-12s %-15s %-10s %-8s %-20s%n",
                        rs.getInt("attendance_id"),
                        rs.getString("attendance_date"),
                        rs.getString("class_name"),
                        rs.getString("status"),
                        rs.getString("time_in") != null ? rs.getString("time_in") : "-",
                        rs.getString("remarks") != null ? rs.getString("remarks") : "-");
            }
            if (!found) System.out.println("  No attendance records for student ID " + studentId);
            InputHelper.printSeparator(90);

        } catch (SQLException e) {
            System.out.println("[ERROR] searchAttendanceByStudentId: " + e.getMessage());
        } finally {
            closeResources(pstmt, rs);
            DBConnection.closeConnection(conn);
        }
    }

    public static void updateAttendance(int attendanceId, String status,
                                        String timeIn, String remarks) {
        String sql = "UPDATE attendances SET status=?, time_in=?, remarks=? WHERE attendance_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setString(2, timeIn);
            pstmt.setString(3, remarks);
            pstmt.setInt(4, attendanceId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("[OK] Attendance record updated.");
            else          System.out.println("[!] Attendance ID " + attendanceId + " not found.");

        } catch (SQLException e) {
            System.out.println("[ERROR] updateAttendance: " + e.getMessage());
        } finally {
            closeResources(pstmt, null);
            DBConnection.closeConnection(conn);
        }
    }

    public static void deleteAttendance(int attendanceId) {
        String sql = "DELETE FROM attendances WHERE attendance_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, attendanceId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("[OK] Attendance record deleted.");
            else          System.out.println("[!] Attendance ID " + attendanceId + " not found.");

        } catch (SQLException e) {
            System.out.println("[ERROR] deleteAttendance: " + e.getMessage());
        } finally {
            closeResources(pstmt, null);
            DBConnection.closeConnection(conn);
        }
    }

// ══════════════════════════════════════════════════════════════════
//  CLASSES
// ══════════════════════════════════════════════════════════════════

// ADD CLASS - inserts new class record into database
public static void addClass(String className, int gradeLevel,
                             String section, int capacity) {
    String sql = "INSERT INTO classes (class_name, grade_level, section, capacity) " +
                 "VALUES (?,?,?,?)";
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
        conn = DBConnection.getConnection();
        if (conn == null) return;

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, className);
        pstmt.setInt(2, gradeLevel);
        pstmt.setString(3, section);
        pstmt.setInt(4, capacity);

        int rows = pstmt.executeUpdate();
        if (rows > 0) System.out.println("[OK] Class added successfully.");
        else          System.out.println("[!] No class was added.");

    } catch (SQLException e) {
        System.out.println("[ERROR] addClass: " + e.getMessage());
    } finally {
        closeResources(pstmt, null);
        DBConnection.closeConnection(conn);
    }
}

public static void viewAllClasses() {
    String sql = "SELECT * FROM classes ORDER BY class_id";
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
        conn = DBConnection.getConnection();
        if (conn == null) return;

        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();

        InputHelper.printSeparator(70);
        System.out.printf("%-5s %-25s %-12s %-10s %-10s%n",
                "ID", "Class Name", "Grade Level", "Section", "Capacity");
        InputHelper.printSeparator(70);

        boolean found = false;
        while (rs.next()) {
            found = true;
            System.out.printf("%-5d %-25s %-12s %-10s %-10s%n",
                    rs.getInt("class_id"),
                    rs.getString("class_name"),
                    rs.getString("grade_level") != null ? rs.getString("grade_level") : "-",
                    rs.getString("section")     != null ? rs.getString("section") : "-",
                    rs.getString("capacity")    != null ? rs.getString("capacity") : "-");
        }
        if (!found) System.out.println("  No classes found.");
        InputHelper.printSeparator(70);

    } catch (SQLException e) {
        System.out.println("[ERROR] viewAllClasses: " + e.getMessage());
    } finally {
        closeResources(pstmt, rs);
        DBConnection.closeConnection(conn);
    }
}

public static void searchClassById(int classId) {
    String sql = "SELECT * FROM classes WHERE class_id = ?";
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
        conn = DBConnection.getConnection();
        if (conn == null) return;

        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, classId);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("\n--- Class Details ---");
            System.out.println("ID         : " + rs.getInt("class_id"));
            System.out.println("Class Name : " + rs.getString("class_name"));
            System.out.println("Grade Level: " + rs.getString("grade_level"));
            System.out.println("Section    : " + rs.getString("section"));
            System.out.println("Capacity   : " + rs.getString("capacity"));
        } else {
            System.out.println("[!] Class ID " + classId + " not found.");
        }

    } catch (SQLException e) {
        System.out.println("[ERROR] searchClassById: " + e.getMessage());
    } finally {
        closeResources(pstmt, rs);
        DBConnection.closeConnection(conn);
    }
}

public static void updateClass(int classId, String className,
                                int gradeLevel, String section, int capacity) {
    String sql = "UPDATE classes SET class_name=?, grade_level=?, " +
                 "section=?, capacity=? WHERE class_id=?";
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
        conn = DBConnection.getConnection();
        if (conn == null) return;

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, className);
        pstmt.setInt(2, gradeLevel);
        pstmt.setString(3, section);
        pstmt.setInt(4, capacity);
        pstmt.setInt(5, classId);

        int rows = pstmt.executeUpdate();
        if (rows > 0) System.out.println("[OK] Class updated successfully.");
        else          System.out.println("[!] Class ID " + classId + " not found.");

    } catch (SQLException e) {
        System.out.println("[ERROR] updateClass: " + e.getMessage());
    } finally {
        closeResources(pstmt, null);
        DBConnection.closeConnection(conn);
    }
}

public static void deleteClass(int classId) {
    String sql = "DELETE FROM classes WHERE class_id = ?";
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
        conn = DBConnection.getConnection();
        if (conn == null) return;

        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, classId);

        int rows = pstmt.executeUpdate();
        if (rows > 0) System.out.println("[OK] Class deleted successfully.");
        else          System.out.println("[!] Class ID " + classId + " not found.");

    } catch (SQLIntegrityConstraintViolationException e) {
        System.out.println("[ERROR] Cannot delete — class has existing students.");
    } catch (SQLException e) {
        System.out.println("[ERROR] deleteClass: " + e.getMessage());
    } finally {
        closeResources(pstmt, null);
        DBConnection.closeConnection(conn);
    }
}
    // ══════════════════════════════════════════════════════════════════
    //  REPORTS  (read-only — auto-generated by markAttendance)
    // ══════════════════════════════════════════════════════════════════

    public static void viewReportByStudent(int studentId) {
        String sql = "SELECT r.*, s.first_name, s.last_name " +
                     "FROM reports r JOIN students s ON r.student_id = s.student_id " +
                     "WHERE r.student_id = ? ORDER BY r.year DESC, r.month DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            rs = pstmt.executeQuery();

            InputHelper.printSeparator(90);
            System.out.printf("%-6s %-6s %-10s %-10s %-10s %-10s %-12s%n",
                    "Month", "Year", "Total", "Present", "Absent", "Late", "Attendance%");
            InputHelper.printSeparator(90);

            boolean found = false;
            while (rs.next()) {
                if (!found) {
                    System.out.println("Report for: " + rs.getString("first_name")
                            + " " + rs.getString("last_name"));
                }
                found = true;
                System.out.printf("%-6d %-6d %-10d %-10d %-10d %-10d %-12.2f%n",
                        rs.getInt("month"),
                        rs.getInt("year"),
                        rs.getInt("total_days"),
                        rs.getInt("present_days"),
                        rs.getInt("absent_days"),
                        rs.getInt("late_days"),
                        rs.getDouble("attendance_percentage"));
            }
            if (!found) System.out.println("  No report found for student ID " + studentId);
            InputHelper.printSeparator(90);

        } catch (SQLException e) {
            System.out.println("[ERROR] viewReportByStudent: " + e.getMessage());
        } finally {
            closeResources(pstmt, rs);
            DBConnection.closeConnection(conn);
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  TEACHERS
    // ══════════════════════════════════════════════════════════════════

    public static void viewAllTeachers() {
        String sql = "SELECT * FROM teachers ORDER BY teacher_id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return;

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            InputHelper.printSeparator(70);
            System.out.printf("%-5s %-20s %-20s %-30s%n",
                    "ID", "First Name", "Last Name", "Email");
            InputHelper.printSeparator(70);

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-20s %-20s %-30s%n",
                        rs.getInt("teacher_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email") != null ? rs.getString("email") : "-");
            }
            if (!found) System.out.println("  No teachers found.");
            InputHelper.printSeparator(70);

        } catch (SQLException e) {
            System.out.println("[ERROR] viewAllTeachers: " + e.getMessage());
        } finally {
            closeResources(pstmt, rs);
            DBConnection.closeConnection(conn);
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  UTILITIES
    // ══════════════════════════════════════════════════════════════════

    private static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
                System.out.println("[INFO] Transaction rolled back — no changes saved.");
            } catch (SQLException ex) {
                System.out.println("[ERROR] Rollback failed: " + ex.getMessage());
            }
        }
    }

    private static void closeResources(PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs    != null) rs.close();
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            System.out.println("[WARN] Failed to close resource: " + e.getMessage());
        }
    }
}
