package seedu.address.testutil;

import seedu.address.model.assignment.Assignment;

/**
 * A utility class to help with building Assignment objects for tests.
 */
public class AssignmentBuilder {

    public static final String DEFAULT_NAME = "Assignment1";

    private String name;

    public AssignmentBuilder() {
        name = DEFAULT_NAME;
    }

    /**
     * Creates an {@code AssignmentBuilder} with fields copied from the given {@code assignment}.
     */
    public AssignmentBuilder(Assignment assignment) {
        name = assignment.getAssignmentName();
    }

    /**
     * Sets the name of the {@code Assignment} that we are building.
     */
    public AssignmentBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Builds and returns an {@code Assignment} instance.
     *
     * Note: If your Assignment constructor requires more fields, add corresponding builder methods
     * and pass them into the constructor here.
     */
    public Assignment build() {
        return new Assignment(name);
    }
}
