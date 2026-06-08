package com.university.util;

import java.util.Scanner;

public class InputValidator {
    private static Scanner scanner = new Scanner(System.in);

    public static String readString(String prompt) {
        ConsoleColor.print(prompt, ConsoleColor.YELLOW_BOLD);
        return scanner.nextLine().trim();
    }

    public static String readStringNotEmpty(String prompt) {
        while (true) {
            String input = readString(prompt);
            if (!input.isEmpty()) {
                return input;
            }
            ConsoleColor.println("Error: Input cannot be empty. Please try again.", ConsoleColor.RED);
        }
    }

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            String input = readString(prompt);
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                ConsoleColor.println("Error: Input must be between " + min + " and " + max + ".", ConsoleColor.RED);
            } catch (NumberFormatException e) {
                ConsoleColor.println("Error: Please enter a valid integer.", ConsoleColor.RED);
            }
        }
    }

    public static double readDouble(String prompt, double min, double max) {
        while (true) {
            String input = readString(prompt);
            try {
                double value = Double.parseDouble(input);
                if (value >= min && value <= max) {
                    return value;
                }
                ConsoleColor.println("Error: Input must be between " + min + " and " + max + ".", ConsoleColor.RED);
            } catch (NumberFormatException e) {
                ConsoleColor.println("Error: Please enter a valid decimal number.", ConsoleColor.RED);
            }
        }
    }

    public static boolean readConfirmation(String prompt) {
        while (true) {
            String input = readString(prompt + " (y/n): ").toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }
            ConsoleColor.println("Error: Please enter 'y' for Yes or 'n' for No.", ConsoleColor.RED);
        }
    }

    public static void pressEnterToContinue() {
        System.out.println();
        readString("Press Enter to continue...");
    }
}
