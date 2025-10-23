package seedu.address.model.assignment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.model.classgroup.ClassGroup;

/**
 * Represents an Assignment in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidAssignmentName(String)}
 */
public class Assignment {

    public static final String MESSAGE_CONSTRAINTS =
            "Assignment names should be alphanumeric and may contain spaces or hyphens";
    // Allow alphanumeric characters, spaces and hyphens; must contain at least one alphanumeric character
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} \\-]*";


    public final String assignmentName;
    public final ClassGroup classGroup;
    private final boolean isMarked;

    /**
     * Constructs a {@code Assignment}.
     *
     * @param assignmentName A valid assignment name.
     */
    public Assignment(String assignmentName) {
        this(assignmentName, false);
    }

    /**
     * Constructs a {@code Assignment} with the specified marked status.
     *
     * @param assignmentName A valid assignment name.
     * @param isMarked The marked status of the assignment.
     */
    public Assignment(String assignmentName, boolean isMarked) {
        requireNonNull(assignmentName);
        checkArgument(isValidAssignmentName(assignmentName), MESSAGE_CONSTRAINTS);
        this.assignmentName = assignmentName;
        this.isMarked = isMarked;
    }

    /**
     * Returns true if a given string is a valid assignment name.
     */
    public static boolean isValidAssignmentName(String test) {
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
        Assignment markedAssignment = new Assignment(this.assignmentName, true);
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
        Assignment unmarkedAssignment = new Assignment(this.assignmentName, false);
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
        return assignmentName.equals(otherAssignment.assignmentName);
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    @Override
    public int hashCode() {
        return assignmentName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + assignmentName + ']';
    }

}
