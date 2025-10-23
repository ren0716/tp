package seedu.address.model.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AssignmentTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Assignment(null));
    }

    @Test
    public void constructor_invalidAssignmentName_throwsIllegalArgumentException() {
        String invalidAssignmentName = "";
        assertThrows(IllegalArgumentException.class, () -> new Assignment(invalidAssignmentName));
    }

    @Test
    public void mark_unmarkedAssignment_returnsMarkedAssignment() {
        Assignment assignment = new Assignment("Physics");
        assertFalse(assignment.isMarked());
        Assignment markedAssignment = assignment.mark();
        assertTrue(markedAssignment.isMarked());
        assertEquals(assignment.getAssignmentName(), markedAssignment.getAssignmentName());
    }

    @Test
    public void mark_alreadyMarkedAssignment_returnsSameInstance() {
        Assignment assignment = new Assignment("Physics", true);
        assertTrue(assignment.isMarked());
        Assignment markedAssignment = assignment.mark();
        assertTrue(markedAssignment.isMarked());
        assertEquals(assignment.getAssignmentName(), markedAssignment.getAssignmentName());
    }

    @Test
    public void unmark_markedAssignment_returnsUnmarkedAssignment() {
        Assignment assignment = new Assignment("Physics", true);
        assertTrue(assignment.isMarked());
        Assignment unmarkedAssignment = assignment.unmark();
        assertFalse(unmarkedAssignment.isMarked());
        assertEquals(assignment.getAssignmentName(), unmarkedAssignment.getAssignmentName());
    }

    @Test
    public void unmark_alreadyUnmarkedAssignment_returnsSameInstance() {
        Assignment assignment = new Assignment("Physics", false);
        assertFalse(assignment.isMarked());
        Assignment unmarkedAssignment = assignment.unmark();
        assertFalse(unmarkedAssignment.isMarked());
        assertEquals(assignment.getAssignmentName(), unmarkedAssignment.getAssignmentName());
    }

    @Test
    public void equals_sameAssignment_returnsTrue() {
        Assignment assignment = new Assignment("Physics");
        assertTrue(assignment.equals(assignment));
    }

    @Test
    public void equals_sameNameDifferentMark_returnsTrue() {
        Assignment assignment1 = new Assignment("Physics", true);
        Assignment assignment2 = new Assignment("Physics", false);
        assertTrue(assignment1.equals(assignment2));
    }

    @Test
    public void equals_differentName_returnsFalse() {
        Assignment assignment1 = new Assignment("Physics");
        Assignment assignment2 = new Assignment("Math");
        assertFalse(assignment1.equals(assignment2));
    }

    @Test
    public void equals_null_returnsFalse() {
        Assignment assignment = new Assignment("Physics");
        assertFalse(assignment.equals(null));
    }

    @Test
    public void toString_validAssignment_returnsCorrectString() {
        Assignment assignment = new Assignment("Physics");
        assertEquals("[Physics]", assignment.toString());
    }
}
