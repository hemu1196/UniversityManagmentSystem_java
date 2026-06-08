package com.university.ui;

import com.university.model.*;
import com.university.repository.Database;
import com.university.service.UniversityService;
import com.university.util.ConsoleColor;
import com.university.util.ConsoleTable;
import com.university.util.InputValidator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdminUI {
    private Database db;
    private UniversityService service;

    public AdminUI() {
        this.db = Database.getInstance();
        this.service = new UniversityService();
    }

    public void showMenu(User adminUser) {
        while (true) {
            ConsoleColor.clearScreen();
            ConsoleColor.printHeader("ADMINISTRATOR CONTROL PANEL");
            System.out.println("Welcome, " + ConsoleColor.colorize(adminUser.getFullName(), ConsoleColor.CYAN_BOLD));
            System.out.println("Role: " + ConsoleColor.colorize("SYSTEM ADMIN", ConsoleColor.RED_BOLD));
            System.out.println();

            // Quick Stats banner
            Map<String, Object> stats = service.getSystemStatistics();
            System.out.printf("[ Stats Dashboard: Students: %d | Faculty: %d | Courses: %d | Avg GPA: %.2f ]\n\n",
                    stats.get("totalStudents"), stats.get("totalProfessors"), stats.get("totalCourses"), stats.get("averageGPA"));

            System.out.println("1. Department Management");
            System.out.println("2. Course Management");
            System.out.println("3. Student Management");
            System.out.println("4. Professor/Faculty Management");
            System.out.println("5. Overall System Stats & Visualizations");
            System.out.println("6. Logout");
            System.out.println();

            int choice = InputValidator.readInt("Enter your choice (1-6): ", 1, 6);

            switch (choice) {
                case 1:
                    manageDepartments();
                    break;
                case 2:
                    manageCourses();
                    break;
                case 3:
                    manageStudents();
                    break;
                case 4:
                    manageProfessors();
                    break;
                case 5:
                    showDetailedStats(stats);
                    break;
                case 6:
                    ConsoleColor.println("Logging out from Admin session...", ConsoleColor.YELLOW);
                    return;
            }
        }
    }

    // --- Department Management ---
    private void manageDepartments() {
        while (true) {
            ConsoleColor.clearScreen();
            ConsoleColor.printHeader("DEPARTMENT MANAGEMENT");
            
            // List departments
            ConsoleTable table = new ConsoleTable();
            table.setHeaders("Dept Code", "Department Name", "Head of Department");
            for (Department d : db.getDepartments()) {
                String headName = d.getHeadOfDepartment() != null ? d.getHeadOfDepartment().getFullName() : "VACANT";
                table.addRow(d.getCode(), d.getName(), headName);
            }
            table.print();

            System.out.println("\n1. Add New Department");
            System.out.println("2. Assign Head of Department");
            System.out.println("3. Back to Admin Menu");
            System.out.println();

            int choice = InputValidator.readInt("Choose action: ", 1, 3);
            if (choice == 3) break;

            if (choice == 1) {
                String code = InputValidator.readStringNotEmpty("Enter Dept Code (e.g. ME): ").toUpperCase();
                if (db.getDepartmentByCode(code).isPresent()) {
                    ConsoleColor.println("Error: Department with code " + code + " already exists.", ConsoleColor.RED);
                    InputValidator.pressEnterToContinue();
                    continue;
                }
                String name = InputValidator.readStringNotEmpty("Enter Department Name: ");
                db.addDepartment(new Department(code, name));
                ConsoleColor.println("Department added successfully!", ConsoleColor.GREEN);
            } else {
                String deptCode = InputValidator.readStringNotEmpty("Enter Dept Code: ").toUpperCase();
                Optional<Department> deptOpt = db.getDepartmentByCode(deptCode);
                if (!deptOpt.isPresent()) {
                    ConsoleColor.println("Error: Department not found.", ConsoleColor.RED);
                    InputValidator.pressEnterToContinue();
                    continue;
                }
                
                // Print professors
                ConsoleColor.printSubHeader("Available Professors for " + deptCode);
                List<Professor> profs = db.getProfessors();
                for (Professor p : profs) {
                    if (p.getDepartment().getCode().equalsIgnoreCase(deptCode)) {
                        System.out.println(p.getId() + " - " + p.getFullName());
                    }
                }
                String profId = InputValidator.readStringNotEmpty("Enter Professor ID to assign as Head: ");
                Optional<User> profOpt = db.getUserById(profId);
                if (profOpt.isPresent() && profOpt.get() instanceof Professor) {
                    deptOpt.get().setHeadOfDepartment((Professor) profOpt.get());
                    ConsoleColor.println("Head of Department assigned successfully!", ConsoleColor.GREEN);
                } else {
                    ConsoleColor.println("Error: Invalid Professor ID.", ConsoleColor.RED);
                }
            }
            InputValidator.pressEnterToContinue();
        }
    }

    // --- Course Management ---
    private void manageCourses() {
        while (true) {
            ConsoleColor.clearScreen();
            ConsoleColor.printHeader("COURSE MANAGEMENT");

            ConsoleTable table = new ConsoleTable();
            table.setHeaders("Course Code", "Course Title", "Credits", "Department", "Instructor", "Enrolled", "Seats Left");
            for (Course c : db.getCourses()) {
                String instructorName = c.getInstructor() != null ? c.getInstructor().getFullName() : "UNASSIGNED";
                table.addRow(
                        c.getCode(),
                        c.getTitle(),
                        String.valueOf(c.getCredits()),
                        c.getDepartment().getCode(),
                        instructorName,
                        c.getEnrolledStudents().size() + "/" + c.getMaxCapacity(),
                        String.valueOf(c.getAvailableSeats())
                );
            }
            table.print();

            System.out.println("\n1. Create New Course");
            System.out.println("2. Assign Instructor to Course");
            System.out.println("3. Delete/Remove Course");
            System.out.println("4. Back to Admin Menu");
            System.out.println();

            int choice = InputValidator.readInt("Choose action: ", 1, 4);
            if (choice == 4) break;

            if (choice == 1) {
                String code = InputValidator.readStringNotEmpty("Enter Course Code (e.g. CS102): ").toUpperCase();
                if (db.getCourseByCode(code).isPresent()) {
                    ConsoleColor.println("Error: Course code already exists.", ConsoleColor.RED);
                    InputValidator.pressEnterToContinue();
                    continue;
                }
                String title = InputValidator.readStringNotEmpty("Enter Course Title: ");
                int credits = InputValidator.readInt("Enter Credits (1-5): ", 1, 5);
                
                String deptCode = InputValidator.readStringNotEmpty("Enter Department Code: ").toUpperCase();
                Optional<Department> deptOpt = db.getDepartmentByCode(deptCode);
                if (!deptOpt.isPresent()) {
                    ConsoleColor.println("Error: Department code not found.", ConsoleColor.RED);
                    InputValidator.pressEnterToContinue();
                    continue;
                }

                int capacity = InputValidator.readInt("Enter Maximum Student Capacity: ", 5, 120);

                service.createCourse(code, title, credits, deptOpt.get(), null, capacity);
                ConsoleColor.println("Course created successfully! (Unassigned Instructor)", ConsoleColor.GREEN);

            } else if (choice == 2) {
                String courseCode = InputValidator.readStringNotEmpty("Enter Course Code: ").toUpperCase();
                Optional<Course> courseOpt = db.getCourseByCode(courseCode);
                if (!courseOpt.isPresent()) {
                    ConsoleColor.println("Error: Course not found.", ConsoleColor.RED);
                    InputValidator.pressEnterToContinue();
                    continue;
                }

                String profId = InputValidator.readStringNotEmpty("Enter Professor ID: ");
                Optional<User> profOpt = db.getUserById(profId);
                if (profOpt.isPresent() && profOpt.get() instanceof Professor) {
                    Professor prof = (Professor) profOpt.get();
                    courseOpt.get().setInstructor(prof);
                    prof.addTeachingCourse(courseOpt.get());
                    ConsoleColor.println("Professor assigned to course successfully!", ConsoleColor.GREEN);
                } else {
                    ConsoleColor.println("Error: Invalid Professor ID.", ConsoleColor.RED);
                }

            } else {
                String courseCode = InputValidator.readStringNotEmpty("Enter Course Code to delete: ").toUpperCase();
                Optional<Course> courseOpt = db.getCourseByCode(courseCode);
                if (courseOpt.isPresent()) {
                    if (InputValidator.readConfirmation("Are you sure you want to delete course " + courseCode + "?")) {
                        db.removeCourse(courseOpt.get());
                        ConsoleColor.println("Course deleted successfully.", ConsoleColor.GREEN);
                    }
                } else {
                    ConsoleColor.println("Error: Course not found.", ConsoleColor.RED);
                }
            }
            InputValidator.pressEnterToContinue();
        }
    }

    // --- Student Management ---
    private void manageStudents() {
        while (true) {
            ConsoleColor.clearScreen();
            ConsoleColor.printHeader("STUDENT MANAGEMENT");

            ConsoleTable table = new ConsoleTable();
            table.setHeaders("ID", "Student Name", "Username", "Department", "GPA", "Enrolled Courses");
            for (Student s : db.getStudents()) {
                table.addRow(
                        s.getId(),
                        s.getFullName(),
                        s.getUsername(),
                        s.getDepartment().getCode(),
                        String.format("%.2f", s.calculateGPA()),
                        String.valueOf(s.getEnrolledCourses().size())
                );
            }
            table.print();

            System.out.println("\n1. Register New Student");
            System.out.println("2. Enroll Student in Course (Manual)");
            System.out.println("3. Drop Student from Course (Manual)");
            System.out.println("4. Back to Admin Menu");
            System.out.println();

            int choice = InputValidator.readInt("Choose action: ", 1, 4);
            if (choice == 4) break;

            if (choice == 1) {
                String name = InputValidator.readStringNotEmpty("Enter Student Full Name: ");
                String username = InputValidator.readStringNotEmpty("Enter Username: ").toLowerCase();
                
                if (db.getUserByUsername(username).isPresent()) {
                    ConsoleColor.println("Error: Username already exists.", ConsoleColor.RED);
                    InputValidator.pressEnterToContinue();
                    continue;
                }

                String password = InputValidator.readStringNotEmpty("Enter Password: ");
                String deptCode = InputValidator.readStringNotEmpty("Enter Department Code: ").toUpperCase();
                Optional<Department> deptOpt = db.getDepartmentByCode(deptCode);
                if (!deptOpt.isPresent()) {
                    ConsoleColor.println("Error: Department code not found.", ConsoleColor.RED);
                    InputValidator.pressEnterToContinue();
                    continue;
                }

                Student s = service.registerStudent(name, username, password, deptOpt.get(), "2026-06-07");
                ConsoleColor.println("Student registered successfully! Generated ID: " + s.getId(), ConsoleColor.GREEN);

            } else if (choice == 2) {
                String studentId = InputValidator.readStringNotEmpty("Enter Student ID: ");
                String courseCode = InputValidator.readStringNotEmpty("Enter Course Code: ").toUpperCase();
                
                StringBuilder msg = new StringBuilder();
                if (service.enrollStudentInCourse(studentId, courseCode, msg)) {
                    ConsoleColor.println(msg.toString(), ConsoleColor.GREEN);
                } else {
                    ConsoleColor.println("Error: " + msg.toString(), ConsoleColor.RED);
                }

            } else {
                String studentId = InputValidator.readStringNotEmpty("Enter Student ID: ");
                String courseCode = InputValidator.readStringNotEmpty("Enter Course Code: ").toUpperCase();

                StringBuilder msg = new StringBuilder();
                if (service.dropStudentFromCourse(studentId, courseCode, msg)) {
                    ConsoleColor.println(msg.toString(), ConsoleColor.GREEN);
                } else {
                    ConsoleColor.println("Error: " + msg.toString(), ConsoleColor.RED);
                }
            }
            InputValidator.pressEnterToContinue();
        }
    }

    // --- Professor Management ---
    private void manageProfessors() {
        while (true) {
            ConsoleColor.clearScreen();
            ConsoleColor.printHeader("PROFESSOR MANAGEMENT");

            ConsoleTable table = new ConsoleTable();
            table.setHeaders("ID", "Professor Name", "Department", "Specialization", "Salary", "Teaching Courses");
            for (Professor p : db.getProfessors()) {
                table.addRow(
                        p.getId(),
                        p.getFullName(),
                        p.getDepartment().getCode(),
                        p.getSpecialization(),
                        String.format("$%,.2f", p.getSalary()),
                        String.valueOf(p.getTeachingCourses().size())
                );
            }
            table.print();

            System.out.println("\n1. Recruit/Add Professor");
            System.out.println("2. Back to Admin Menu");
            System.out.println();

            int choice = InputValidator.readInt("Choose action: ", 1, 2);
            if (choice == 2) break;

            String name = InputValidator.readStringNotEmpty("Enter Professor Full Name: ");
            String username = InputValidator.readStringNotEmpty("Enter Username: ").toLowerCase();
            
            if (db.getUserByUsername(username).isPresent()) {
                ConsoleColor.println("Error: Username already exists.", ConsoleColor.RED);
                InputValidator.pressEnterToContinue();
                continue;
            }

            String password = InputValidator.readStringNotEmpty("Enter Password: ");
            String deptCode = InputValidator.readStringNotEmpty("Enter Department Code: ").toUpperCase();
            Optional<Department> deptOpt = db.getDepartmentByCode(deptCode);
            if (!deptOpt.isPresent()) {
                ConsoleColor.println("Error: Department code not found.", ConsoleColor.RED);
                InputValidator.pressEnterToContinue();
                continue;
            }

            String spec = InputValidator.readStringNotEmpty("Enter Specialization: ");
            double salary = InputValidator.readDouble("Enter Base Annual Salary: ", 30000, 250000);

            Professor p = service.registerProfessor(name, username, password, deptOpt.get(), spec, salary);
            ConsoleColor.println("Professor added successfully! Generated ID: " + p.getId(), ConsoleColor.GREEN);
            InputValidator.pressEnterToContinue();
        }
    }

    // --- Stats Summary ---
    private void showDetailedStats(Map<String, Object> stats) {
        ConsoleColor.clearScreen();
        ConsoleColor.printHeader("DETAILED SYSTEM STATISTICS");
        
        System.out.println(ConsoleColor.colorize("University Distribution Summary:", ConsoleColor.CYAN_BOLD));
        System.out.println("  • Total Student Enrollment : " + stats.get("totalStudents"));
        System.out.println("  • Total Employed Faculty   : " + stats.get("totalProfessors"));
        System.out.println("  • Total Courses Cataloged  : " + stats.get("totalCourses"));
        System.out.printf("  • Overall Student GPA      : %.2f / 4.00\n", stats.get("averageGPA"));
        System.out.println("  • Most Enrolled Course     : " + stats.get("topCourse") + " (" + stats.get("topCourseCount") + " students)");
        System.out.println();

        System.out.println(ConsoleColor.colorize("Department-wise Enrollments (Visualization):", ConsoleColor.CYAN_BOLD));
        @SuppressWarnings("unchecked")
        Map<String, Integer> deptCounts = (Map<String, Integer>) stats.get("departmentStudents");
        for (Map.Entry<String, Integer> entry : deptCounts.entrySet()) {
            StringBuilder bar = new StringBuilder();
            for (int i = 0; i < entry.getValue(); i++) {
                bar.append("■");
            }
            System.out.printf("  %-10s [%2d students] %s\n", 
                    entry.getKey(), 
                    entry.getValue(), 
                    ConsoleColor.colorize(bar.toString(), ConsoleColor.GREEN_BOLD));
        }

        System.out.println();
        InputValidator.pressEnterToContinue();
    }
}
