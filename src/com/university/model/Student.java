package com.university.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends User {
    private Department department;
    private String enrollmentDate;
    private List<Course> enrolledCourses;
    private Map<String, String> grades; // courseCode -> grade (A, B+, etc.)
    private Map<String, Double> attendance; // courseCode -> attendance percentage
    private double feeBalance;
    private double feesPaid;

    public Student(String id, String username, String password, String fullName, Department department, String enrollmentDate) {
        super(id, username, password, fullName, Role.STUDENT);
        this.department = department;
        this.enrollmentDate = enrollmentDate;
        this.enrolledCourses = new ArrayList<>();
        this.grades = new HashMap<>();
        this.attendance = new HashMap<>();
        this.feeBalance = 4000.0; // Default tuition fees per semester
        this.feesPaid = 0.0;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void enrollInCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            grades.put(course.getCode(), "N/A"); // Default grade
            attendance.put(course.getCode(), 100.0); // Default attendance
            feeBalance += 500.0; // Extra charge per course enrollment
        }
    }

    public void dropCourse(Course course) {
        if (enrolledCourses.contains(course)) {
            enrolledCourses.remove(course);
            grades.remove(course.getCode());
            attendance.remove(course.getCode());
            feeBalance -= 500.0;
        }
    }

    public Map<String, String> getGrades() {
        return grades;
    }

    public void setGrade(String courseCode, String grade) {
        if (grades.containsKey(courseCode)) {
            grades.put(courseCode, grade);
        }
    }

    public Map<String, Double> getAttendance() {
        return attendance;
    }

    public void setAttendance(String courseCode, double percentage) {
        if (attendance.containsKey(courseCode)) {
            attendance.put(courseCode, Math.max(0.0, Math.min(100.0, percentage)));
        }
    }

    public double getFeeBalance() {
        return feeBalance;
    }

    public void payFees(double amount) {
        if (amount > 0) {
            if (amount > feeBalance) {
                feesPaid += feeBalance;
                feeBalance = 0;
            } else {
                feesPaid += amount;
                feeBalance -= amount;
            }
        }
    }

    public double getFeesPaid() {
        return feesPaid;
    }

    public double calculateGPA() {
        if (grades.isEmpty()) {
            return 0.0;
        }

        double totalPoints = 0.0;
        int totalCredits = 0;
        boolean hasGradedCourses = false;

        for (Course course : enrolledCourses) {
            String grade = grades.get(course.getCode());
            if (grade != null && !grade.equals("N/A")) {
                double gpaValue = convertGradeToGPA(grade);
                totalPoints += gpaValue * course.getCredits();
                totalCredits += course.getCredits();
                hasGradedCourses = true;
            }
        }

        if (!hasGradedCourses || totalCredits == 0) {
            return 0.0;
        }

        return totalPoints / totalCredits;
    }

    private double convertGradeToGPA(String grade) {
        switch (grade.toUpperCase()) {
            case "A+":
            case "A":
                return 4.0;
            case "A-":
                return 3.7;
            case "B+":
                return 3.3;
            case "B":
                return 3.0;
            case "B-":
                return 2.7;
            case "C+":
                return 2.3;
            case "C":
                return 2.0;
            case "C-":
                return 1.7;
            case "D+":
                return 1.3;
            case "D":
                return 1.0;
            case "F":
            default:
                return 0.0;
        }
    }
}
