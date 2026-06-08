package com.university.ui;

import com.university.model.User;
import com.university.service.AuthService;
import com.university.util.ConsoleColor;
import com.university.util.InputValidator;

import java.util.Optional;

public class LoginUI {
    private AuthService authService;
    private AdminUI adminUI;
    private ProfessorUI professorUI;
    private StudentUI studentUI;

    public LoginUI() {
        this.authService = new AuthService();
        this.adminUI = new AdminUI();
        this.professorUI = new ProfessorUI();
        this.studentUI = new StudentUI();
    }

    public void start() {
        while (true) {
            ConsoleColor.clearScreen();
            
            // Styled Banner
            ConsoleColor.println("╔══════════════════════════════════════════════════════════════════════╗", ConsoleColor.CYAN_BOLD);
            ConsoleColor.println("║                                                                      ║", ConsoleColor.CYAN_BOLD);
            ConsoleColor.println("║                 ACADEMIC NEXUS: UNIVERSITY SYSTEM                    ║", ConsoleColor.CYAN_BOLD);
            ConsoleColor.println("║                                                                      ║", ConsoleColor.CYAN_BOLD);
            ConsoleColor.println("╚══════════════════════════════════════════════════════════════════════╝", ConsoleColor.CYAN_BOLD);
            
            System.out.println();
            ConsoleColor.println("---- System Quick-Access Directory (For Testing) ----", ConsoleColor.YELLOW);
            System.out.println("  • Administrator Portal : username: [ admin  ] | password: [ admin123   ]");
            System.out.println("  • Faculty Portal       : username: [ turing ] | password: [ prof123    ]");
            System.out.println("  • Student Portal       : username: [ hema   ] | password: [ student123 ]");
            System.out.println("-----------------------------------------------------");
            System.out.println();

            System.out.println("1. Login to Portal");
            System.out.println("2. Exit Application");
            System.out.println();

            int choice = InputValidator.readInt("Select Option (1-2): ", 1, 2);
            if (choice == 2) {
                ConsoleColor.println("\nThank you for using Academic Nexus. Exiting...", ConsoleColor.CYAN_BOLD);
                break;
            }

            System.out.println();
            String username = InputValidator.readStringNotEmpty("Enter Username: ");
            String password = InputValidator.readStringNotEmpty("Enter Password: ");

            ConsoleColor.println("\nAuthenticating...", ConsoleColor.YELLOW);
            try {
                Thread.sleep(600); // Small delay to feel like a real system check
            } catch (InterruptedException ignored) {}

            Optional<User> userOpt = authService.authenticate(username, password);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                ConsoleColor.println("Authentication Successful!", ConsoleColor.GREEN);
                try {
                    Thread.sleep(400);
                } catch (InterruptedException ignored) {}

                // Route to appropriate view based on role
                switch (user.getRole()) {
                    case ADMIN:
                        adminUI.showMenu(user);
                        break;
                    case PROFESSOR:
                        professorUI.showMenu(user);
                        break;
                    case STUDENT:
                        studentUI.showMenu(user);
                        break;
                }
            } else {
                ConsoleColor.println("Error: Invalid username or password. Please try again.", ConsoleColor.RED_BOLD);
                InputValidator.pressEnterToContinue();
            }
        }
    }
}
