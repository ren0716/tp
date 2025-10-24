package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.person.Person;
import seedu.address.testutil.AssignmentBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;
import seedu.address.commons.util.StringUtil;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code UnmarkAssignmentCommand}.
 * Tests include scenarios for successful unmarking, handling of invalid index, absence of the specified assignment,
 * and equality checks.
 */
public class UnmarkAssignmentCommandTest {

    /**
     * Tests that executing a valid UnmarkAssignmentCommand unmarks the assignment and updates the person's assignments.
     */
    @Test
    public void execute_validAssignment_success() {
        // Prepare a typical model and a person with the assignment to be unmarked
        String classGroup = "default-class";
        Assignment assignment = new AssignmentBuilder()
                .withName("Physics-1800")
                .withClassGroup(classGroup)
                .build();
        Person originalPerson = TypicalPersons.getTypicalAddressBook().getPersonList().get(0);
        Person personWithAssignment = new PersonBuilder(originalPerson)
                .withClassGroups(classGroup)
                .withAssignments(classGroup, assignment.getAssignmentName())
                .build();

        Model model = new ModelManager(new AddressBook(TypicalPersons.getTypicalAddressBook()), new UserPrefs());
        // Replace the original person with the modified one containing the assignment
        model.setPerson(originalPerson, personWithAssignment);

        Index targetIndex = Index.fromOneBased(1);

        MarkAssignmentCommand markCommand = new MarkAssignmentCommand(targetIndex, assignment);
        try {
            markCommand.execute(model);
        } catch (CommandException ce) {
            throw new AssertionError("Setup for unmarking failed.", ce);
        }

        UnmarkAssignmentCommand command = new UnmarkAssignmentCommand(targetIndex, assignment);
        try {
            var result = command.execute(model);
            String expectedMessage = String.format(seedu.address.logic.Messages.MESSAGE_UNMARK_PERSON_SUCCESS,
                    StringUtil.toTitleCase(assignment.getAssignmentName()), 
                    StringUtil.toTitleCase(personWithAssignment.getName().fullName));
            assertEquals(expectedMessage, result.getFeedbackToUser());
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Tests that execution fails when the target index is invalid.
     */
    @Test
    public void execute_invalidPersonIndex_failure() {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Assignment assignment = new AssignmentBuilder().withName("Physics-1800").build();
        UnmarkAssignmentCommand command = new UnmarkAssignmentCommand(outOfBoundsIndex, assignment);

        assertCommandFailure(command, model, seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Tests that execution fails when the specified assignment is not present in the person's assignment set.
     */
    @Test
    public void execute_assignmentNotPresent_failure() {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        // Retrieve first person and ensure the assignment "Physics-1800" is not present
        Person person = model.getFilteredPersonList().get(0);
        String[] remainingAssignments = person.getAssignments().stream()
                .map(a -> a.getAssignmentName())
                .filter(name -> !name.equals("Physics-1800"))
                .toArray(String[]::new);
        Person updatedPerson = new PersonBuilder(person).withAssignmentsUsingDefaultClass(remainingAssignments).build();
        model.setPerson(person, updatedPerson);

        Assignment assignment = new AssignmentBuilder()
                .withName("Physics-1800")
                .withClassGroup("default-class")
                .build();
        UnmarkAssignmentCommand command = new UnmarkAssignmentCommand(Index.fromOneBased(1), assignment);

        assertCommandFailure(command, model, seedu.address.logic.Messages.MESSAGE_INVALID_ASSIGNMENT_IN_PERSON);
    }

    /**
     * Tests the {@code equals} method of {@code UnmarkAssignmentCommand}.
     */
    @Test
    public void equals() {
        Assignment assignment = new AssignmentBuilder().withName("Physics-1800").build();
        UnmarkAssignmentCommand command1 = new UnmarkAssignmentCommand(Index.fromOneBased(1), assignment);
        UnmarkAssignmentCommand command2 = new UnmarkAssignmentCommand(Index.fromOneBased(1), assignment);
        UnmarkAssignmentCommand command3 = new UnmarkAssignmentCommand(Index.fromOneBased(2), assignment);

        // same object -> returns true
        assertEquals(command1, command1);

        // same values -> returns true
        assertEquals(command1, command2);

        // different index -> returns false
        if (command1.equals(command3)) {
            throw new AssertionError("Commands with different indices should not be equal");
        }
    }

    /**
     * Helper method to assert that a command execution fails with the expected message.
     *
     * @param command the command to execute
     * @param model the model on which the command is executed
     * @param expectedMessage the expected error message
     */
    private void assertCommandFailure(UnmarkAssignmentCommand command, Model model, String expectedMessage) {
        try {
            command.execute(model);
            throw new AssertionError("Expected a CommandException to be thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
        }
    }
}
