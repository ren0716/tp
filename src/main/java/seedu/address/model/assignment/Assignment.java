package seedu.address.model.assignment;

/**
 * Represents an assignment in TutorTrack.
 * Each {@code Assignment} has a name and a completion status.
 * Assignments are typically linked to a student or class and can be marked as done or undone.
 */
public class Assignment {
    /** The name or title of the assignment. */
    private String name;

    /** Whether the assignment has been completed. */
    private boolean isDone;

    /**
     * Constructs an {@code Assignment} with the specified name.
     * The assignment is initially marked as not done.
     *
     * @param name The name or title of the assignment.
     */
    public Assignment(String name) {
        this.name = name;
        this.isDone = false;
    }

    /**
     * Marks this assignment as done.
     * Once marked done, {@code isDone} becomes {@code true}.
     */
    public void markDone() {
        this.isDone = true;
    }

    /**
     * Marks this assignment as not done.
     * Once marked undone, {@code isDone} becomes {@code false}.
     */
    public void markUndone() {
        this.isDone = false;
    }
}
