// ============================================================
// Author: sittejaned1
// DBConnection.java - Developed by: Sitte Jane
// Role: Database Engineer
// Description: Manages MySQL JDBC connection for the
//              Student Attendance System.
// ============================================================

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/student_attendance_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "";  // change to your MySQL password

    private static Connection connection = null;

    // -------------------------------------------------------
    // getConnection()
    // Returns an active Connection, creating one if needed.
    // -------------------------------------------------------
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] Connection established successfully.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("[DB ERROR] MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Failed to connect: " + e.getMessage());
        }
        return connection;
    }

    // --------------------------------------------------------
    // closeConnection()
    // Safely closes the active database connection.
    // --------------------------------------------------------
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("[DB] Connection closed.");
                }
            } catch (SQLException e) {
                System.out.println("[DB ERROR] Failed to close connection: " + e.getMessage());
            }
        }
    }

    // -------------------------------------------------------
    // testConnection()
    // Verifies connectivity on startup.
    // -------------------------------------------------------
    public static boolean testConnection() {
        return getConnection() != null;
    }
}
