package com.university.service;

import com.university.model.*;
import com.university.repository.Database;

import java.util.*;

public class UniversityService {
    private Database db;

    public UniversityService() {
        this.db = Database.getInstance();
    }

    // --- Student Enrollments ---
    public boolean enrollStudentInCourse(String studentId, String courseCode, StringBuilder msg) {
        Optional<User> studentOpt = db.getUserById(studentId);
        Optional<Course> courseOpt = db.getCourseByCode(courseCode);

        if (!studentOpt.isPresent() || !(studentOpt.get() instanceof Student)) {
            msg.append("Student not found.");
            return false;
        }
        if (!courseOpt.isPresent()) {
            msg.append("Course not found.");
            return false;
        }

        Student student = (Student) studentOpt.get();
        Course course = courseOpt.get();

        if (course.getEnrolledStudents().contains(student)) {
            msg.append("Student is already enrolled in this course.");
            return false;
        }

        if (course.getAvailableSeats() <= 0) {
            msg.append("Course capacity reached. No seats available.");
            return false;
        }

        student.enrollInCourse(course);
        course.enrollStudent(student);
        msg.append("Enrolled successfully!");
        return true;
    }

    public boolean dropStudentFromCourse(String studentId, String courseCode, StringBuilder msg) {
        Optional<User> studentOpt = db.getUserById(studentId);
        Optional<Course> courseOpt = db.getCourseByCode(courseCode);

        if (!studentOpt.isPresent() || !(studentOpt.get() instanceof Student)) {
            msg.append("Student not found.");
            return false;
        }
        if (!courseOpt.isPresent()) {
            msg.append("Course not found.");
            return false;
        }

        Student student = (Student) studentOpt.get();
        Course course = courseOpt.get();

        if (!course.getEnrolledStudents().contains(student)) {
            msg.append("Student is not enrolled in this course.");
            return false;
        }

        student.dropCourse(course);
        course.removeStudent(student);
        msg.append("Dropped course successfully.");
        return true;
    }

    // --- Registration Helpers ---
    public String generateNextId(Role role) {
        char prefix = 'U';
        int maxNum = 0;
        List<User> list = db.getUsers();

        if (role == Role.STUDENT) prefix = 'S';
        else if (role == Role.PROFESSOR) prefix = 'P';
        else if (role == Role.ADMIN) prefix = 'A';

        for (User u : list) {
            if (u.getId().charAt(0) == prefix) {
                try {
                    int num = Integer.parseInt(u.getId().substring(1));
                    if (num > maxNum) {
                        maxNum = num;
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("%c%03d", prefix, maxNum + 1);
    }

    public Student registerStudent(String fullName, String username, String password, Department department, String enrollmentDate) {
        String id = generateNextId(Role.STUDENT);
        Student student = new Student(id, username, password, fullName, department, enrollmentDate);
        db.addUser(student);
        return student;
    }

    public Professor registerProfessor(String fullName, String username, String password, Department department, String specialization, double salary) {
        String id = generateNextId(Role.PROFESSOR);
        Professor professor = new Professor(id, username, password, fullName, department, specialization, salary);
        db.addUser(professor);
        return professor;
    }

    public Course createCourse(String code, String title, int credits, Department department, Professor instructor, int capacity) {
        Course course = new Course(code, title, credits, department, instructor, capacity);
        db.addCourse(course);
        if (instructor != null) {
            instructor.addTeachingCourse(course);
        }
        return course;
    }

    // --- Grade & Attendance Helpers ---
    public boolean updateStudentGrade(String studentId, String courseCode, String grade, StringBuilder msg) {
        Optional<User> studentOpt = db.getUserById(studentId);
        if (!studentOpt.isPresent() || !(studentOpt.get() instanceof Student)) {
            msg.append("Student not found.");
            return false;
        }

        Student student = (Student) studentOpt.get();
        if (!student.getGrades().containsKey(courseCode)) {
            msg.append("Student is not enrolled in course " + courseCode + ".");
            return false;
        }

        // Validate grade format
        List<String> validGrades = Arrays.asList("A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "F", "N/A");
        if (!validGrades.contains(grade.toUpperCase())) {
            msg.append("Invalid grade. Choose from: A+, A, A-, B+, B, B-, C+, C, C-, D+, D, F, N/A");
            return false;
        }

        student.setGrade(courseCode, grade.toUpperCase());
        msg.append("Grade updated successfully.");
        return true;
    }

    public boolean updateStudentAttendance(String studentId, String courseCode, double percentage, StringBuilder msg) {
        Optional<User> studentOpt = db.getUserById(studentId);
        if (!studentOpt.isPresent() || !(studentOpt.get() instanceof Student)) {
            msg.append("Student not found.");
            return false;
        }

        Student student = (Student) studentOpt.get();
        if (!student.getAttendance().containsKey(courseCode)) {
            msg.append("Student is not enrolled in course " + courseCode + ".");
            return false;
        }

        student.setAttendance(courseCode, percentage);
        msg.append("Attendance updated successfully.");
        return true;
    }

    // --- Stats calculation ---
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();
        List<Student> students = db.getStudents();
        List<Professor> professors = db.getProfessors();
        List<Course> courses = db.getCourses();

        stats.put("totalStudents", students.size());
        stats.put("totalProfessors", professors.size());
        stats.put("totalCourses", courses.size());

        // Average GPA
        double sumGPA = 0;
        int studentsWithGPA = 0;
        for (Student s : students) {
            double gpa = s.calculateGPA();
            if (gpa > 0.0 || !s.getGrades().isEmpty()) {
                sumGPA += gpa;
                studentsWithGPA++;
            }
        }
        stats.put("averageGPA", studentsWithGPA > 0 ? (sumGPA / studentsWithGPA) : 0.0);

        // Department-wise student counts
        Map<String, Integer> deptCounts = new HashMap<>();
        for (Student s : students) {
            String deptName = s.getDepartment() != null ? s.getDepartment().getCode() : "Unassigned";
            deptCounts.put(deptName, deptCounts.getOrDefault(deptName, 0) + 1);
        }
        stats.put("departmentStudents", deptCounts);

        // Top Course by enrollment
        Course topCourse = null;
        int maxEnrollment = -1;
        for (Course c : courses) {
            int size = c.getEnrolledStudents().size();
            if (size > maxEnrollment) {
                maxEnrollment = size;
                topCourse = c;
            }
        }
        stats.put("topCourse", topCourse != null ? topCourse.getTitle() + " (" + topCourse.getCode() + ")" : "N/A");
        stats.put("topCourseCount", maxEnrollment != -1 ? maxEnrollment : 0);

        return stats;
    }
}
