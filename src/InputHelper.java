import java.util.Scanner;

public class InputHelper {

    private static final Scanner scanner = new Scanner(System.in);

    // ─── Primitives ────────────────────────────────────────────────────────────
// Reads non-empty string - loops until valid input is entere
    // Reads a non-empty string; re-prompts on blank input
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

    // Reads an optional string (blank is allowed — returns "")
    public static String readOptionalString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // Reads a positive integer; re-prompts on invalid input
    // Reads positive integer - rejects letters and negative numbers
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
                System.out.println("[!] Invalid number. Please try again.");
            }
        }
    }

    // Reads any integer (including 0 and negatives — used for menu choices)
    public static int readMenuChoice(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[!] Please enter a valid number.");
            }
        }
    }

    // ─── Specific field validators ─────────────────────────────────────────────
// Validates YYYY-MM-DD format using regex pattern matching
    // Validates date format YYYY-MM-DD
    public static String readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return input;
            }
            System.out.println("[!] Invalid date format. Please use YYYY-MM-DD (e.g. 2024-05-10).");
        }
    }

    // Validates time format HH:MM or HH:MM:SS
    public static String readTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches("\\d{2}:\\d{2}(:\\d{2})?")) {
                return input;
            }
            System.out.println("[!] Invalid time format. Please use HH:MM (e.g. 08:30).");
        }
    }
// Validates attendance status against DB ENUM values
    // Validates attendance status
    public static String readStatus(String prompt) {
        String[] valid = {"Present", "Absent", "Late", "Excused"};
        while (true) {
            System.out.print(prompt + " [Present / Absent / Late / Excused]: ");
            String input = scanner.nextLine().trim();
            for (String s : valid) {
                if (s.equalsIgnoreCase(input)) {
                    // Return properly capitalised value (matches ENUM in DB)
                    return s;
                }
            }
            System.out.println("[!] Invalid status. Choose: Present, Absent, Late, or Excused.");
        }
    }

    // Validates student status (active / inactive)
    public static String readStudentStatus(String prompt) {
        while (true) {
            System.out.print(prompt + " [active / inactive]: ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("active") || input.equals("inactive")) {
                return input;
            }
            System.out.println("[!] Please enter 'active' or 'inactive'.");
        }
    }
//Validates email format using regex - nullable field
    // Validates email format (basic check)
    public static String readEmail(String prompt) {
        while (true) {
            System.out.print(prompt + " (or press Enter to skip): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return null;   // nullable field
            if (input.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
                return input;
            }
            System.out.println("[!] Invalid email format. Try again or press Enter to skip.");
        }
    }
// Validates contact number - digits only, 7-15 characters
    // Validates contact number (digits only, 7-15 chars)
    public static String readContactNumber(String prompt) {
        while (true) {
            System.out.print(prompt + " (or press Enter to skip): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return null;
            if (input.matches("\\d{7,15}")) {
                return input;
            }
            System.out.println("[!] Invalid contact number. Use digits only, 7-15 characters.");
        }
    }
// Prompts user for yes/no confirmation before destructive actions
    // Confirm destructive action (delete / overwrite)
    public static boolean confirm(String message) {
        while (true) {
            System.out.print(message + " [y/n]: ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) return true;
            if (input.equals("n") || input.equals("no"))  return false;
            System.out.println("[!] Please enter y or n.");
        }
    }

    // ─── Formatting helpers ────────────────────────────────────────────────────

    // Prints a separator line for table headers
    public static void printSeparator(int width) {
        System.out.println("-".repeat(width));
    }

    // Prints a section title banner
    public static void printHeader(String title) {
        System.out.println();
        System.out.println("========================================");
        System.out.println("  " + title);
        System.out.println("========================================");
    }

    // Pause and wait for Enter (used after showing results)
    public static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
