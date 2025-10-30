package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.assignment.Assignment;

/**
 * Jackson-friendly version of {@link Assignment}.
 */
class JsonAdaptedAssignment {

    private final String assignmentName;
    private final String classGroupName;
    private final boolean isMarked;

    /**
     * Constructs a {@code JsonAdaptedAssignment} with the given values.
     */
    @JsonCreator
    public JsonAdaptedAssignment(
            @JsonProperty("name") String assignmentName,
            @JsonProperty("classGroup") String classGroupName,
            @JsonProperty("marked") Boolean isMarked) {
        this.assignmentName = assignmentName;
        this.classGroupName = classGroupName;
        this.isMarked = isMarked != null ? isMarked : false; // Default to unmarked if not specified
    }

    /**
     * Constructor for backward compatibility with old data format
     */
    public JsonAdaptedAssignment(String assignmentName) {
        this(assignmentName, "Unknown", false);
    }

    /**
     * Converts a given {@code Assignment} into this class for Jackson use.
     */
    public JsonAdaptedAssignment(Assignment source) {
        requireNonNull(source);
        assignmentName = source.getAssignmentName();
        classGroupName = source.getClassGroupName();
        isMarked = source.isMarked();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JsonAdaptedAssignment)) {
            return false;
        }
        JsonAdaptedAssignment that = (JsonAdaptedAssignment) o;
        return isMarked == that.isMarked
                && java.util.Objects.equals(assignmentName, that.assignmentName)
                && java.util.Objects.equals(classGroupName, that.classGroupName);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(assignmentName, classGroupName, isMarked);
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public String getClassGroupName() {
        return classGroupName;
    }

    public boolean isMarked() {
        return isMarked;
    }

    /**
     * Returns a JSON object containing both the assignment name and marked status.
     */
    @JsonValue
    public JsonAdaptedAssignmentWrapper toJsonObject() {
        return new JsonAdaptedAssignmentWrapper(this);
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code Assignment} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted assignment.
     */
    public Assignment toModelType() throws IllegalValueException {
        if (!Assignment.isValidAssignmentName(assignmentName)) {
            throw new IllegalValueException(Assignment.MESSAGE_CONSTRAINTS);
        }
        if (classGroupName == null || !Assignment.isValidClassGroupName(classGroupName)) {
            throw new IllegalValueException(Assignment.MESSAGE_CLASSGROUP_CONSTRAINTS);
        }
        return new Assignment(assignmentName, classGroupName, isMarked);
    }

}