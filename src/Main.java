// ============================================
// File: Main.java
// Author: mendozadarlene-dotcom
// Role: Error Handling & Menu Developer
// Description: Main entry point of the Student
//              Attendance System. Handles the
//              menu-driven console interface,
//              integrates all modules, and
//              manages error handling and
//              program flow.
// Date: May 2026
// ============================================
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   STUDENT ATTENDANCE SYSTEM              ║");
        System.out.println("║   Powered by Java + MySQL (JDBC)         ║");
        System.out.println("╚══════════════════════════════════════════╝");

        if (!DBConnection.testConnection()) {
            System.out.println("[FATAL] Could not connect to the database. Exiting.");
            return;
        }

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = InputHelper.readMenuChoice("Enter choice: ");

            switch (choice) {
                case 1: studentMenu(); break;
                case 2: attendanceMenu(); break;
                case 3: viewClassesMenu(); break;
                case 4: teacherMenu(); break;
                case 5: reportsMenu(); break;
                case 0:
                    System.out.println("\nGoodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("[!] Invalid option. Please choose 0-5.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n==========================================");
        System.out.println("  MAIN MENU");
        System.out.println("==========================================");
        System.out.println("  [1] Students");
        System.out.println("  [2] Attendance");
        System.out.println("  [3] Classes");
        System.out.println("  [4] Teachers");
        System.out.println("  [5] Reports");
        System.out.println("  [0] Exit");
        System.out.println("==========================================");
    }


    private static void studentMenu() {
        boolean back = false;
        while (!back) {
            InputHelper.printHeader("STUDENTS");
            System.out.println("  [1] Add student");
            System.out.println("  [2] View all students");
            System.out.println("  [3] Search student by ID");
            System.out.println("  [4] Update student");
            System.out.println("  [5] Delete student");
            System.out.println("  [0] Back to main menu");

            int choice = InputHelper.readMenuChoice("Enter choice: ");
            switch (choice) {
                case 1: addStudentFlow(); break;
                case 2:
                    CRUDOperations.viewAllStudents();
                    InputHelper.pressEnterToContinue();
                    break;
                case 3:
                    int id = InputHelper.readInt("Enter Student ID: ");
                    CRUDOperations.searchStudentById(id);
                    InputHelper.pressEnterToContinue();
                    break;
                case 4: updateStudentFlow(); break;
                case 5: deleteStudentFlow(); break;
                case 0: back = true; break;
                default: System.out.println("[!] Invalid option.");
            }
        }
    }

// Collects and validates student data then calls CRUDOperations
    private static void addStudentFlow() {
        InputHelper.printHeader("ADD NEW STUDENT");
        String firstName      = InputHelper.readString("First name       : ");
        String lastName       = InputHelper.readString("Last name        : ");
        String email          = InputHelper.readEmail("Email            ");
        String enrollmentDate = InputHelper.readDate("Enrollment date  (YYYY-MM-DD): ");
        String status         = InputHelper.readStudentStatus("Status           ");

        System.out.println("\n-- Available Classes --");
        CRUDOperations.viewAllClasses();
        System.out.print("Class ID (or 0 to skip): ");
        Scanner sc = new Scanner(System.in);
        String classInput = sc.nextLine().trim();
        Integer classId = null;
        try {
            int parsed = Integer.parseInt(classInput);
            if (parsed > 0) classId = parsed;
        } catch (NumberFormatException ignored) {}

        String contactNumber = InputHelper.readContactNumber("Contact number   ");
        String parentName    = InputHelper.readOptionalString("Parent/Guardian name (or Enter to skip): ");

        CRUDOperations.addStudent(firstName, lastName, email, enrollmentDate,
                status, classId, contactNumber,
                parentName.isEmpty() ? null : parentName);
        InputHelper.pressEnterToContinue();
    }

    private static void updateStudentFlow() {
        InputHelper.printHeader("UPDATE STUDENT");
        int id = InputHelper.readInt("Enter Student ID to update: ");
        CRUDOperations.searchStudentById(id);

        String firstName     = InputHelper.readString("First name      : ");
        String lastName      = InputHelper.readString("Last name       : ");
        String email         = InputHelper.readEmail("Email           ");
        String status        = InputHelper.readStudentStatus("Status          ");

        System.out.println("\n-- Available Classes --");
        CRUDOperations.viewAllClasses();
        System.out.print("Class ID (or 0 to skip): ");
        Scanner sc = new Scanner(System.in);
        String classInput = sc.nextLine().trim();
        Integer classId = null;
        try {
            int parsed = Integer.parseInt(classInput);
            if (parsed > 0) classId = parsed;
        } catch (NumberFormatException ignored) {}

        String contactNumber = InputHelper.readContactNumber("Contact number  ");
        String parentName    = InputHelper.readOptionalString("Parent/Guardian : ");

        CRUDOperations.updateStudent(id, firstName, lastName, email, status,
                classId, contactNumber,
                parentName.isEmpty() ? null : parentName);
        InputHelper.pressEnterToContinue();
    }

// Confirms deletion before calling CRUDOperations.deleteStudent()
    private static void deleteStudentFlow() {
        InputHelper.printHeader("DELETE STUDENT");
        int id = InputHelper.readInt("Enter Student ID to delete: ");
        CRUDOperations.searchStudentById(id);

        if (InputHelper.confirm("Are you sure you want to delete this student?")) {
            CRUDOperations.deleteStudent(id);
        } else {
            System.out.println("[INFO] Deletion cancelled.");
        }
        InputHelper.pressEnterToContinue();
    }

  //attendance menu - handles marking, viewing, updating, deleting records
    private static void attendanceMenu() {
        boolean back = false;
        while (!back) {
            InputHelper.printHeader("ATTENDANCE");
            System.out.println("  [1] Mark attendance");
            System.out.println("  [2] View all attendance records");
            System.out.println("  [3] View attendance by student ID");
            System.out.println("  [4] Update attendance record");
            System.out.println("  [5] Delete attendance record");
            System.out.println("  [0] Back");

            int choice = InputHelper.readMenuChoice("Enter choice: ");
            switch (choice) {
                case 1: markAttendanceFlow(); break;
                case 2:
                    CRUDOperations.viewAllAttendance();
                    InputHelper.pressEnterToContinue();
                    break;
                case 3:
                    int id = InputHelper.readInt("Enter Student ID: ");
                    CRUDOperations.searchAttendanceByStudentId(id);
                    InputHelper.pressEnterToContinue();
                    break;
                case 4: updateAttendanceFlow(); break;
                case 5: deleteAttendanceFlow(); break;
                case 0: back = true; break;
                default: System.out.println("[!] Invalid option.");
            }
        }
    }

    private static void markAttendanceFlow() {
        InputHelper.printHeader("MARK ATTENDANCE");
        System.out.println("-- Students --");
        CRUDOperations.viewAllStudents();
        int studentId = InputHelper.readInt("Student ID: ");

        System.out.println("\n-- Classes --");
        CRUDOperations.viewAllClasses();
        int classId = InputHelper.readInt("Class ID: ");

        String date    = InputHelper.readDate("Date (YYYY-MM-DD): ");
        String status  = InputHelper.readStatus("Status");
        String timeIn  = InputHelper.readOptionalString("Time in (HH:MM, or Enter to skip): ");
        String remarks = InputHelper.readOptionalString("Remarks (or Enter to skip)        : ");

        CRUDOperations.markAttendance(studentId, classId, date, status,
                timeIn.isEmpty()  ? null : timeIn,
                remarks.isEmpty() ? null : remarks);
        InputHelper.pressEnterToContinue();
    }

    private static void updateAttendanceFlow() {
        InputHelper.printHeader("UPDATE ATTENDANCE");
        CRUDOperations.viewAllAttendance();
        int id = InputHelper.readInt("Enter Attendance ID to update: ");
        String status  = InputHelper.readStatus("New status");
        String timeIn  = InputHelper.readOptionalString("New time in (HH:MM, or Enter to skip): ");
        String remarks = InputHelper.readOptionalString("New remarks (or Enter to skip)        : ");
        CRUDOperations.updateAttendance(id, status,
                timeIn.isEmpty()  ? null : timeIn,
                remarks.isEmpty() ? null : remarks);
        InputHelper.pressEnterToContinue();
    }

    private static void deleteAttendanceFlow() {
        InputHelper.printHeader("DELETE ATTENDANCE");
        int id = InputHelper.readInt("Enter Attendance ID to delete: ");
        if (InputHelper.confirm("Delete attendance record ID " + id + "?")) {
            CRUDOperations.deleteAttendance(id);
        } else {
            System.out.println("[INFO] Cancelled.");
        }
        InputHelper.pressEnterToContinue();
    }

    // Classes menu - handles full CRUD for class management
    private static void viewClassesMenu() {
    boolean back = false;
    while (!back) {
        InputHelper.printHeader("CLASSES");
        System.out.println("  [1] Add class");
        System.out.println("  [2] View all classes");
        System.out.println("  [3] Search class by ID");
        System.out.println("  [4] Update class");
        System.out.println("  [5] Delete class");
        System.out.println("  [0] Back");

        int choice = InputHelper.readMenuChoice("Enter choice: ");
        switch (choice) {
            case 1:
                InputHelper.printHeader("ADD CLASS");
                String className = InputHelper.readString("Class name   : ");
                int gradeLevel   = InputHelper.readInt("Grade level  : ");
                String section   = InputHelper.readString("Section      : ");
                int capacity     = InputHelper.readInt("Capacity     : ");
                CRUDOperations.addClass(className, gradeLevel, section, capacity);
                InputHelper.pressEnterToContinue();
                break;
            case 2:
                CRUDOperations.viewAllClasses();
                InputHelper.pressEnterToContinue();
                break;
            case 3:
                int id = InputHelper.readInt("Enter Class ID: ");
                CRUDOperations.searchClassById(id);
                InputHelper.pressEnterToContinue();
                break;
            case 4:
                InputHelper.printHeader("UPDATE CLASS");
                int updateId       = InputHelper.readInt("Enter Class ID to update: ");
                CRUDOperations.searchClassById(updateId);
                String newName     = InputHelper.readString("New class name  : ");
                int newGrade       = InputHelper.readInt("New grade level : ");
                String newSection  = InputHelper.readString("New section     : ");
                int newCapacity    = InputHelper.readInt("New capacity    : ");
                CRUDOperations.updateClass(updateId, newName, newGrade,
                        newSection, newCapacity);
                InputHelper.pressEnterToContinue();
                break;
            case 5:
                InputHelper.printHeader("DELETE CLASS");
                int delId = InputHelper.readInt("Enter Class ID to delete: ");
                CRUDOperations.searchClassById(delId);
                if (InputHelper.confirm("Delete this class?")) {
                    CRUDOperations.deleteClass(delId);
                } else {
                    System.out.println("[INFO] Cancelled.");
                }
                InputHelper.pressEnterToContinue();
                break;
            case 0:
                back = true;
                break;
            default:
                System.out.println("[!] Invalid option.");
        }
    }
}
// Teachers menu - displays all registered teachers
    private static void teacherMenu() {
        InputHelper.printHeader("TEACHERS");
        CRUDOperations.viewAllTeachers();
        InputHelper.pressEnterToContinue();
    }
// Reports menu - displays monthly attendance summary per student
    private static void reportsMenu() {
        InputHelper.printHeader("ATTENDANCE REPORTS");
        System.out.println("-- Students --");
        CRUDOperations.viewAllStudents();
        int studentId = InputHelper.readInt("Enter Student ID to view report: ");
        CRUDOperations.viewReportByStudent(studentId);
        InputHelper.pressEnterToContinue();
    }
}
