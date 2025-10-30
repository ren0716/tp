package seedu.address.testutil;

import seedu.address.model.assignment.Assignment;

/**
 * A utility class to help with building Assignment objects for tests.
 */
public class AssignmentBuilder {

    public static final String DEFAULT_NAME = "assignment1";
    public static final String DEFAULT_CLASS_GROUP = "default-class";

    private String name;
    private String classGroup;
    private boolean isMarked;

    /**
     * Creates a new {@code AssignmentBuilder} with default values.
     */
    public AssignmentBuilder() {
        name = DEFAULT_NAME;
        classGroup = DEFAULT_CLASS_GROUP;
        isMarked = false;
    }

    /**
     * Creates an {@code AssignmentBuilder} with fields copied from the given {@code assignment}.
     */
    public AssignmentBuilder(Assignment assignment) {
        name = assignment.getAssignmentName();
        classGroup = assignment.classGroupName;
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
     * Sets the class group of the {@code Assignment} that we are building.
     */
    public AssignmentBuilder withClassGroup(String classGroup) {
        this.classGroup = classGroup;
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
        return new Assignment(name, classGroup, isMarked);
    }
}
