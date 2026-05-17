// ============================================================
// File: InputHelper.java
// Role: Input & Validation Engineer
// Author: Dominosula
// Description: Handles all user input and validation for the
//              Student Attendance System.
// ============================================================

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InputHelper {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // --------------------------------------------------------
    // readString(prompt)
    // Reads a non-empty string. Re-prompts if blank.
    // --------------------------------------------------------
    public static String readString(String prompt) {
        String input = "";
        while (input.trim().isEmpty()) {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                System.out.println("[!] Input cannot be empty. Please try again.");
            }
        }
        return input.trim();
    }

    // --------------------------------------------------------
    // readOptionalString(prompt)
    // Reads a string where blank is acceptable (for updates).
    // --------------------------------------------------------
    public static String readOptionalString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // --------------------------------------------------------
    // readInt(prompt)
    // Reads a positive integer. Rejects letters and negatives.
    // --------------------------------------------------------
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    System.out.println("[!] Please enter a positive number.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid input. Numbers only, please.");
            }
        }
    }

    // --------------------------------------------------------
    // readIntAllowZero(prompt)
    // Reads a non-negative integer (allows 0 for counts).
    // --------------------------------------------------------
    public static int readIntAllowZero(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("[!] Please enter a number that is 0 or greater.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid input. Numbers only, please.");
            }
        }
    }

    // --------------------------------------------------------
    // readMenuChoice(prompt, min, max)
    // Reads a menu option within a valid range.
    // --------------------------------------------------------
    public static int readMenuChoice(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("[!] Please enter a number between " + min + " and " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid input. Please enter a number.");
            }
        }
    }

    // --------------------------------------------------------
    // readDate(prompt)
    // Reads a date in yyyy-MM-dd format. Re-prompts if invalid.
    // --------------------------------------------------------
    public static String readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                LocalDate.parse(input, DATE_FORMAT);
                return input;
            } catch (DateTimeParseException e) {
                System.out.println("[!] Invalid date. Use yyyy-MM-dd format (e.g., 2026-05-17).");
            }
        }
    }

    // --------------------------------------------------------
    // readEmail(prompt)
    // Reads and validates a basic email format.
    // --------------------------------------------------------
    public static String readEmail(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.contains("@") && input.contains(".") && input.length() > 5) {
                return input;
            } else if (input.isEmpty()) {
                return "";  // allow blank for optional email
            } else {
                System.out.println("[!] Invalid email format. Please try again.");
            }
        }
    }

    // --------------------------------------------------------
    // readAttendanceStatus(prompt)
    // Accepts: Present, Absent, Late, Excused only.
    // --------------------------------------------------------
    public static String readAttendanceStatus(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("Present")  ||
                input.equalsIgnoreCase("Absent")   ||
                input.equalsIgnoreCase("Late")     ||
                input.equalsIgnoreCase("Excused")) {
                return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
            } else {
                System.out.println("[!] Status must be: Present, Absent, Late, or Excused.");
            }
        }
    }

    // --------------------------------------------------------
    // readStudentStatus(prompt)
    // Accepts: active or inactive only.
    // --------------------------------------------------------
    public static String readStudentStatus(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("active") || input.equalsIgnoreCase("inactive")) {
                return input.toLowerCase();
            } else {
                System.out.println("[!] Status must be: active or inactive.");
            }
        }
    }

    // --------------------------------------------------------
    // readContactNumber(prompt)
    // Validates Philippine-style contact numbers (11 digits).
    // --------------------------------------------------------
    public static String readContactNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return "";  // optional field
            if (input.matches("\\d{11}") && input.startsWith("09")) {
                return input;
            } else {
                System.out.println("[!] Contact number must be 11 digits starting with 09 (e.g., 09123456789).");
            }
        }
    }

    // --------------------------------------------------------
    // readMonth(prompt)
    // Validates month input (1-12).
    // --------------------------------------------------------
    public static int readMonth(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int month = Integer.parseInt(input);
                if (month >= 1 && month <= 12) return month;
                System.out.println("[!] Month must be between 1 and 12.");
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid input. Please enter a number.");
            }
        }
    }

    // --------------------------------------------------------
public static int readYear(String prompt) {
    int currentYear = LocalDate.now().getYear(); // dynamically gets current year
    while (true) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            int year = Integer.parseInt(input);
            if (year >= 2000 && year <= currentYear) return year;
            System.out.println("[!] Please enter a valid year (2000-" + currentYear + ").");
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid input. Please enter a 4-digit year.");
        }
    }
}
    // --------------------------------------------------------
    // readTimeIn(prompt)
    // Reads time in HH:mm:ss format. Optional — blank allowed.
    // --------------------------------------------------------
    public static String readTimeIn(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return null;
            if (input.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]")) {
                return input;
            } else {
                System.out.println("[!] Invalid time. Use HH:mm:ss format (e.g., 08:30:00). Press Enter to skip.");
            }
        }
    }

    // --------------------------------------------------------
    // confirm(prompt)
    // Asks a yes/no confirmation. Returns true for 'y'.
    // --------------------------------------------------------
    public static boolean confirm(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) return true;
            if (input.equals("n")) return false;
            System.out.println("[!] Please enter y or n.");
        }
    }
}