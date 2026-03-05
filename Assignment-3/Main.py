
class Course:
    def __init__(self, course_name, course_number, course_section, course_term_year, course_student_count):
        self._name = course_name
        self._number = course_number
        self._section = course_section
        self._term_year = course_term_year
        self._student_count = course_student_count

    def set_name(self, course_name):
        self._name = course_name

    def set_number(self, course_number):
        self._number = course_number

    def set_section(self, course_section):
        self._section = course_section

    def set_term_year(self, course_term_year):
        self._term_year = course_term_year

    def set_student_count(self, course_student_count):
        self._student_count = course_student_count

    def print_name(self):
        print(self._name)

    def print_number(self):
        print(self._number)

    def print_section(self):
        print(self._section)

    def print_term_year(self):
        print(self._term_year)

    def print_student_count(self):
        print(self._student_count)

courses = [     # Predefined list of registered classes
    Course("Calculus 101", 1101, "W01", "Spring 2026", 30),
    Course("English 101", 1101, "04", "Summer 2026", 45),
    Course("World History 202", 2202, "J02", "Fall 2025", 20),
    Course("Government 102", 1102, "01", "Summer 2026", 35),
    Course("Philosophy 104", 3104, "02", "Fall 2026", 25)
           ]

running = True

print("Welcome to your course dashboard!")
print()

while running:
    print("What would you like to do?")
    print("1. View current registered courses.")
    print("2. Add registered course.")
    print("3. Edit registered course.")
    print("4. Delete registered course.")
    print("5. Quit program.")
    choice = input("Enter your choice (1 through 5): ")

    if choice == "":
        print("Error: Enter a valid choice!")
        continue

    choice = int(choice)

    print()
    if choice == 1:     # Print out currently registered courses
        for course in courses:
            print("Course name: ", end="")
            course.print_name()
            print("Course number: ", end="")
            course.print_number()
            print("Course section: ", end="")
            course.print_section()
            print("Course term and year: ", end="")
            course.print_term_year()
            print("Course student count: ", end="")
            course.print_student_count()
            print()

        print("You are registered for " + str(len(courses)) + " courses!")
        print()

    if choice == 2:     # Add new registered course
        name = input("Enter the course name: ")
        number = int(input("Enter the course number: "))
        section = input("Enter the course section: ")
        term_year = input("Enter the course term and year: ")
        student_count = int(input("Enter the course student count: "))

        courses.append(Course(name, number, section, term_year, student_count))

        print("Course added to your registered courses!")
        print()

    if choice == 3:     # Edit a registered course
        if len(courses) == 0:
            print("No courses registered to edit.")
            continue
        print("Which course would you like to edit?")
        for i in range(len(courses)):
            print(str(i + 1) + ". ", end="")
            courses[i].print_name()
        print()

        course_index = input("Enter your choice (1 through " + str(len(courses)) + ", enter nothing to cancel): ")

        if course_index != "":
            course_index = int(course_index)
            course_index -= 1   # Undoes displacement

            print("What would you like to edit?")
            print("1. Course name.")
            print("2. Course number.")
            print("3. Course section.")
            print("4. Course term and year.")
            print("5. Course student count.")

            edit_choice = input("Enter your choice (1 through " + str(len(courses)) + ", enter nothing to cancel): ")

            if edit_choice != "":
                edit_choice = int(edit_choice)
                new_value = input("Enter the new value: ")

                if edit_choice == 1:    # Edit name
                    courses[course_index].set_name(new_value)
                    print("Course name changed to " + new_value + "!")

                if edit_choice == 2:    # Edit number
                    new_value = int(new_value)
                    courses[course_index].set_number(new_value)
                    print("Course number changed to " + str(new_value) + "!")

                if edit_choice == 3:    # Edit section
                    courses[course_index].set_section(new_value)
                    print("Course section changed to " + new_value + "!")

                if edit_choice == 4:    # Edit term and year
                    courses[course_index].set_term_year(new_value)
                    print("Course term and year changed to " + new_value + "!")

                if edit_choice == 5:    # Edit student count
                    new_value = int(new_value)
                    courses[course_index].set_student_count(new_value)
                    print("Course student count changed to " + str(new_value) + "!")

                print()

    if choice == 4:     # Delete a registered course
        if len(courses) == 0:
            print("No courses registered to delete.")
            continue
        print("Which course would you like to delete?")
        for i in range(len(courses)):
            print(str(i + 1) + ". ", end="")
            courses[i].print_name()
        print()

        course_index = input("Enter your choice (1 through " + str(len(courses)) + ", enter nothing to cancel): ")

        if course_index != "":
            course_index = int(course_index)
            course_index -= 1   # Undoes displacement
            removed = courses[course_index]
            del courses[course_index]

            print("The following course has been deleted: ", end="")
            removed.print_name()
            print()

    if choice == 5:     # Quit system
        running = False

        print("System shutting down...")
