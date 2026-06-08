package com.university.ui;

import com.university.model.*;
import com.university.repository.Database;
import com.university.service.UniversityService;
import com.university.util.ConsoleColor;
import com.university.util.ConsoleTable;
import com.university.util.InputValidator;

import java.util.List;
import java.util.Optional;

public class StudentUI {
    private Database db;
    private UniversityService service;

    public StudentUI() {
        this.db = Database.getInstance();
        this.service = new UniversityService();
    }

    public void showMenu(User user) {
        Student student = (Student) user;
        while (true) {
            ConsoleColor.clearScreen();
            ConsoleColor.printHeader("STUDENT PORTAL - ACADEMICS & SERVICES");
            System.out.println("Welcome, " + ConsoleColor.colorize(student.getFullName(), ConsoleColor.CYAN_BOLD));
            System.out.println("ID: " + ConsoleColor.colorize(student.getId(), ConsoleColor.WHITE_BOLD) + 
                    " | Department: " + ConsoleColor.colorize(student.getDepartment().getName(), ConsoleColor.WHITE_BOLD));
            System.out.printf("Current Academic GPA: %s\n\n", 
                    ConsoleColor.colorize(String.format("%.2f", student.calculateGPA()), ConsoleColor.GREEN_BOLD));

            System.out.println("1. Academic Dashboard & Report Card");
            System.out.println("2. Enroll in Course");
            System.out.println("3. Drop Course");
            System.out.println("4. Tuition & Fee Center");
            System.out.println("5. Logout");
            System.out.println();

            int choice = InputValidator.readInt("Enter choice (1-5): ", 1, 5);

            switch (choice) {
                case 1:
                    viewReportCard(student);
                    break;
                case 2:
                    enrollCourse(student);
                    break;
                case 3:
                    dropCourse(student);
                    break;
                case 4:
                    manageFees(student);
                    break;
                case 5:
                    ConsoleColor.println("Logging out from Student session...", ConsoleColor.YELLOW);
                    return;
            }
        }
    }

    private void viewReportCard(Student student) {
        ConsoleColor.clearScreen();
        ConsoleColor.printHeader("ACADEMIC DASHBOARD & REPORT CARD");

        List<Course> enrolled = student.getEnrolledCourses();
        if (enrolled.isEmpty()) {
            ConsoleColor.println("You are not currently enrolled in any courses.", ConsoleColor.YELLOW);
        } else {
            ConsoleTable table = new ConsoleTable();
            table.setHeaders("Course Code", "Course Title", "Credits", "Instructor", "Grade", "Attendance");
            for (Course c : enrolled) {
                String grade = student.getGrades().getOrDefault(c.getCode(), "N/A");
                Double att = student.getAttendance().getOrDefault(c.getCode(), 100.0);
                String instructorName = c.getInstructor() != null ? c.getInstructor().getFullName() : "Staff";
                
                String attStr = String.format("%.1f%%", att);
                if (att < 75.0) {
                    attStr = ConsoleColor.colorize(attStr + " (Warning)", ConsoleColor.RED_BOLD);
                } else {
                    attStr = ConsoleColor.colorize(attStr, ConsoleColor.GREEN);
                }

                table.addRow(
                        c.getCode(),
                        c.getTitle(),
                        String.valueOf(c.getCredits()),
                        instructorName,
                        grade,
                        attStr
                );
            }
            table.print();
            
            System.out.println();
            System.out.printf("Total Enrolled Credits : %d\n", enrolled.stream().mapToInt(Course::getCredits).sum());
            System.out.printf("Current Semester GPA   : %s\n", 
                    ConsoleColor.colorize(String.format("%.2f", student.calculateGPA()), ConsoleColor.GREEN_BOLD));
        }
        InputValidator.pressEnterToContinue();
    }

    private void enrollCourse(Student student) {
        ConsoleColor.clearScreen();
        ConsoleColor.printHeader("COURSE REGISTRATION PORTAL");

        // List courses the student is NOT enrolled in
        List<Course> allCourses = db.getCourses();
        ConsoleTable table = new ConsoleTable();
        table.setHeaders("Course Code", "Course Title", "Credits", "Instructor", "Seats Available");
        
        boolean availableToEnroll = false;
        for (Course c : allCourses) {
            if (!student.getEnrolledCourses().contains(c)) {
                String instructorName = c.getInstructor() != null ? c.getInstructor().getFullName() : "Staff";
                table.addRow(
                        c.getCode(),
                        c.getTitle(),
                        String.valueOf(c.getCredits()),
                        instructorName,
                        c.getAvailableSeats() + " / " + c.getMaxCapacity()
                );
                availableToEnroll = true;
            }
        }

        if (!availableToEnroll) {
            ConsoleColor.println("No other courses are available to register at this time.", ConsoleColor.YELLOW);
            InputValidator.pressEnterToContinue();
            return;
        }

        table.print();
        System.out.println();
        
        String courseCode = InputValidator.readStringNotEmpty("Enter the Course Code you wish to enroll in: ").toUpperCase();
        
        StringBuilder msg = new StringBuilder();
        if (service.enrollStudentInCourse(student.getId(), courseCode, msg)) {
            ConsoleColor.println(msg.toString(), ConsoleColor.GREEN);
        } else {
            ConsoleColor.println("Registration Failed: " + msg.toString(), ConsoleColor.RED);
        }
        InputValidator.pressEnterToContinue();
    }

    private void dropCourse(Student student) {
        ConsoleColor.clearScreen();
        ConsoleColor.printHeader("DROP COURSE PORTAL");

        List<Course> enrolled = student.getEnrolledCourses();
        if (enrolled.isEmpty()) {
            ConsoleColor.println("You are not enrolled in any courses to drop.", ConsoleColor.YELLOW);
            InputValidator.pressEnterToContinue();
            return;
        }

        ConsoleTable table = new ConsoleTable();
        table.setHeaders("Course Code", "Course Title", "Credits");
        for (Course c : enrolled) {
            table.addRow(c.getCode(), c.getTitle(), String.valueOf(c.getCredits()));
        }
        table.print();
        System.out.println();

        String courseCode = InputValidator.readStringNotEmpty("Enter Course Code to drop: ").toUpperCase();
        
        Optional<Course> courseOpt = enrolled.stream().filter(c -> c.getCode().equalsIgnoreCase(courseCode)).findFirst();
        if (courseOpt.isPresent()) {
            if (InputValidator.readConfirmation("Are you sure you want to drop course " + courseCode + "?")) {
                StringBuilder msg = new StringBuilder();
                if (service.dropStudentFromCourse(student.getId(), courseCode, msg)) {
                    ConsoleColor.println(msg.toString(), ConsoleColor.GREEN);
                } else {
                    ConsoleColor.println("Failed: " + msg.toString(), ConsoleColor.RED);
                }
            }
        } else {
            ConsoleColor.println("Error: You are not enrolled in that course.", ConsoleColor.RED);
        }
        InputValidator.pressEnterToContinue();
    }

    private void manageFees(Student student) {
        while (true) {
            ConsoleColor.clearScreen();
            ConsoleColor.printHeader("TUITION & FINANCIAL CENTER");
            
            System.out.println(ConsoleColor.colorize("Tuition Summary Statement:", ConsoleColor.CYAN_BOLD));
            System.out.printf("  • Semester Base Fee    : $%,.2f\n", 4000.00);
            System.out.printf("  • Course Enroll. Fees  : $%,.2f\n", student.getEnrolledCourses().size() * 500.00);
            System.out.printf("  • Total Account Cost   : $%,.2f\n", 4000.00 + student.getEnrolledCourses().size() * 500.00);
            System.out.println("  --------------------------------------");
            System.out.printf("  • Total Fees Paid      : %s\n", ConsoleColor.colorize(String.format("$%,.2f", student.getFeesPaid()), ConsoleColor.GREEN));
            System.out.printf("  • Current Balance Due  : %s\n", ConsoleColor.colorize(String.format("$%,.2f", student.getFeeBalance()), 
                    student.getFeeBalance() > 0 ? ConsoleColor.RED_BOLD : ConsoleColor.GREEN_BOLD));
            System.out.println();

            System.out.println("1. Make Simulated Payment");
            System.out.println("2. Back to Student Menu");
            System.out.println();

            int choice = InputValidator.readInt("Choose action: ", 1, 2);
            if (choice == 2) break;

            if (student.getFeeBalance() <= 0.0) {
                ConsoleColor.println("Your balance is zero. No payment is due!", ConsoleColor.GREEN);
                InputValidator.pressEnterToContinue();
                continue;
            }

            double amt = InputValidator.readDouble("Enter payment amount ($): ", 1.0, student.getFeeBalance());
            student.payFees(amt);
            ConsoleColor.println("Transaction Approved! Thank you for your payment.", ConsoleColor.GREEN);
            InputValidator.pressEnterToContinue();
        }
    }
}
