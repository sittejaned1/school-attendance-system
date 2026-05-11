import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/student_attendance_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "";          // change if you have a password

    // Returns a live Connection, or null on failure
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("[ERROR] MySQL JDBC Driver not found.");
            System.out.println("        Make sure mysql-connector-j-*.jar is in your classpath.");
        } catch (SQLException e) {
            System.out.println("[ERROR] Cannot connect to database: " + e.getMessage());
        }
        return conn;
    }

    // Call this at startup so the program fails early if DB is unreachable
    public static boolean testConnection() {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("[OK] Database connected successfully.");
            closeConnection(conn);
            return true;
        }
        return false;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("[WARN] Failed to close connection: " + e.getMessage());
            }
        }
    }
}