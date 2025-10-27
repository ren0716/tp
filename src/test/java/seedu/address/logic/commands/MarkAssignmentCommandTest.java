package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_ASSIGNMENT_IN_PERSON;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_MARK_PERSON_SUCCESS;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
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

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code MarkAssignmentCommand}.
 * Tests include scenarios for successful marking, handling of invalid index, absence of the specified assignment,
 * and equality checks.
 */
public class MarkAssignmentCommandTest {

    /**
     * Tests that executing a valid MarkAssignmentCommand marks the assignment and updates the person's assignments.
     */
    @Test
    public void execute_validAssignment_success() {
        // Prepare a typical model and a person with the assignment to be marked
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

        List<Index> targetIndices = Arrays.asList(Index.fromOneBased(1));
        MarkAssignmentCommand command = new MarkAssignmentCommand(targetIndices, assignment);

        try {
            var result = command.execute(model);
            String expectedMessage = String.format(
                    MESSAGE_MARK_PERSON_SUCCESS,
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
        MarkAssignmentCommand command = new MarkAssignmentCommand(Arrays.asList(outOfBoundsIndex), assignment);

        assertCommandFailure(command, model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
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
        MarkAssignmentCommand command = new MarkAssignmentCommand(Arrays.asList(Index.fromOneBased(1)), assignment);

        assertCommandFailure(command, model, String.format(
                MESSAGE_INVALID_ASSIGNMENT_IN_PERSON, assignment.getAssignmentName())
        );
    }

    /**
     * Tests the {@code equals} method of {@code MarkAssignmentCommand}.
     */
    @Test
    public void execute_multipleValidAssignments_success() {
        // Prepare multiple persons with assignments
        String classGroup = "default-class";
        Assignment assignment = new AssignmentBuilder()
                .withName("Physics-1800")
                .withClassGroup(classGroup)
                .build();

        Model model = new ModelManager(new AddressBook(TypicalPersons.getTypicalAddressBook()), new UserPrefs());
        List<Person> originalPersons = model.getFilteredPersonList().subList(0, 3);

        for (Person originalPerson : originalPersons) {
            Person personWithAssignment = new PersonBuilder(originalPerson)
                    .withClassGroups(classGroup)
                    .withAssignments(classGroup, assignment.getAssignmentName())
                    .build();
            model.setPerson(originalPerson, personWithAssignment);
        }

        List<Index> targetIndices = Arrays.asList(
                Index.fromOneBased(1),
                Index.fromOneBased(2),
                Index.fromOneBased(3)
        );
        MarkAssignmentCommand command = new MarkAssignmentCommand(targetIndices, assignment);

        try {
            CommandResult result = command.execute(model);
            String expectedMessage = String.format(
                    MESSAGE_MARK_PERSON_SUCCESS,
                    StringUtil.toTitleCase(assignment.getAssignmentName()),
                    originalPersons.stream()
                            .map(p -> StringUtil.toTitleCase(p.getName().fullName))
                            .collect(Collectors.joining(", ")));
            assertEquals(expectedMessage, result.getFeedbackToUser());
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    @Test
    public void execute_partialInvalidIndices_failure() {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        String classGroup = "default-class";

        // Create and add assignments for first three persons
        for (int i = 0; i < 3; i++) {
            Assignment assignment = new AssignmentBuilder()
                    .withName("Physics-1800")
                    .withClassGroup(classGroup)
                    .build();
            Person person = model.getFilteredPersonList().get(i);
            Person personWithAssignment = new PersonBuilder(person)
                    .withClassGroups(classGroup)
                    .withAssignments(classGroup, assignment.getAssignmentName())
                    .build();
            model.setPerson(person, personWithAssignment);
        }

        // Now try to mark with an index that's out of bounds
        Assignment commandAssignment = new AssignmentBuilder()
                .withName("Physics-1800")
                .withClassGroup(classGroup)
                .build();
        List<Index> indices = Arrays.asList(
                Index.fromOneBased(model.getFilteredPersonList().size() + 1) // Invalid index
        );
        MarkAssignmentCommand command = new MarkAssignmentCommand(indices, commandAssignment);

        assertCommandFailure(command, model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Assignment assignment = new AssignmentBuilder().withName("Physics-1800").build();
        List<Index> indices1 = Arrays.asList(Index.fromOneBased(1));
        List<Index> indices2 = Arrays.asList(Index.fromOneBased(1));
        List<Index> indices3 = Arrays.asList(Index.fromOneBased(2));
        MarkAssignmentCommand command1 = new MarkAssignmentCommand(indices1, assignment);
        MarkAssignmentCommand command2 = new MarkAssignmentCommand(indices2, assignment);
        MarkAssignmentCommand command3 = new MarkAssignmentCommand(indices3, assignment);

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
    private void assertCommandFailure(MarkAssignmentCommand command, Model model, String expectedMessage) {
        try {
            command.execute(model);
            throw new AssertionError("Expected a CommandException to be thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
        }
    }
}
