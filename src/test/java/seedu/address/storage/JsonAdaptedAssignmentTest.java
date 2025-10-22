package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.assignment.Assignment;

public class JsonAdaptedAssignmentTest {

    private static final String VALID_ASSIGNMENT_NAME = "Physics123";
    private static final String INVALID_ASSIGNMENT_NAME = "@Math";

    @Test
    public void toModelType_validAssignmentDetails_returnsAssignment() throws Exception {
        JsonAdaptedAssignment assignment = new JsonAdaptedAssignment(VALID_ASSIGNMENT_NAME);
        assertEquals(new Assignment(VALID_ASSIGNMENT_NAME), assignment.toModelType());
    }

    @Test
    public void toModelType_validMarkedAssignment_returnsMarkedAssignment() throws Exception {
        JsonAdaptedAssignment assignment = new JsonAdaptedAssignment(VALID_ASSIGNMENT_NAME, true);
        Assignment modelAssignment = assignment.toModelType();
        assertEquals(VALID_ASSIGNMENT_NAME, modelAssignment.getAssignmentName());
        assertTrue(modelAssignment.isMarked());
    }

    @Test
    public void toModelType_invalidAssignmentName_throwsIllegalValueException() {
        JsonAdaptedAssignment assignment = new JsonAdaptedAssignment(INVALID_ASSIGNMENT_NAME);
        assertThrows(IllegalValueException.class, () -> assignment.toModelType());
    }

    @Test
    public void toModelType_nullAssignmentName_throwsIllegalValueException() {
        JsonAdaptedAssignment assignment = new JsonAdaptedAssignment((String) null);
        assertThrows(IllegalValueException.class, () -> assignment.toModelType());
    }

    @Test
    public void toModelType_nullMarkedStatus_treatedAsFalse() throws Exception {
        JsonAdaptedAssignment assignment = new JsonAdaptedAssignment(VALID_ASSIGNMENT_NAME, null);
        Assignment modelAssignment = assignment.toModelType();
        assertFalse(modelAssignment.isMarked());
    }

    @Test
    public void equals() {
        JsonAdaptedAssignment assignment1 = new JsonAdaptedAssignment(VALID_ASSIGNMENT_NAME, true);

        // same object -> returns true
        assertEquals(assignment1, assignment1);

        // same values -> returns true
        JsonAdaptedAssignment assignment2 = new JsonAdaptedAssignment(VALID_ASSIGNMENT_NAME, true);
        assertEquals(assignment1, assignment2);

        // different values -> returns false
        JsonAdaptedAssignment assignment3 = new JsonAdaptedAssignment(VALID_ASSIGNMENT_NAME, false);
        assertFalse(assignment1.equals(assignment3));
    }
}
