package com.university.model;

public class Department {
    private String code;
    private String name;
    private Professor headOfDepartment;

    public Department(String code, String name) {
        this.code = code;
        this.name = name;
        this.headOfDepartment = null;
    }

    public Department(String code, String name, Professor headOfDepartment) {
        this.code = code;
        this.name = name;
        this.headOfDepartment = headOfDepartment;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Professor getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(Professor headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}
