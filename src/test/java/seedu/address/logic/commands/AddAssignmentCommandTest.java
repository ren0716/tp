package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.person.Person;
import seedu.address.testutil.AssignmentBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for AddAssignmentCommand.
 */
public class AddAssignmentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /**
     * Tests successful addition of an assignment to a person in an unfiltered list.
     * Verifies that the command correctly updates the person with the new assignment
     * and produces the expected success message.
     */
    @Test
    public void execute_addAssignment_success() throws Exception {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Assignment validAssignment = new AssignmentBuilder().withName("HW1").build();
        AddAssignmentCommand.AddAssignmentDescriptor descriptor = new AddAssignmentCommand.AddAssignmentDescriptor();
        descriptor.setAssignments(Set.of(validAssignment));
        AddAssignmentCommand command = new AddAssignmentCommand(INDEX_FIRST_PERSON, descriptor);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person editedPerson = new PersonBuilder(personToEdit)
                .withAssignments(validAssignment.getAssignmentName())
                .build();
        expectedModel.setPerson(personToEdit, editedPerson);

        String expectedMessage = String.format(AddAssignmentCommand.MESSAGE_SUCCESS,
                Messages.format(editedPerson));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    /**
     * Tests that attempting to add a duplicate assignment to a person fails.
     * Verifies that the command throws an exception with the appropriate error message
     * when trying to add an assignment that already exists for the person.
     */
    @Test
    public void execute_duplicateAssignment_failure() {
        Assignment duplicate = new AssignmentBuilder().withName("EXISTING").build();
        Person original = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person withAssignment = new PersonBuilder(original).withAssignments(duplicate.getAssignmentName()).build();
        model.setPerson(original, withAssignment);

        AddAssignmentCommand.AddAssignmentDescriptor descriptor = new AddAssignmentCommand.AddAssignmentDescriptor();
        descriptor.setAssignments(Set.of(duplicate));
        AddAssignmentCommand command = new AddAssignmentCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(command, model, String.format(
                AddAssignmentCommand.MESSAGE_DUPLICATE_ASSIGNMENT, duplicate.getAssignmentName()));
    }

    /**
     * Tests that attempting to add an assignment to an invalid person index fails.
     * Verifies that the command throws an exception when the specified index is
     * out of bounds of the filtered person list.
     */
    @Test
    public void execute_invalidPersonIndex_failure() {
        Index outOfBounds = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Assignment assignment = new AssignmentBuilder().withName("HW-OUT").build();
        AddAssignmentCommand.AddAssignmentDescriptor descriptor = new AddAssignmentCommand.AddAssignmentDescriptor();
        descriptor.setAssignments(Set.of(assignment));
        AddAssignmentCommand command = new AddAssignmentCommand(outOfBounds, descriptor);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Tests the equals method of AddAssignmentCommand.
     * Verifies that commands with the same index and assignment descriptor are equal,
     * and that commands with different indices or descriptors are not equal.
     */
    @Test
    public void equals() {
        Assignment a1 = new AssignmentBuilder().withName("A1").build();
        Assignment a2 = new AssignmentBuilder().withName("A2").build();

        AddAssignmentCommand.AddAssignmentDescriptor desc1 = new AddAssignmentCommand.AddAssignmentDescriptor();
        desc1.setAssignments(Set.of(a1));
        AddAssignmentCommand.AddAssignmentDescriptor desc1Copy = new AddAssignmentCommand.AddAssignmentDescriptor();
        desc1Copy.setAssignments(Set.of(a1));
        AddAssignmentCommand.AddAssignmentDescriptor desc2 = new AddAssignmentCommand.AddAssignmentDescriptor();
        desc2.setAssignments(Set.of(a2));

        AddAssignmentCommand addA1First = new AddAssignmentCommand(INDEX_FIRST_PERSON, desc1);
        AddAssignmentCommand addA1FirstCopy = new AddAssignmentCommand(INDEX_FIRST_PERSON, desc1Copy);
        AddAssignmentCommand addA2First = new AddAssignmentCommand(INDEX_FIRST_PERSON, desc2);
        AddAssignmentCommand addA1Second = new AddAssignmentCommand(INDEX_SECOND_PERSON, desc1);

        // same object -> true
        assertTrue(addA1First.equals(addA1First));

        // same values -> true
        assertTrue(addA1First.equals(addA1FirstCopy));

        // different assignment -> false
        assertFalse(addA1First.equals(addA2First));

        // different index -> false
        assertFalse(addA1First.equals(addA1Second));

        // null -> false
        assertFalse(addA1First.equals(null));
    }

    /**
     * Tests the toString method of AddAssignmentCommand.
     * Verifies that the string representation contains the command's index
     * and assignment descriptor in the expected format.
     */
    @Test
    public void toStringMethod() {
        Assignment assignment = new AssignmentBuilder().withName("HW1").build();
        AddAssignmentCommand.AddAssignmentDescriptor descriptor = new AddAssignmentCommand.AddAssignmentDescriptor();
        descriptor.setAssignments(Set.of(assignment));
        AddAssignmentCommand command = new AddAssignmentCommand(INDEX_FIRST_PERSON, descriptor);

        String expected = AddAssignmentCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PERSON
                + ", addAssignmentDescriptor=" + descriptor + "}";
        assertEquals(expected, command.toString());
    }

    /**
     * Tests the toString method of AddAssignmentDescriptor.
     * Verifies that the string representation contains the descriptor's
     * assignments in the expected format.
     */
    @Test
    public void descriptorToStringMethod() {
        Assignment assignment = new AssignmentBuilder().withName("Math-HW").build();
        AddAssignmentCommand.AddAssignmentDescriptor descriptor = new AddAssignmentCommand.AddAssignmentDescriptor();
        descriptor.setAssignments(Set.of(assignment));

        String expected = AddAssignmentCommand.AddAssignmentDescriptor.class.getCanonicalName()
                + "{assignments=" + Set.of(assignment) + "}";
        assertEquals(expected, descriptor.toString());
    }

    /**
     * Tests that isAssignmentAdded returns true when assignments are present.
     * Verifies that the descriptor correctly indicates when at least one
     * assignment has been added.
     */
    @Test
    public void descriptorIsAssignmentAdded_withAssignments_returnsTrue() {
        Assignment assignment = new AssignmentBuilder().withName("Quiz1").build();
        AddAssignmentCommand.AddAssignmentDescriptor descriptor = new AddAssignmentCommand.AddAssignmentDescriptor();
        descriptor.setAssignments(Set.of(assignment));

        assertTrue(descriptor.isAssignmentAdded());
    }

    /**
     * Tests that isAssignmentAdded returns false when no assignments are present.
     * Verifies that the descriptor correctly indicates when no assignments
     * have been added.
     */
    @Test
    public void descriptorIsAssignmentAdded_noAssignments_returnsFalse() {
        AddAssignmentCommand.AddAssignmentDescriptor descriptor = new AddAssignmentCommand.AddAssignmentDescriptor();
        assertFalse(descriptor.isAssignmentAdded());
    }

    /**
     * Tests that executing a command with no assignments provided fails.
     * Verifies that the command throws an exception with the appropriate error message
     * when an empty assignment descriptor is used.
     */
    @Test
    public void execute_noAssignmentsProvided_failure() {
        AddAssignmentCommand.AddAssignmentDescriptor emptyDescriptor = new AddAssignmentCommand
                .AddAssignmentDescriptor();
        AddAssignmentCommand command = new AddAssignmentCommand(INDEX_FIRST_PERSON, emptyDescriptor);

        assertCommandFailure(command, model, AddAssignmentCommand.MESSAGE_ASSIGNMENT_NOT_ADDED);
    }
}
