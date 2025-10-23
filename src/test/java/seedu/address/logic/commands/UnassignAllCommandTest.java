package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ASSIGNMENT_MATH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ASSIGNMENT_PHYSICS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASSGROUP_MATH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASSGROUP_PHYSICS;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UnassignAllCommand.
 */
public class UnassignAllCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /**
     * Tests that unassigning an assignment from all students in a class group succeeds.
     * Verifies that the assignment is removed from all students enrolled in the specified class.
     */
    @Test
    public void execute_unassignFromClassGroup_success() {
        // Add students with Math class and existing assignment
        Model modelWithMathClass = new ModelManager(new AddressBook(), new UserPrefs());
        Person alice = new PersonBuilder().withName("Alice").withPhone("91234567")
                .withLevel("1").withClassGroups(VALID_CLASSGROUP_MATH)
                .withAssignments(VALID_ASSIGNMENT_MATH).build();
        Person bob = new PersonBuilder().withName("Bob").withPhone("92345678")
                .withLevel("2").withClassGroups(VALID_CLASSGROUP_MATH)
                .withAssignments(VALID_ASSIGNMENT_MATH).build();
        modelWithMathClass.addPerson(alice);
        modelWithMathClass.addPerson(bob);

        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH);
        UnassignAllCommand command = new UnassignAllCommand(VALID_CLASSGROUP_MATH, assignment);

        String expectedMessage = String.format(UnassignAllCommand.MESSAGE_SUCCESS,
                VALID_ASSIGNMENT_MATH, 2, VALID_CLASSGROUP_MATH);

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person aliceWithoutAssignment = new PersonBuilder(alice).withAssignments().build();
        Person bobWithoutAssignment = new PersonBuilder(bob).withAssignments().build();
        expectedModel.addPerson(aliceWithoutAssignment);
        expectedModel.addPerson(bobWithoutAssignment);

        assertCommandSuccess(command, modelWithMathClass, expectedMessage, expectedModel);
    }

    /**
     * Tests that unassigning an assignment from a single student in a class succeeds.
     * Verifies that the command works correctly when only one student is enrolled in the class.
     */
    @Test
    public void execute_unassignFromOneStudentInClass_success() {
        // Add one student with Physics class and assignment
        Model modelWithPhysicsClass = new ModelManager(new AddressBook(), new UserPrefs());
        Person charlie = new PersonBuilder().withName("Charlie").withPhone("93456789")
                .withLevel("3").withClassGroups(VALID_CLASSGROUP_PHYSICS)
                .withAssignments(VALID_ASSIGNMENT_PHYSICS).build();
        modelWithPhysicsClass.addPerson(charlie);

        Assignment assignment = new Assignment(VALID_ASSIGNMENT_PHYSICS);
        UnassignAllCommand command = new UnassignAllCommand(VALID_CLASSGROUP_PHYSICS, assignment);

        String expectedMessage = String.format(UnassignAllCommand.MESSAGE_SUCCESS,
                VALID_ASSIGNMENT_PHYSICS, 1, VALID_CLASSGROUP_PHYSICS);

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person charlieWithoutAssignment = new PersonBuilder(charlie).withAssignments().build();
        expectedModel.addPerson(charlieWithoutAssignment);

        assertCommandSuccess(command, modelWithPhysicsClass, expectedMessage, expectedModel);
    }

    /**
     * Tests that unassigning only affects students in the specified class group.
     * Verifies that students not enrolled in the target class are not affected.
     */
    @Test
    public void execute_unassignFromMultipleClassStudents_success() {
        // Add students, some with Math class, some without
        Model modelWithMixedClasses = new ModelManager(new AddressBook(), new UserPrefs());
        Person alice = new PersonBuilder().withName("Alice").withPhone("91234567")
                .withLevel("1").withClassGroups(VALID_CLASSGROUP_MATH)
                .withAssignments(VALID_ASSIGNMENT_MATH).build();
        Person bob = new PersonBuilder().withName("Bob").withPhone("92345678")
                .withLevel("2").withClassGroups(VALID_CLASSGROUP_PHYSICS)
                .withAssignments(VALID_ASSIGNMENT_MATH).build();
        Person charlie = new PersonBuilder().withName("Charlie").withPhone("93456789")
                .withLevel("3").withClassGroups(VALID_CLASSGROUP_MATH)
                .withAssignments(VALID_ASSIGNMENT_MATH).build();
        modelWithMixedClasses.addPerson(alice);
        modelWithMixedClasses.addPerson(bob);
        modelWithMixedClasses.addPerson(charlie);

        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH);
        UnassignAllCommand command = new UnassignAllCommand(VALID_CLASSGROUP_MATH, assignment);

        // Only Alice and Charlie should have assignment removed (they have Math class)
        String expectedMessage = String.format(UnassignAllCommand.MESSAGE_SUCCESS,
                VALID_ASSIGNMENT_MATH, 2, VALID_CLASSGROUP_MATH);

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person aliceWithoutAssignment = new PersonBuilder(alice).withAssignments().build();
        Person charlieWithoutAssignment = new PersonBuilder(charlie).withAssignments().build();
        expectedModel.addPerson(aliceWithoutAssignment);
        expectedModel.addPerson(bob); // Bob doesn't have Math class, so assignment remains
        expectedModel.addPerson(charlieWithoutAssignment);

        assertCommandSuccess(command, modelWithMixedClasses, expectedMessage, expectedModel);
    }

    /**
     * Tests that students without the specified assignment are skipped during unassignment.
     * Verifies that only students who actually have the assignment get it removed.
     */
    @Test
    public void execute_studentDoesNotHaveAssignment_skipsStudent() {
        // Add students with Math class, one has the assignment, one doesn't
        Model modelWithPartialAssignment = new ModelManager(new AddressBook(), new UserPrefs());
        Person alice = new PersonBuilder().withName("Alice").withPhone("91234567")
                .withLevel("1").withClassGroups(VALID_CLASSGROUP_MATH).build();
        Person bob = new PersonBuilder().withName("Bob").withPhone("92345678")
                .withLevel("2").withClassGroups(VALID_CLASSGROUP_MATH)
                .withAssignments(VALID_ASSIGNMENT_MATH).build();
        modelWithPartialAssignment.addPerson(alice);
        modelWithPartialAssignment.addPerson(bob);

        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH);
        UnassignAllCommand command = new UnassignAllCommand(VALID_CLASSGROUP_MATH, assignment);

        // Only Bob should have the assignment removed (Alice doesn't have it)
        String expectedMessage = String.format(UnassignAllCommand.MESSAGE_SUCCESS,
                VALID_ASSIGNMENT_MATH, 1, VALID_CLASSGROUP_MATH);

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person bobWithoutAssignment = new PersonBuilder(bob).withAssignments().build();
        expectedModel.addPerson(alice); // Alice doesn't have assignment
        expectedModel.addPerson(bobWithoutAssignment);

        assertCommandSuccess(command, modelWithPartialAssignment, expectedMessage, expectedModel);
    }

    /**
     * Tests that unassigning from a non-existent class group throws a CommandException.
     * Verifies that the command fails when no students are enrolled in the specified class.
     */
    @Test
    public void execute_noStudentsInClass_throwsCommandException() {
        // Students without the specified class
        Model modelWithoutMathClass = new ModelManager(new AddressBook(), new UserPrefs());
        Person alice = new PersonBuilder().withName("Alice").withPhone("91234567")
                .withLevel("1").withClassGroups(VALID_CLASSGROUP_PHYSICS)
                .withAssignments(VALID_ASSIGNMENT_MATH).build();
        modelWithoutMathClass.addPerson(alice);

        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH);
        UnassignAllCommand command = new UnassignAllCommand(VALID_CLASSGROUP_MATH, assignment);

        assertCommandFailure(command, modelWithoutMathClass,
                String.format(UnassignAllCommand.MESSAGE_NO_STUDENTS_FOUND, VALID_CLASSGROUP_MATH));
    }

    /**
     * Tests that unassigning from an empty address book throws a CommandException.
     * Verifies that the command fails when there are no students at all.
     */
    @Test
    public void execute_emptyAddressBook_throwsCommandException() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH);
        UnassignAllCommand command = new UnassignAllCommand(VALID_CLASSGROUP_MATH, assignment);

        assertCommandFailure(command, emptyModel,
                String.format(UnassignAllCommand.MESSAGE_NO_STUDENTS_FOUND, VALID_CLASSGROUP_MATH));
    }

    /**
     * Tests that class group name matching is case-insensitive.
     * Verifies that "math 3pm" matches students enrolled in "Math 3PM".
     */
    @Test
    public void execute_caseInsensitiveClassGroupMatch_success() {
        // Add student with "Math 3PM" class and assignment
        Model modelWithMathClass = new ModelManager(new AddressBook(), new UserPrefs());
        Person alice = new PersonBuilder().withName("Alice").withPhone("91234567")
                .withLevel("1").withClassGroups("Math 3PM")
                .withAssignments(VALID_ASSIGNMENT_MATH).build();
        modelWithMathClass.addPerson(alice);

        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH);
        // Use different case for class group name
        UnassignAllCommand command = new UnassignAllCommand("math 3pm", assignment);

        String expectedMessage = String.format(UnassignAllCommand.MESSAGE_SUCCESS,
                VALID_ASSIGNMENT_MATH, 1, "math 3pm");

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person aliceWithoutAssignment = new PersonBuilder(alice).withAssignments().build();
        expectedModel.addPerson(aliceWithoutAssignment);

        assertCommandSuccess(command, modelWithMathClass, expectedMessage, expectedModel);
    }

    /**
     * Tests the equals method for UnassignAllCommand.
     * Verifies correct equality behavior for same objects, same values, different types,
     * null values, different class groups, and different assignments.
     */
    @Test
    public void equals() {
        Assignment mathAssignment = new Assignment(VALID_ASSIGNMENT_MATH);
        Assignment physicsAssignment = new Assignment(VALID_ASSIGNMENT_PHYSICS);

        UnassignAllCommand unassignMathFromMathClass = new UnassignAllCommand(VALID_CLASSGROUP_MATH, mathAssignment);
        UnassignAllCommand unassignMathFromPhysicsClass = new UnassignAllCommand(VALID_CLASSGROUP_PHYSICS,
                mathAssignment);
        UnassignAllCommand unassignPhysicsFromMathClass = new UnassignAllCommand(VALID_CLASSGROUP_MATH,
                physicsAssignment);

        // same object -> returns true
        assertTrue(unassignMathFromMathClass.equals(unassignMathFromMathClass));

        // same values -> returns true
        UnassignAllCommand unassignMathFromMathClassCopy = new UnassignAllCommand(VALID_CLASSGROUP_MATH,
                mathAssignment);
        assertTrue(unassignMathFromMathClass.equals(unassignMathFromMathClassCopy));

        // different types -> returns false
        assertFalse(unassignMathFromMathClass.equals(1));

        // null -> returns false
        assertFalse(unassignMathFromMathClass.equals(null));

        // different class group -> returns false
        assertFalse(unassignMathFromMathClass.equals(unassignMathFromPhysicsClass));

        // different assignment -> returns false
        assertFalse(unassignMathFromMathClass.equals(unassignPhysicsFromMathClass));
    }

    /**
     * Tests the toString method for UnassignAllCommand.
     * Verifies that the string representation includes the class group name and assignment details.
     */
    @Test
    public void toStringMethod() {
        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH);
        UnassignAllCommand command = new UnassignAllCommand(VALID_CLASSGROUP_MATH, assignment);

        String expected = UnassignAllCommand.class.getCanonicalName()
                + "{classGroupName=" + VALID_CLASSGROUP_MATH
                + ", assignment=" + assignment + "}";
        assertEquals(expected, command.toString());
    }
}
