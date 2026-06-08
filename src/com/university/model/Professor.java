package com.university.model;

import java.util.ArrayList;
import java.util.List;

public class Professor extends User {
    private Department department;
    private String specialization;
    private double salary;
    private List<Course> teachingCourses;

    public Professor(String id, String username, String password, String fullName, Department department, String specialization, double salary) {
        super(id, username, password, fullName, Role.PROFESSOR);
        this.department = department;
        this.specialization = specialization;
        this.salary = salary;
        this.teachingCourses = new ArrayList<>();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public List<Course> getTeachingCourses() {
        return teachingCourses;
    }

    public void addTeachingCourse(Course course) {
        if (!teachingCourses.contains(course)) {
            teachingCourses.add(course);
        }
    }

    public void removeTeachingCourse(Course course) {
        teachingCourses.remove(course);
    }
}
