package seedu.address.model.assignment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an Assignment in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidAssignmentName(String)}
 */
public class Assignment {

    public static final String MESSAGE_CONSTRAINTS = "Assignment names should start with and "
            + "contain alphanumeric characters, and it should not be blank.\n"
            + "Allowed special characters are hyphens (-) and spaces.";
    public static final String MESSAGE_CLASSGROUP_CONSTRAINTS =
            "Class name cannot be empty.";
    // Allow alphanumeric characters, spaces and hyphens; must contain at least one alphanumeric character
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} \\-]*";


    public final String assignmentName;
    public final String classGroupName;
    private final boolean isMarked;

    /**
     * Constructs a {@code Assignment}.
     *
     * @param assignmentName A valid assignment name.
     * @param classGroupName A valid class group name.
     */
    public Assignment(String assignmentName, String classGroupName) {
        this(assignmentName, classGroupName, false);
    }

    /**
     * Constructs a {@code Assignment} with the specified marked status.
     *
     * @param assignmentName A valid assignment name.
     * @param classGroupName A valid class group name.
     * @param isMarked The marked status of the assignment.
     */
    public Assignment(String assignmentName, String classGroupName, boolean isMarked) {
        requireNonNull(assignmentName);
        requireNonNull(classGroupName);
        checkArgument(isValidAssignmentName(assignmentName), MESSAGE_CONSTRAINTS);
        checkArgument(isValidClassGroupName(classGroupName), MESSAGE_CLASSGROUP_CONSTRAINTS);
        this.assignmentName = assignmentName;
        this.classGroupName = classGroupName;
        this.isMarked = isMarked;
    }

    /**
     * Returns true if a given string is a valid assignment name.
     */
    public static boolean isValidAssignmentName(String test) {
        return test != null && test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid class group name.
     */
    public static boolean isValidClassGroupName(String test) {
        return test != null && test.matches(VALIDATION_REGEX);
    }

    /**
     * Creates and returns a new Assignment with the same name but marked as completed.
     *
     * @return A new Assignment instance that is marked as completed
     */
    public Assignment mark() {
        if (this.isMarked) {
            return this;
        }
        Assignment markedAssignment = new Assignment(this.assignmentName, this.classGroupName, true);
        return markedAssignment;
    }

    /**
     * Creates and returns a new Assignment with the same name but marked as not completed.
     *
     * @return A new Assignment instance that is not marked
     */
    public Assignment unmark() {
        if (!this.isMarked) {
            return this;
        }
        Assignment unmarkedAssignment = new Assignment(this.assignmentName, this.classGroupName, false);
        return unmarkedAssignment;
    }

    /**
     * Returns true if the assignment has been marked as completed.
     */
    public boolean isMarked() {
        return isMarked;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Assignment)) {
            return false;
        }

        Assignment otherAssignment = (Assignment) other;
        return assignmentName.equalsIgnoreCase(otherAssignment.assignmentName)
                && classGroupName.equalsIgnoreCase(otherAssignment.classGroupName);
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public String getClassGroupName() {
        return classGroupName;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(assignmentName.toLowerCase(), classGroupName.toLowerCase());
    }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        return assignmentName + " (" + classGroupName + ")";
    }

}
