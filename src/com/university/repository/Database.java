package com.university.repository;

import com.university.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Database {
    private static Database instance;

    private List<User> users;
    private List<Department> departments;
    private List<Course> courses;

    private Database() {
        users = new ArrayList<>();
        departments = new ArrayList<>();
        courses = new ArrayList<>();
        initializeMockData();
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Student> getStudents() {
        return users.stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .collect(Collectors.toList());
    }

    public List<Professor> getProfessors() {
        return users.stream()
                .filter(u -> u instanceof Professor)
                .map(u -> (Professor) u)
                .collect(Collectors.toList());
    }

    public Optional<User> getUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    public Optional<User> getUserById(String id) {
        return users.stream()
                .filter(u -> u.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    public Optional<Department> getDepartmentByCode(String code) {
        return departments.stream()
                .filter(d -> d.getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    public Optional<Course> getCourseByCode(String code) {
        return courses.stream()
                .filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addDepartment(Department department) {
        departments.add(department);
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        // Clean up course from enrolled students
        for (Student student : getStudents()) {
            student.dropCourse(course);
        }
        // Clean up course from teaching professors
        if (course.getInstructor() != null) {
            course.getInstructor().removeTeachingCourse(course);
        }
    }

    private void initializeMockData() {
        // 1. Add Default Admin
        users.add(new User("A001", "admin", "admin123", "Chief Administrator", Role.ADMIN));

        // 2. Create Departments
        Department cs = new Department("CS", "Computer Science & Engineering");
        Department ee = new Department("EE", "Electrical Engineering");
        Department me = new Department("ME", "Mechanical Engineering");
        
        departments.add(cs);
        departments.add(ee);
        departments.add(me);

        // 3. Create Professors
        Professor profTuring = new Professor("P001", "turing", "prof123", "Dr. Alan Turing", cs, "Theoretical Computation", 95000.0);
        Professor profLovelace = new Professor("P002", "lovelace", "prof123", "Dr. Ada Lovelace", cs, "Algorithms & Programming", 92000.0);
        Professor profTesla = new Professor("P003", "tesla", "prof123", "Dr. Nikola Tesla", ee, "Electromagnetism", 88000.0);
        Professor profEinstein = new Professor("P004", "einstein", "prof123", "Dr. Albert Einstein", ee, "Quantum Physics", 99000.0);

        users.add(profTuring);
        users.add(profLovelace);
        users.add(profTesla);
        users.add(profEinstein);

        // Set Department Heads
        cs.setHeadOfDepartment(profTuring);
        ee.setHeadOfDepartment(profTesla);
        me.setHeadOfDepartment(null); // Head position vacant

        // 4. Create Courses
        Course cs101 = new Course("CS101", "Introduction to Programming", 4, cs, profLovelace, 30);
        Course cs201 = new Course("CS201", "Design and Analysis of Algorithms", 4, cs, profTuring, 25);
        Course ee101 = new Course("EE101", "Fundamentals of AC/DC Circuits", 3, ee, profTesla, 20);
        Course phy102 = new Course("PHY102", "Modern Physics & Relativity", 4, ee, profEinstein, 15);
        Course me101 = new Course("ME101", "Engineering Thermodynamics", 3, me, null, 25); // Unassigned instructor initially

        courses.add(cs101);
        courses.add(cs201);
        courses.add(ee101);
        courses.add(phy102);
        courses.add(me101);

        // Associate courses with professors
        profLovelace.addTeachingCourse(cs101);
        profTuring.addTeachingCourse(cs201);
        profTesla.addTeachingCourse(ee101);
        profEinstein.addTeachingCourse(phy102);

        // 5. Create Students
        Student s1 = new Student("S001", "hema", "student123", "Hemachandra S", cs, "2024-09-01");
        Student s2 = new Student("S002", "alice", "student123", "Alice Smith", cs, "2024-09-01");
        Student s3 = new Student("S003", "bob", "student123", "Bob Johnson", ee, "2024-09-01");
        Student s4 = new Student("S004", "charlie", "student123", "Charlie Brown", me, "2025-01-15");
        Student s5 = new Student("S005", "diana", "student123", "Diana Prince", cs, "2025-01-15");

        users.add(s1);
        users.add(s2);
        users.add(s3);
        users.add(s4);
        users.add(s5);

        // Enroll students & Set initial grades/attendance for rich display
        // Student 1 (Hema)
        enroll(s1, cs101);
        enroll(s1, cs201);
        enroll(s1, phy102);
        s1.setGrade("CS101", "A");
        s1.setGrade("CS201", "A-");
        s1.setGrade("PHY102", "B+");
        s1.setAttendance("CS101", 94.5);
        s1.setAttendance("CS201", 92.0);
        s1.setAttendance("PHY102", 88.0);
        s1.payFees(2500.0);

        // Student 2 (Alice)
        enroll(s2, cs101);
        enroll(s2, cs201);
        s2.setGrade("CS101", "B+");
        s2.setGrade("CS201", "A");
        s2.setAttendance("CS101", 98.0);
        s2.setAttendance("CS201", 100.0);
        s2.payFees(4500.0); // Paid in full

        // Student 3 (Bob)
        enroll(s3, ee101);
        enroll(s3, phy102);
        s3.setGrade("EE101", "B-");
        s3.setGrade("PHY102", "C+");
        s3.setAttendance("EE101", 72.5); // Under attendance warnings
        s3.setAttendance("PHY102", 80.0);
        s3.payFees(1000.0);

        // Student 4 (Charlie)
        enroll(s4, me101);
        s4.setAttendance("ME101", 85.0);
        
        // Student 5 (Diana)
        enroll(s5, cs101);
        s5.setGrade("CS101", "A");
        s5.setAttendance("CS101", 96.0);
        s5.payFees(4500.0);
    }

    private void enroll(Student student, Course course) {
        student.enrollInCourse(course);
        course.enrollStudent(student);
    }
}
