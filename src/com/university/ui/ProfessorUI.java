package com.university.ui;

import com.university.model.*;
import com.university.repository.Database;
import com.university.service.UniversityService;
import com.university.util.ConsoleColor;
import com.university.util.ConsoleTable;
import com.university.util.InputValidator;

import java.util.List;
import java.util.Optional;

public class ProfessorUI {
    private Database db;
    private UniversityService service;

    public ProfessorUI() {
        this.db = Database.getInstance();
        this.service = new UniversityService();
    }

    public void showMenu(User user) {
        Professor professor = (Professor) user;
        while (true) {
            ConsoleColor.clearScreen();
            ConsoleColor.printHeader("FACULTY PORTAL - " + professor.getDepartment().getName());
            System.out.println("Welcome, " + ConsoleColor.colorize(professor.getFullName(), ConsoleColor.CYAN_BOLD));
            System.out.println("Specialization: " + ConsoleColor.colorize(professor.getSpecialization(), ConsoleColor.WHITE_BOLD));
            System.out.println();

            System.out.println("1. View Teaching Schedule");
            System.out.println("2. View Class Roster");
            System.out.println("3. Grade Student");
            System.out.println("4. Record Attendance");
            System.out.println("5. Logout");
            System.out.println();

            int choice = InputValidator.readInt("Enter choice (1-5): ", 1, 5);

            switch (choice) {
                case 1:
                    viewSchedule(professor);
                    break;
                case 2:
                    viewRoster(professor);
                    break;
                case 3:
                    gradeStudent(professor);
                    break;
                case 4:
                    recordAttendance(professor);
                    break;
                case 5:
                    ConsoleColor.println("Logging out from Faculty session...", ConsoleColor.YELLOW);
                    return;
            }
        }
    }

    private void viewSchedule(Professor professor) {
        ConsoleColor.clearScreen();
        ConsoleColor.printHeader("MY TEACHING SCHEDULE");

        List<Course> courses = professor.getTeachingCourses();
        if (courses.isEmpty()) {
            ConsoleColor.println("You are not currently teaching any courses.", ConsoleColor.YELLOW);
        } else {
            ConsoleTable table = new ConsoleTable();
            table.setHeaders("Course Code", "Course Title", "Credits", "Enrolled Students", "Capacity");
            for (Course c : courses) {
                table.addRow(
                        c.getCode(),
                        c.getTitle(),
                        String.valueOf(c.getCredits()),
                        String.valueOf(c.getEnrolledStudents().size()),
                        String.valueOf(c.getMaxCapacity())
                );
            }
            table.print();
        }
        InputValidator.pressEnterToContinue();
    }

    private void viewRoster(Professor professor) {
        ConsoleColor.clearScreen();
        ConsoleColor.printHeader("CLASS ROSTER LOOKUP");

        List<Course> courses = professor.getTeachingCourses();
        if (courses.isEmpty()) {
            ConsoleColor.println("You are not teaching any courses.", ConsoleColor.YELLOW);
            InputValidator.pressEnterToContinue();
            return;
        }

        System.out.println("Select a course to view roster:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getCode() + " - " + courses.get(i).getTitle());
        }
        System.out.println((courses.size() + 1) + ". Back");
        
        int choice = InputValidator.readInt("Enter course index: ", 1, courses.size() + 1);
        if (choice == courses.size() + 1) return;

        Course course = courses.get(choice - 1);
        ConsoleColor.clearScreen();
        ConsoleColor.printHeader("ROSTER FOR: " + course.getCode() + " - " + course.getTitle());

        List<Student> students = course.getEnrolledStudents();
        if (students.isEmpty()) {
            ConsoleColor.println("No students enrolled in this course.", ConsoleColor.YELLOW);
        } else {
            ConsoleTable table = new ConsoleTable();
            table.setHeaders("Student ID", "Full Name", "Grade", "Attendance");
            for (Student s : students) {
                String grade = s.getGrades().getOrDefault(course.getCode(), "N/A");
                Double att = s.getAttendance().getOrDefault(course.getCode(), 100.0);
                
                String attStr = String.format("%.1f%%", att);
                if (att < 75.0) {
                    attStr = ConsoleColor.colorize(attStr, ConsoleColor.RED_BOLD);
                } else if (att < 85.0) {
                    attStr = ConsoleColor.colorize(attStr, ConsoleColor.YELLOW_BOLD);
                } else {
                    attStr = ConsoleColor.colorize(attStr, ConsoleColor.GREEN);
                }

                table.addRow(s.getId(), s.getFullName(), grade, attStr);
            }
            table.print();
        }
        InputValidator.pressEnterToContinue();
    }

    private void gradeStudent(Professor professor) {
        ConsoleColor.clearScreen();
        ConsoleColor.printHeader("STUDENT GRADING SYSTEM");

        List<Course> courses = professor.getTeachingCourses();
        if (courses.isEmpty()) {
            ConsoleColor.println("You are not teaching any courses.", ConsoleColor.YELLOW);
            InputValidator.pressEnterToContinue();
            return;
        }

        System.out.println("Select a course to grade:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getCode() + " - " + courses.get(i).getTitle());
        }
        System.out.println((courses.size() + 1) + ". Back");

        int choice = InputValidator.readInt("Enter choice: ", 1, courses.size() + 1);
        if (choice == courses.size() + 1) return;

        Course course = courses.get(choice - 1);
        List<Student> students = course.getEnrolledStudents();
        if (students.isEmpty()) {
            ConsoleColor.println("No students enrolled in this course.", ConsoleColor.YELLOW);
            InputValidator.pressEnterToContinue();
            return;
        }

        String studentId = InputValidator.readStringNotEmpty("Enter Student ID to grade: ");
        Optional<Student> studentOpt = students.stream()
                .filter(s -> s.getId().equalsIgnoreCase(studentId))
                .findFirst();

        if (!studentOpt.isPresent()) {
            ConsoleColor.println("Error: Student is not enrolled in this course.", ConsoleColor.RED);
            InputValidator.pressEnterToContinue();
            return;
        }

        Student student = studentOpt.get();
        String currentGrade = student.getGrades().getOrDefault(course.getCode(), "N/A");
        System.out.println("Student: " + ConsoleColor.colorize(student.getFullName(), ConsoleColor.CYAN_BOLD));
        System.out.println("Current Grade: " + ConsoleColor.colorize(currentGrade, ConsoleColor.YELLOW));
        
        String newGrade = InputValidator.readStringNotEmpty("Enter new Grade (A+, A, A-, B+, B, B-, C+, C, C-, D+, D, F): ").toUpperCase();
        
        StringBuilder msg = new StringBuilder();
        if (service.updateStudentGrade(student.getId(), course.getCode(), newGrade, msg)) {
            ConsoleColor.println(msg.toString(), ConsoleColor.GREEN);
        } else {
            ConsoleColor.println("Error: " + msg.toString(), ConsoleColor.RED);
        }
        InputValidator.pressEnterToContinue();
    }

    private void recordAttendance(Professor professor) {
        ConsoleColor.clearScreen();
        ConsoleColor.printHeader("ATTENDANCE TRACKER");

        List<Course> courses = professor.getTeachingCourses();
        if (courses.isEmpty()) {
            ConsoleColor.println("You are not teaching any courses.", ConsoleColor.YELLOW);
            InputValidator.pressEnterToContinue();
            return;
        }

        System.out.println("Select a course to record attendance:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getCode() + " - " + courses.get(i).getTitle());
        }
        System.out.println((courses.size() + 1) + ". Back");

        int choice = InputValidator.readInt("Enter choice: ", 1, courses.size() + 1);
        if (choice == courses.size() + 1) return;

        Course course = courses.get(choice - 1);
        List<Student> students = course.getEnrolledStudents();
        if (students.isEmpty()) {
            ConsoleColor.println("No students enrolled in this course.", ConsoleColor.YELLOW);
            InputValidator.pressEnterToContinue();
            return;
        }

        String studentId = InputValidator.readStringNotEmpty("Enter Student ID: ");
        Optional<Student> studentOpt = students.stream()
                .filter(s -> s.getId().equalsIgnoreCase(studentId))
                .findFirst();

        if (!studentOpt.isPresent()) {
            ConsoleColor.println("Error: Student is not enrolled in this course.", ConsoleColor.RED);
            InputValidator.pressEnterToContinue();
            return;
        }

        Student student = studentOpt.get();
        double currentAtt = student.getAttendance().getOrDefault(course.getCode(), 100.0);
        System.out.println("Student: " + ConsoleColor.colorize(student.getFullName(), ConsoleColor.CYAN_BOLD));
        System.out.printf("Current Attendance: %.1f%%\n", currentAtt);

        double newAtt = InputValidator.readDouble("Enter new attendance percentage (0.0 to 100.0): ", 0.0, 100.0);

        StringBuilder msg = new StringBuilder();
        if (service.updateStudentAttendance(student.getId(), course.getCode(), newAtt, msg)) {
            ConsoleColor.println(msg.toString(), ConsoleColor.GREEN);
        } else {
            ConsoleColor.println("Error: " + msg.toString(), ConsoleColor.RED);
        }
        InputValidator.pressEnterToContinue();
    }
}
