package seedu.address.model.classgroup;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

import seedu.address.model.person.Person;

/**
 * Represents a group of students with a specific class schedule.
 */
public class ClassGroup {
    private ArrayList<Person> studentList;
    private TimeSlot timeSlot;

    //edit these 2 classes
    /** Subject taught to this class group. */
    private String subject;
    /** List of assignment names for this class group. */
    private ArrayList<String> assignmentList;
    //

    /**
     * Constructs a {@code ClassGroup} with a specific {@code TimeSlot}.
     *
     * @param day        the day of the week
     * @param startTime  the start time (inclusive)
     * @param endTime    the end time (exclusive)
     */
    public ClassGroup(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        this.studentList = new ArrayList<>();
        this.timeSlot = new TimeSlot(day, startTime, endTime);
    }

    /**
     * Returns a formatted string listing all students in this class group.
     *
     * @return A numbered list of students as a string. Returns an empty string if there are no students.
     */
    public String viewStudents() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.studentList.size(); i++) {
            sb.append((i + 1)).append(". ").append(studentList.get(i)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Adds a student to this class group.
     *
     * @param student The {@code Person} object representing the student to add.
     * @return A confirmation message indicating the student has been added.
     */
    public String addStudent(Person student) {
        this.studentList.add(student);
        return String.format("Student %s has been added to the class %s!", student, this.toString());
    }

    /**
     * Adds an assignment name to this class group.
     *
     * @param assignmentName The name of the assignment to add.
     * @return The added assignment name.
     */
    public String addAssignmentName(String assignmentName) {
        this.assignmentList.add(assignmentName);
        return assignmentName;
    }

    /**
     * Deletes an assignment from this class group by its name.
     *
     * @param assignmentName The name of the assignment to delete.
     * @return A message indicating the outcome of the deletion attempt.
     */
    public String deleteAssignment(String assignmentName) {
        if (this.assignmentList.contains(assignmentName)) {
            this.assignmentList.remove(assignmentName);
            return String.format("The assigmnment %s could not be found!", assignmentName);
        } else {
            return "Assignment not found!";
        }
    }

    /**
     * Returns a formatted string listing all assignments in this class group.
     *
     * @return A numbered list of assignments as a string. Returns an empty string if there are no assignments.
     */
    public String viewAssignments() {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.assignmentList.size(); i++) {
            sb.append((i + 1)).append(". ").append(assignmentList.get(i)).append("\n");
        }

        return sb.toString();
    }
}

