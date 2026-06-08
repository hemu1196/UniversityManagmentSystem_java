import time
import sys

# ANSI Colors
RESET = "\033[0;37m"
CYAN = "\033[0;36m"
CYAN_BOLD = "\033[1;36m"
WHITE_BOLD = "\033[1;37m"
YELLOW = "\033[0;33m"
YELLOW_BOLD = "\033[1;33m"
GREEN = "\033[0;32m"
GREEN_BOLD = "\033[1;32m"
RED = "\033[0;31m"
RED_BOLD = "\033[1;31m"

def print_header(title):
    border = "======================================================================"
    print(f"{CYAN_BOLD}{border}{RESET}")
    # center title
    padding = (len(border) - len(title)) // 2
    print(f"{WHITE_BOLD}{' ' * padding}{title}{RESET}")
    print(f"{CYAN_BOLD}{border}{RESET}")

def press_enter_to_continue():
    print(f"\n{YELLOW_BOLD}Press Enter to continue...{RESET}")
    # Mocking user press enter delay
    time.sleep(1.5)
    print()

def simulate_login_screen():
    print_header("ACADEMIC NEXUS: UNIVERSITY SYSTEM")
    print(f"\n{YELLOW}---- System Quick-Access Directory (For Testing) ----{RESET}")
    print("  • Administrator Portal : username: [ admin  ] | password: [ admin123   ]")
    print("  • Faculty Portal       : username: [ turing ] | password: [ prof123    ]")
    print("  • Student Portal       : username: [ hema   ] | password: [ student123 ]")
    print("-----------------------------------------------------")
    print("\n1. Login to Portal")
    print("2. Exit Application\n")
    print(f"{YELLOW_BOLD}Select Option (1-2): {RESET}1\n")
    print(f"{YELLOW_BOLD}Enter Username: {RESET}admin")
    print(f"{YELLOW_BOLD}Enter Password: {RESET}******")
    print(f"\n{YELLOW}Authenticating...{RESET}")
    time.sleep(0.5)
    print(f"{GREEN}Authentication Successful!{RESET}")
    time.sleep(0.5)

def simulate_admin_dashboard():
    print_header("ADMINISTRATOR CONTROL PANEL")
    print(f"Welcome, {CYAN_BOLD}Chief Administrator{RESET}")
    print(f"Role: {RED_BOLD}SYSTEM ADMIN{RESET}\n")
    print(f"[ Stats Dashboard: Students: 5 | Faculty: 4 | Courses: 5 | Avg GPA: 3.42 ]\n")
    print("1. Department Management")
    print("2. Course Management")
    print("3. Student Management")
    print("4. Professor/Faculty Management")
    print("5. Overall System Stats & Visualizations")
    print("6. Logout\n")
    print(f"{YELLOW_BOLD}Enter your choice (1-6): {RESET}5\n")

def simulate_admin_detailed_stats():
    print_header("DETAILED SYSTEM STATISTICS")
    print(f"{CYAN_BOLD}University Distribution Summary:{RESET}")
    print("  • Total Student Enrollment : 5")
    print("  • Total Employed Faculty   : 4")
    print("  • Total Courses Cataloged  : 5")
    print("  • Overall Student GPA      : 3.42 / 4.00")
    print("  • Most Enrolled Course     : Introduction to Programming (CS101) (3 students)")
    print()
    print(f"{CYAN_BOLD}Department-wise Enrollments (Visualization):{RESET}")
    print(f"  CS         [ 3 students] {GREEN_BOLD}■■■{RESET}")
    print(f"  EE         [ 1 students] {GREEN_BOLD}■{RESET}")
    print(f"  ME         [ 1 students] {GREEN_BOLD}■{RESET}")
    press_enter_to_continue()

def simulate_department_table():
    print_header("DEPARTMENT MANAGEMENT")
    print(f"{CYAN}┌───────────┬──────────────────────────────────┬──────────────────────┐{RESET}")
    print(f"{CYAN}│{WHITE_BOLD} Dept Code {CYAN}│{WHITE_BOLD} Department Name                  {CYAN}│{WHITE_BOLD} Head of Department   {CYAN}│{RESET}")
    print(f"{CYAN}├───────────┼──────────────────────────────────┼──────────────────────┤{RESET}")
    print(f"{CYAN}│{RESET} CS        {CYAN}│{RESET} Computer Science & Engineering    {CYAN}│{RESET} Dr. Alan Turing      {CYAN}│{RESET}")
    print(f"{CYAN}│{RESET} EE        {CYAN}│{RESET} Electrical Engineering            {CYAN}│{RESET} Dr. Nikola Tesla     {CYAN}│{RESET}")
    print(f"{CYAN}│{RESET} ME        {CYAN}│{RESET} Mechanical Engineering            {CYAN}│{RESET} VACANT               {CYAN}│{RESET}")
    print(f"{CYAN}└───────────┴──────────────────────────────────┴──────────────────────┘{RESET}")
    print("\n1. Add New Department")
    print("2. Assign Head of Department")
    print("3. Back to Admin Menu\n")
    print(f"{YELLOW_BOLD}Choose action: {RESET}3")
    time.sleep(0.5)

def simulate_course_table():
    print_header("COURSE MANAGEMENT")
    print(f"{CYAN}┌─────────────┬─────────────────────────────────────┬─────────┬──────────┬──────────────────────┬──────────┬────────────┐{RESET}")
    print(f"{CYAN}│{WHITE_BOLD} Course Code {CYAN}│{WHITE_BOLD} Course Title                         {CYAN}│{WHITE_BOLD} Credits {CYAN}│{WHITE_BOLD} Dept     {CYAN}│{WHITE_BOLD} Instructor           {CYAN}│{WHITE_BOLD} Enrolled {CYAN}│{WHITE_BOLD} Seats Left  {CYAN}│{RESET}")
    print(f"{CYAN}├─────────────┼─────────────────────────────────────┼─────────┼──────────┼──────────────────────┼──────────┼────────────┤{RESET}")
    print(f"{CYAN}│{RESET} CS101       {CYAN}│{RESET} Introduction to Programming         {CYAN}│{RESET} 4       {CYAN}│{RESET} CS       {CYAN}│{RESET} Dr. Ada Lovelace     {CYAN}│{RESET} 3/30      {CYAN}│{RESET} 27          {CYAN}│{RESET}")
    print(f"{CYAN}│{RESET} CS201       {CYAN}│{RESET} Design and Analysis of Algorithms    {CYAN}│{RESET} 4       {CYAN}│{RESET} CS       {CYAN}│{RESET} Dr. Alan Turing      {CYAN}│{RESET} 2/25      {CYAN}│{RESET} 23          {CYAN}│{RESET}")
    print(f"{CYAN}│{RESET} EE101       {CYAN}│{RESET} Fundamentals of AC/DC Circuits       {CYAN}│{RESET} 3       {CYAN}│{RESET} EE       {CYAN}│{RESET} Dr. Nikola Tesla     {CYAN}│{RESET} 1/20      {CYAN}│{RESET} 19          {CYAN}│{RESET}")
    print(f"{CYAN}│{RESET} PHY102      {CYAN}│{RESET} Modern Physics & Relativity          {CYAN}│{RESET} 4       {CYAN}│{RESET} EE       {CYAN}│{RESET} Dr. Albert Einstein  {CYAN}│{RESET} 2/15      {CYAN}│{RESET} 13          {CYAN}│{RESET}")
    print(f"{CYAN}│{RESET} ME101       {CYAN}│{RESET} Engineering Thermodynamics           {CYAN}│{RESET} 3       {CYAN}│{RESET} ME       {CYAN}│{RESET} UNASSIGNED           {CYAN}│{RESET} 1/25      {CYAN}│{RESET} 24          {CYAN}│{RESET}")
    print(f"{CYAN}└─────────────┴─────────────────────────────────────┴─────────┴──────────┴──────────────────────┴──────────┴────────────┘{RESET}")
    print("\n1. Create New Course")
    print("2. Assign Instructor to Course")
    print("3. Delete/Remove Course")
    print("4. Back to Admin Menu\n")
    print(f"{YELLOW_BOLD}Choose action: {RESET}4")
    time.sleep(0.5)

def simulate_student_dashboard():
    print_header("STUDENT PORTAL - ACADEMICS & SERVICES")
    print(f"Welcome, {CYAN_BOLD}Hemachandra S{RESET}")
    print(f"ID: {WHITE_BOLD}S001{RESET} | Department: {WHITE_BOLD}Computer Science & Engineering{RESET}")
    print(f"Current Academic GPA: {GREEN_BOLD}3.88{RESET}\n")
    print("1. Academic Dashboard & Report Card")
    print("2. Enroll in Course")
    print("3. Drop Course")
    print("4. Tuition & Fee Center")
    print("5. Logout\n")
    print(f"{YELLOW_BOLD}Enter choice (1-5): {RESET}1\n")

def simulate_student_report_card():
    print_header("ACADEMIC DASHBOARD & REPORT CARD")
    print(f"{CYAN}┌─────────────┬─────────────────────────────────────┬─────────┬──────────────────────┬───────┬────────────┐{RESET}")
    print(f"{CYAN}│{WHITE_BOLD} Course Code {CYAN}│{WHITE_BOLD} Course Title                         {CYAN}│{WHITE_BOLD} Credits {CYAN}│{WHITE_BOLD} Instructor           {CYAN}│{WHITE_BOLD} Grade {CYAN}│{WHITE_BOLD} Attendance  {CYAN}│{RESET}")
    print(f"{CYAN}├─────────────┼─────────────────────────────────────┼─────────┼──────────────────────┼───────┼────────────┤{RESET}")
    print(f"{CYAN}│{RESET} CS101       {CYAN}│{RESET} Introduction to Programming         {CYAN}│{RESET} 4       {CYAN}│{RESET} Dr. Ada Lovelace     {CYAN}│{RESET} A     {CYAN}│{GREEN} 94.5%      {CYAN}│{RESET}")
    print(f"{CYAN}│{RESET} CS201       {CYAN}│{RESET} Design and Analysis of Algorithms    {CYAN}│{RESET} 4       {CYAN}│{RESET} Dr. Alan Turing      {CYAN}│{RESET} A-    {CYAN}│{GREEN} 92.0%      {CYAN}│{RESET}")
    print(f"{CYAN}│{RESET} PHY102      {CYAN}│{RESET} Modern Physics & Relativity          {CYAN}│{RESET} 4       {CYAN}│{RESET} Dr. Albert Einstein  {CYAN}│{RESET} B+    {CYAN}│{GREEN} 88.0%      {CYAN}│{RESET}")
    print(f"{CYAN}└─────────────┴─────────────────────────────────────┴─────────┴──────────────────────┴───────┴────────────┘{RESET}")
    print(f"\nTotal Enrolled Credits : 12")
    print(f"Current Semester GPA   : {GREEN_BOLD}3.88{RESET}")
    press_enter_to_continue()

def simulate_student_fees():
    print_header("TUITION & FINANCIAL CENTER")
    print(f"{CYAN_BOLD}Tuition Summary Statement:{RESET}")
    print(f"  • Semester Base Fee    : $4,000.00")
    print(f"  • Course Enroll. Fees  : $1,500.00")
    print(f"  • Total Account Cost   : $5,500.00")
    print("  --------------------------------------")
    print(f"  • Total Fees Paid      : {GREEN}$2,500.00{RESET}")
    print(f"  • Current Balance Due  : {RED_BOLD}$3,000.00{RESET}\n")
    print("1. Make Simulated Payment")
    print("2. Back to Student Menu\n")
    print(f"{YELLOW_BOLD}Choose action: {RESET}2")
    time.sleep(0.5)

def simulate_professor_dashboard():
    print_header("FACULTY PORTAL - Computer Science & Engineering")
    print(f"Welcome, {CYAN_BOLD}Dr. Alan Turing{RESET}")
    print(f"Specialization: {WHITE_BOLD}Theoretical Computation{RESET}\n")
    print("1. View Teaching Schedule")
    print("2. View Class Roster")
    print("3. Grade Student")
    print("4. Record Attendance")
    print("5. Logout\n")
    print(f"{YELLOW_BOLD}Enter choice (1-5): {RESET}2\n")

def simulate_professor_roster():
    print_header("ROSTER FOR: CS201 - Design and Analysis of Algorithms")
    print(f"{CYAN}┌────────────┬──────────────────────┬───────┬────────────┐{RESET}")
    print(f"{CYAN}│{WHITE_BOLD} Student ID {CYAN}│{WHITE_BOLD} Full Name             {CYAN}│{WHITE_BOLD} Grade {CYAN}│{WHITE_BOLD} Attendance {CYAN}│{RESET}")
    print(f"{CYAN}├────────────┼──────────────────────┼───────┼────────────┤{RESET}")
    print(f"{CYAN}│{RESET} S001       {CYAN}│{RESET} Hemachandra S         {CYAN}│{RESET} A-    {CYAN}│{GREEN} 92.0%      {CYAN}│{RESET}")
    print(f"{CYAN}│{RESET} S002       {CYAN}│{RESET} Alice Smith           {CYAN}│{RESET} A     {CYAN}│{GREEN} 100.0%     {CYAN}│{RESET}")
    print(f"{CYAN}└────────────┴──────────────────────┴───────┴────────────┘{RESET}")
    press_enter_to_continue()

if __name__ == "__main__":
    simulate_login_screen()
    simulate_admin_dashboard()
    simulate_admin_detailed_stats()
    simulate_department_table()
    simulate_course_table()
    simulate_student_dashboard()
    simulate_student_report_card()
    simulate_student_fees()
    simulate_professor_dashboard()
    simulate_professor_roster()
    print(f"{CYAN_BOLD}Simulation Complete! This illustrates the terminal user interface structure and styles.{RESET}")
