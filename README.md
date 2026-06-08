# Academic Nexus: University Management System

A highly polished, interactive, and professionally designed Java console-based University Management System. This project showcases robust Object-Oriented Programming (OOP) concepts, design patterns, layered clean architecture, validation frameworks, and a beautiful command-line interface (CLI) using ANSI colors and Unicode box-drawing tables.

---

## 🚀 Features & User Roles

The system supports three distinct login roles, each with its own customized, color-coded dashboard interface:

### 1. 🔑 Administrator
- **Department Management**: Add departments and assign or reassign Head of Departments (HoD).
- **Course Cataloging**: Create and list courses, configure credit hours, maximum capacity, and assign instructors.
- **Student & Faculty Rosters**: Register new students, hire faculty members, and view consolidated directories.
- **Analytics Dashboard**: Access instant university statistics (student enrollment counts, department distributions, class caps, and average GPA) with styled bar chart visualizers.

### 2. 👨‍🏫 Faculty / Professor
- **Teaching Schedule**: View assigned teaching modules, credits, and student capacities.
- **Class Rosters**: Look up real-time lists of students enrolled in each teaching course.
- **Grade Management**: Submit and update letter grades (A+, A, B, etc.) for students.
- **Attendance Registry**: Log and monitor student attendance percentages with threshold warning markers.

### 3. 🎓 Student
- **Academic Dashboard**: Look up enrolled courses, credit values, assigned professors, letter grades, and attendance.
- **GPA Calculator**: Real-time GPA computation based on course credits and letter-grade weights.
- **Course Enrollment**: Search, register for, or drop courses dynamically based on department directories and seat capacity.
- **Fee Center**: View statement charges (semester base fee + credits scale) and process simulated tuition payments.

---

## 🛠️ Architecture & Design Patterns

To maintain a clean separation of concerns and maintainable code, the application is divided into a **Layered Architecture**:

- **Model Layer (`com.university.model`)**: Pure Domain Objects (`Student`, `Professor`, `Course`, `Department`) representing core business concepts.
- **Repository Layer (`com.university.repository`)**: Simulates a persistence engine using the **Singleton** pattern for in-memory collections and pre-loaded mock data.
- **Service Layer (`com.university.service`)**: Holds business rules, validation constraints (for registration, course limits, grading ranges), and credential check routines.
- **UI/View Layer (`com.university.ui`)**: Renders dashboards, login screens, prompt prompts, and maps inputs.
- **Utility Layer (`com.university.util`)**: Formats complex tables using box-drawing characters, handles ANSI styling, and validates user keyboard inputs.

### 💡 Core Design Patterns Used
- **Singleton**: Ensures a single source of data truth across all repository lookups.
- **Inheritance & Polymorphism**: Extends a base `User` model to handle authentication details for `Student` and `Professor` profiles.

---

## 🏃 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher.

### Compilation & Execution
1. Clone the repository and navigate to the project directory:
   ```bash
   git clone https://github.com/hemu1196/UniversityManagmentSystem_java.git
   cd UniversityManagmentSystem_java
   ```
2. Compile the source code:
   ```bash
   mkdir -p bin
   javac -d bin $(find src -name "*.java")
   ```
3. Run the application:
   ```bash
   java -cp bin com.university.Main
   ```

---

## 🔑 Quick Login Directory (For Reviewers)

Use these credentials to explore the different dashboards:

| Portal Role | Username | Password |
| :--- | :--- | :--- |
| **System Administrator** | `admin` | `admin123` |
| **Faculty Member** | `turing` | `prof123` |
| **Student Profile** | `hema` | `student123` |

---

## 📝 License
Distributed under the MIT License. See `LICENSE` for more information.
