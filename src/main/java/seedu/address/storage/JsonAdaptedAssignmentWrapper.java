package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A wrapper class to properly serialize assignment data to JSON.
 */
class JsonAdaptedAssignmentWrapper {
    @JsonProperty("name")
    private String assignmentName;
    @JsonProperty("classGroup")
    private String classGroupName;
    @JsonProperty("marked")
    private boolean isMarked;

    public JsonAdaptedAssignmentWrapper() {
        // Jackson needs this
    }

    public JsonAdaptedAssignmentWrapper(JsonAdaptedAssignment assignment) {
        this.assignmentName = assignment.getAssignmentName();
        this.classGroupName = assignment.getClassGroupName();
        this.isMarked = assignment.isMarked();
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
}
