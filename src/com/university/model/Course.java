package com.university.model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String code;
    private String title;
    private int credits;
    private Department department;
    private Professor instructor;
    private int maxCapacity;
    private List<Student> enrolledStudents;

    public Course(String code, String title, int credits, Department department, Professor instructor, int maxCapacity) {
        this.code = code;
        this.title = title;
        this.credits = credits;
        this.department = department;
        this.instructor = instructor;
        this.maxCapacity = maxCapacity;
        this.enrolledStudents = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Professor getInstructor() {
        return instructor;
    }

    public void setInstructor(Professor instructor) {
        this.instructor = instructor;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public boolean enrollStudent(Student student) {
        if (enrolledStudents.size() < maxCapacity && !enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
            return true;
        }
        return false;
    }

    public void removeStudent(Student student) {
        enrolledStudents.remove(student);
    }

    public int getAvailableSeats() {
        return maxCapacity - enrolledStudents.size();
    }
}
