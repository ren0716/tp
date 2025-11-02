package seedu.address.model.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AssignmentTest {

    private static final String DEFAULT_CLASS_GROUP = "test-class";

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Assignment(null, DEFAULT_CLASS_GROUP));
    }

    @Test
    public void constructor_invalidAssignmentName_throwsIllegalArgumentException() {
        String invalidAssignmentName = "";
        assertThrows(IllegalArgumentException.class, () -> new Assignment(invalidAssignmentName, DEFAULT_CLASS_GROUP));
    }

    @Test
    public void mark_unmarkedAssignment_returnsMarkedAssignment() {
        Assignment assignment = new Assignment("Physics", DEFAULT_CLASS_GROUP);
        assertFalse(assignment.isMarked());
        Assignment markedAssignment = assignment.mark();
        assertTrue(markedAssignment.isMarked());
        assertEquals(assignment.getAssignmentName(), markedAssignment.getAssignmentName());
    }

    @Test
    public void mark_alreadyMarkedAssignment_returnsSameInstance() {
        Assignment assignment = new Assignment("Physics", DEFAULT_CLASS_GROUP, true);
        assertTrue(assignment.isMarked());
        Assignment markedAssignment = assignment.mark();
        assertTrue(markedAssignment.isMarked());
        assertEquals(assignment.getAssignmentName(), markedAssignment.getAssignmentName());
    }

    @Test
    public void unmark_markedAssignment_returnsUnmarkedAssignment() {
        Assignment assignment = new Assignment("Physics", DEFAULT_CLASS_GROUP, true);
        assertTrue(assignment.isMarked());
        Assignment unmarkedAssignment = assignment.unmark();
        assertFalse(unmarkedAssignment.isMarked());
        assertEquals(assignment.getAssignmentName(), unmarkedAssignment.getAssignmentName());
    }

    @Test
    public void unmark_alreadyUnmarkedAssignment_returnsSameInstance() {
        Assignment assignment = new Assignment("Physics", DEFAULT_CLASS_GROUP, false);
        assertFalse(assignment.isMarked());
        Assignment unmarkedAssignment = assignment.unmark();
        assertFalse(unmarkedAssignment.isMarked());
        assertEquals(assignment.getAssignmentName(), unmarkedAssignment.getAssignmentName());
    }

    @Test
    public void equals_sameAssignment_returnsTrue() {
        Assignment assignment = new Assignment("Physics", DEFAULT_CLASS_GROUP);
        assertTrue(assignment.equals(assignment));
    }

    @Test
    public void equals_sameNameDifferentMark_returnsTrue() {
        Assignment assignment1 = new Assignment("Physics", DEFAULT_CLASS_GROUP, true);
        Assignment assignment2 = new Assignment("Physics", DEFAULT_CLASS_GROUP, false);
        assertTrue(assignment1.equals(assignment2));
    }

    @Test
    public void equals_differentName_returnsFalse() {
        Assignment assignment1 = new Assignment("Physics", DEFAULT_CLASS_GROUP);
        Assignment assignment2 = new Assignment("Math", DEFAULT_CLASS_GROUP);
        assertFalse(assignment1.equals(assignment2));
    }

    @Test
    public void equals_null_returnsFalse() {
        Assignment assignment = new Assignment("Physics", DEFAULT_CLASS_GROUP);
        assertFalse(assignment.equals(null));
    }

    @Test
    public void toString_validAssignment_returnsCorrectString() {
        Assignment assignment = new Assignment("Physics", DEFAULT_CLASS_GROUP);
        assertEquals("Physics (test-class)", assignment.toString());
    }
}
