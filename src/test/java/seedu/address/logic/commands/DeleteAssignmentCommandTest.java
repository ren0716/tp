package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_ASSIGNMENT_NOT_DELETED;
import static seedu.address.logic.Messages.MESSAGE_ASSIGNMENT_NOT_EXIST;
import static seedu.address.logic.Messages.MESSAGE_DELETE_ASSIGNMENT_SUCCESS;
import static seedu.address.logic.Messages.MESSAGE_STUDENT_NOT_IN_CLASS_GROUP;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteAssignmentCommand.DeleteAssignmentDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.person.Person;
import seedu.address.testutil.AssignmentBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for DeleteAssignmentCommand.
 */
public class DeleteAssignmentCommandTest {

    /**
     * Tests successful deletion of an assignment from a person.
     */
    @Test
    public void execute_deleteAssignment_success() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String classGroup = "del-test-class";
        Assignment existing = new AssignmentBuilder().withName("HW-DEL").withClassGroup(classGroup).build();
        Person personWithAssignment = new PersonBuilder(personToEdit)
                .withClassGroups(classGroup)
                .withAssignments(classGroup, existing.getAssignmentName())
                .build();
        model.setPerson(personToEdit, personWithAssignment);

        DeleteAssignmentDescriptor descriptor = new DeleteAssignmentDescriptor();
        descriptor.setClassGroupName(classGroup);
        descriptor.setAssignments(Set.of(existing));
        DeleteAssignmentCommand command = new DeleteAssignmentCommand(INDEX_FIRST_PERSON, descriptor);

        // build expected edited person (assignment removed)
        java.util.Set<Assignment> updatedAssignments = new java.util.HashSet<>(personWithAssignment.getAssignments());
        updatedAssignments.remove(existing);
        Person expectedPerson = new Person(
                personWithAssignment.getName(),
                personWithAssignment.getPhone(),
                personWithAssignment.getLevel(),
                personWithAssignment.getClassGroups(),
                updatedAssignments
        );

        String expectedMessage = String.format(MESSAGE_DELETE_ASSIGNMENT_SUCCESS, Messages.format(expectedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithAssignment, expectedPerson);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    /**
     * Tests deleting a non-existent assignment fails with the appropriate message.
     */
    @Test
    public void execute_deleteNonExistentAssignment_failure() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person personToEdit = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        String classGroup = "some-class";
        // ensure the person has the class but does NOT have the target assignment
        Person personWithClass = new PersonBuilder(personToEdit)
                .withClassGroups(classGroup)
                .build();
        model.setPerson(personToEdit, personWithClass);

        Assignment missing = new AssignmentBuilder().withName("MISSING").withClassGroup(classGroup).build();

        DeleteAssignmentDescriptor descriptor = new DeleteAssignmentDescriptor();
        descriptor.setClassGroupName(classGroup);
        descriptor.setAssignments(Set.of(missing));
        DeleteAssignmentCommand command = new DeleteAssignmentCommand(INDEX_SECOND_PERSON, descriptor);

        String expectedMessage = String.format(MESSAGE_ASSIGNMENT_NOT_EXIST, missing.toString());
        assertCommandFailure(command, model, expectedMessage);
    }

    /**
     * Tests that invalid person index results in failure.
     */
    @Test
    public void execute_invalidPersonIndex_failure() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index outOfBounds = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        Assignment assignment = new AssignmentBuilder().withName("HW-OUT").build();
        DeleteAssignmentDescriptor descriptor = new DeleteAssignmentDescriptor();
        descriptor.setAssignments(Set.of(assignment));
        DeleteAssignmentCommand command = new DeleteAssignmentCommand(outOfBounds, descriptor);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Tests that attempting to delete with a class the student doesn't belong to fails.
     */
    @Test
    public void execute_studentNotInClassGroup_failure() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Assignment assignmentWithInvalidClass = new AssignmentBuilder()
                .withName("HW1")
                .withClassGroup("nonexistent-class")
                .build();

        DeleteAssignmentDescriptor descriptor = new DeleteAssignmentDescriptor();
        descriptor.setClassGroupName(assignmentWithInvalidClass.getClassGroupName());
        descriptor.setAssignments(Set.of(assignmentWithInvalidClass));
        DeleteAssignmentCommand command = new DeleteAssignmentCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(MESSAGE_STUDENT_NOT_IN_CLASS_GROUP,
                assignmentWithInvalidClass.getClassGroupName());
        assertCommandFailure(command, model, expectedMessage);
    }

    /**
     * Tests that executing a command with empty assignment set fails.
     */
    @Test
    public void execute_emptyAssignmentSet_failure() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        DeleteAssignmentDescriptor descriptor = new DeleteAssignmentDescriptor();
        descriptor.setClassGroupName("some-class");
        descriptor.setAssignments(Set.of());
        DeleteAssignmentCommand command = new DeleteAssignmentCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(command, model, MESSAGE_ASSIGNMENT_NOT_DELETED);
    }

    /**
     * Tests DeleteAssignmentDescriptor behaviours (copy constructor, equals, unmodifiable set).
     */
    @Test
    public void descriptorCopyAndAccessors_andEquals_behaviour() {
        Assignment a1 = new AssignmentBuilder().withName("A1").build();
        Assignment a2 = new AssignmentBuilder().withName("A2").build();

        DeleteAssignmentDescriptor desc1 = new DeleteAssignmentDescriptor();
        desc1.setAssignments(Set.of(a1));
        desc1.setClassGroupName("cg");

        DeleteAssignmentDescriptor desc1Copy = new DeleteAssignmentDescriptor(desc1);
        DeleteAssignmentDescriptor desc2 = new DeleteAssignmentDescriptor();
        desc2.setAssignments(Set.of(a2));

        // copy equality
        assertEquals(desc1, desc1Copy);

        // isAssignmentDeleted true/false
        assertTrue(desc1.isAssignmentDeleted());
        DeleteAssignmentDescriptor emptyDesc = new DeleteAssignmentDescriptor();
        assertFalse(emptyDesc.isAssignmentDeleted());

        // getAssignments unmodifiable
        Set<Assignment> assignments = desc1.getAssignments().get();
        try {
            assignments.add(new AssignmentBuilder().withName("X").build());
            assertTrue(false, "Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }

        // equals different scenarios
        assertFalse(desc1.equals(desc2));
        assertTrue(desc1.equals(desc1));
        assertFalse(desc1.equals(null));
        assertFalse(desc1.equals("string"));

        // both null assignments equal
        DeleteAssignmentDescriptor d1 = new DeleteAssignmentDescriptor();
        DeleteAssignmentDescriptor d2 = new DeleteAssignmentDescriptor();
        assertTrue(d1.equals(d2));
    }

    /**
     * Tests DeleteAssignmentCommand equals and toString.
     */
    @Test
    public void commandEqualsAndToString() {
        Assignment a1 = new AssignmentBuilder().withName("A1").build();
        Assignment a2 = new AssignmentBuilder().withName("A2").build();

        DeleteAssignmentDescriptor desc1 = new DeleteAssignmentDescriptor();
        desc1.setAssignments(Set.of(a1));
        DeleteAssignmentDescriptor desc1Copy = new DeleteAssignmentDescriptor();
        desc1Copy.setAssignments(Set.of(a1));
        DeleteAssignmentDescriptor desc2 = new DeleteAssignmentDescriptor();
        desc2.setAssignments(Set.of(a2));

        DeleteAssignmentCommand cmd1 = new DeleteAssignmentCommand(INDEX_FIRST_PERSON, desc1);
        DeleteAssignmentCommand cmd1Copy = new DeleteAssignmentCommand(INDEX_FIRST_PERSON, desc1Copy);
        DeleteAssignmentCommand cmd2 = new DeleteAssignmentCommand(INDEX_FIRST_PERSON, desc2);
        DeleteAssignmentCommand cmdDifferentIndex = new DeleteAssignmentCommand(INDEX_SECOND_PERSON, desc1);

        // same object
        assertTrue(cmd1.equals(cmd1));
        // same values
        assertTrue(cmd1.equals(cmd1Copy));
        // different assignments -> false
        assertFalse(cmd1.equals(cmd2));
        // different index -> false
        assertFalse(cmd1.equals(cmdDifferentIndex));
        // null -> false
        assertFalse(cmd1.equals(null));
        // different type -> false
        assertFalse(cmd1.equals("string"));

        // toString includes class name and descriptor representation
        String expected = DeleteAssignmentCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PERSON
                + ", deleteAssignmentDescriptor=" + desc1 + "}";
        assertEquals(expected, cmd1.toString());
    }
}
