package seedu.address.testutil;

import seedu.address.model.assignment.Assignment;

/**
 * A utility class to help with building Assignment objects for tests.
 */
public class AssignmentBuilder {

    public static final String DEFAULT_NAME = "Assignment1";

    private String name;
    private boolean isMarked;

    /**
     * Creates a new {@code AssignmentBuilder} with default values.
     */
    public AssignmentBuilder() {
        name = DEFAULT_NAME;
        isMarked = false;
    }

    /**
     * Creates an {@code AssignmentBuilder} with fields copied from the given {@code assignment}.
     */
    public AssignmentBuilder(Assignment assignment) {
        name = assignment.getAssignmentName();
        isMarked = assignment.isMarked();
    }

    /**
     * Sets the name of the {@code Assignment} that we are building.
     */
    public AssignmentBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the marked status of the {@code Assignment} that we are building.
     */
    public AssignmentBuilder withMarkedStatus(boolean isMarked) {
        this.isMarked = isMarked;
        return this;
    }

    /**
     * Builds and returns an {@code Assignment} instance.
     */
    public Assignment build() {
        return new Assignment(name, isMarked);
    }
}
