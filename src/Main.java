// ============================================================

// ============================================================

public class Main {

    public static void main(String[] args) {
        printBanner();

        if (!DBConnection.testConnection()) {
            System.out.println("[FATAL] Cannot connect to database. Please check:");
            System.out.println("        - Is MySQL running?");
            System.out.println("        - Does 'student_attendance_db' exist?");
            System.out.println("        - Are credentials correct in DBConnection.java?");
            return;
        }

        showMainMenu();
        DBConnection.closeConnection();
        System.out.println("\nThank you for using the Student Attendance System. Goodbye!");
    }

    // --------------------------------------------------------
    // showMainMenu()
    // Top-level menu routing to each module.
    // --------------------------------------------------------
    private static void showMainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔═══════════════════════════════════════════╗");
            System.out.println("║           STUDENT ATTENDANCE SYSTEM       ║");
            System.out.println("╠═══════════════════════════════════════════╣");
            System.out.println("║  [1] Teacher Management                   ║");
            System.out.println("║  [2] Class Management                     ║");
            System.out.println("║  [3] Student Management                   ║");
            System.out.println("║  [4] Attendance Management                ║");
            System.out.println("║  [5] Report Management                    ║");
            System.out.println("║  [0] Exit                                 ║");
            System.out.println("╚══════════════════════════════════════════╝");

            int choice = InputHelper.readMenuChoice("Select option: ", 0, 5);
            switch (choice) {
                case 1: showTeacherMenu();    break;
                case 2: showClassMenu();      break;
                case 3: showStudentMenu();    break;
                case 4: showAttendanceMenu(); break;
                case 5: showReportMenu();     break;
                case 0: running = false;      break;
            }
        }
    }

    // --------------------------------------------------------
    // showTeacherMenu()
    // --------------------------------------------------------
    private static void showTeacherMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n┌──────────────────────────────────┐");
            System.out.println("│        TEACHER MANAGEMENT         │");
            System.out.println("├──────────────────────────────────┤");
            System.out.println("│  [1] Add New Teacher              │");
            System.out.println("│  [2] View All Teachers            │");
            System.out.println("│  [3] Search Teacher by ID         │");
            System.out.println("│  [4] Update Teacher               │");
            System.out.println("│  [5] Delete Teacher               │");
            System.out.println("│  [0] Back                         │");
            System.out.println("└──────────────────────────────────┘");

            int choice = InputHelper.readMenuChoice("Select option: ", 0, 5);
            switch (choice) {
                case 1: CRUDOperations.addTeacher();       break;
                case 2: CRUDOperations.viewAllTeachers();  break;
                case 3: CRUDOperations.searchTeacherById(); break;
                case 4: CRUDOperations.updateTeacher();    break;
                case 5: CRUDOperations.deleteTeacher();    break;
                case 0: inMenu = false;                    break;
            }
        }
    }

    // --------------------------------------------------------
    // showClassMenu()
    // --------------------------------------------------------
    private static void showClassMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n┌──────────────────────────────────┐");
            System.out.println("│         CLASS MANAGEMENT          │");
            System.out.println("├──────────────────────────────────┤");
            System.out.println("│  [1] Add New Class                │");
            System.out.println("│  [2] View All Classes             │");
            System.out.println("│  [3] Search Class by ID           │");
            System.out.println("│  [4] Update Class                 │");
            System.out.println("│  [5] Delete Class                 │");
            System.out.println("│  [0] Back                         │");
            System.out.println("└──────────────────────────────────┘");

            int choice = InputHelper.readMenuChoice("Select option: ", 0, 5);
            switch (choice) {
                case 1: CRUDOperations.addClass();       break;
                case 2: CRUDOperations.viewAllClasses(); break;
                case 3: CRUDOperations.searchClassById(); break;
                case 4: CRUDOperations.updateClass();    break;
                case 5: CRUDOperations.deleteClass();    break;
                case 0: inMenu = false;                  break;
            }
        }
    }

    // --------------------------------------------------------
    // showStudentMenu()
    // --------------------------------------------------------
    private static void showStudentMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n┌──────────────────────────────────┐");
            System.out.println("│        STUDENT MANAGEMENT         │");
            System.out.println("├──────────────────────────────────┤");
            System.out.println("│  [1] Add New Student              │");
            System.out.println("│  [2] View All Students            │");
            System.out.println("│  [3] Search Student by ID         │");
            System.out.println("│  [4] Update Student               │");
            System.out.println("│  [5] Delete Student               │");
            System.out.println("│  [0] Back                         │");
            System.out.println("└──────────────────────────────────┘");

            int choice = InputHelper.readMenuChoice("Select option: ", 0, 5);
            switch (choice) {
                case 1: CRUDOperations.addStudent();       break;
                case 2: CRUDOperations.viewAllStudents();  break;
                case 3: CRUDOperations.searchStudentById(); break;
                case 4: CRUDOperations.updateStudent();    break;
                case 5: CRUDOperations.deleteStudent();    break;
                case 0: inMenu = false;                    break;
            }
        }
    }

    // --------------------------------------------------------
    // showAttendanceMenu()
    // --------------------------------------------------------
    private static void showAttendanceMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n┌──────────────────────────────────┐");
            System.out.println("│      ATTENDANCE MANAGEMENT        │");
            System.out.println("├──────────────────────────────────┤");
            System.out.println("│  [1] Record Attendance            │");
            System.out.println("│  [2] View All Attendance Records  │");
            System.out.println("│  [3] Search Attendance by ID      │");
            System.out.println("│  [4] Update Attendance            │");
            System.out.println("│  [5] Delete Attendance            │");
            System.out.println("│  [0] Back                         │");
            System.out.println("└──────────────────────────────────┘");

            int choice = InputHelper.readMenuChoice("Select option: ", 0, 5);
            switch (choice) {
                case 1: CRUDOperations.addAttendance();        break;
                case 2: CRUDOperations.viewAllAttendances();   break;
                case 3: CRUDOperations.searchAttendanceById(); break;
                case 4: CRUDOperations.updateAttendance();     break;
                case 5: CRUDOperations.deleteAttendance();     break;
                case 0: inMenu = false;                        break;
            }
        }
    }

    // --------------------------------------------------------
    // showReportMenu()
    // --------------------------------------------------------
    private static void showReportMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n┌──────────────────────────────────┐");
            System.out.println("│        REPORT MANAGEMENT          │");
            System.out.println("├──────────────────────────────────┤");
            System.out.println("│  [1] Add New Report               │");
            System.out.println("│  [2] View All Reports             │");
            System.out.println("│  [3] Search Report by ID          │");
            System.out.println("│  [4] Update Report                │");
            System.out.println("│  [5] Delete Report                │");
            System.out.println("│  [0] Back                         │");
            System.out.println("└──────────────────────────────────┘");

            int choice = InputHelper.readMenuChoice("Select option: ", 0, 5);
            switch (choice) {
                case 1: CRUDOperations.addReport();       break;
                case 2: CRUDOperations.viewAllReports();  break;
                case 3: CRUDOperations.searchReportById(); break;
                case 4: CRUDOperations.updateReport();    break;
                case 5: CRUDOperations.deleteReport();    break;
                case 0: inMenu = false;                   break;
            }
        }
    }

    // --------------------------------------------------------
    // printBanner()
    // --------------------------------------------------------
    private static void printBanner() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║         STUDENT ATTENDANCE SYSTEM            ║");
        System.out.println("║      Intermediate Programming  (JDBC)        ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║  Database : MySQL (student_attendance_db)    ║");
        System.out.println("║  Language : Java Procedural + JDBC           ║");
        System.out.println("║  Tables   : teachers, classes, students,     ║");
        System.out.println("║             attendances, reports              ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("  Initializing connection...\n");
    }
}
