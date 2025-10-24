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

import seedu.address.commons.util.StringUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for AssignAllCommand.
 */
public class AssignAllCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /**
     * Tests that assigning an assignment to all students in a class group succeeds.
     * Verifies that the assignment is added to all students enrolled in the specified class.
     */
    @Test
    public void execute_assignToClassGroup_success() {
        // Setup: Add students with Math class
        Model modelWithMathClass = new ModelManager(new AddressBook(), new UserPrefs());
        Person alice = new PersonBuilder().withName("Alice").withPhone("91234567")
                .withLevel("1").withClassGroups(VALID_CLASSGROUP_MATH).build();
        Person bob = new PersonBuilder().withName("Bob").withPhone("92345678")
                .withLevel("2").withClassGroups(VALID_CLASSGROUP_MATH).build();
        modelWithMathClass.addPerson(alice);
        modelWithMathClass.addPerson(bob);

        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH.toLowerCase(), VALID_CLASSGROUP_MATH.toLowerCase());
        AssignAllCommand command = new AssignAllCommand(VALID_CLASSGROUP_MATH.toLowerCase(), assignment);

        String expectedMessage = String.format(AssignAllCommand.MESSAGE_SUCCESS,
                VALID_ASSIGNMENT_MATH.toLowerCase(), 2, VALID_CLASSGROUP_MATH.toLowerCase());

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person aliceWithAssignment = new PersonBuilder(alice).withAssignments(VALID_CLASSGROUP_MATH.toLowerCase(), VALID_ASSIGNMENT_MATH.toLowerCase()).build();
        Person bobWithAssignment = new PersonBuilder(bob).withAssignments(VALID_CLASSGROUP_MATH.toLowerCase(), VALID_ASSIGNMENT_MATH.toLowerCase()).build();
        expectedModel.addPerson(aliceWithAssignment);
        expectedModel.addPerson(bobWithAssignment);

        assertCommandSuccess(command, modelWithMathClass, expectedMessage, expectedModel);
    }

    /**
     * Tests that assigning an assignment to a single student in a class succeeds.
     * Verifies that the command works correctly when only one student is enrolled in the class.
     */
    @Test
    public void execute_assignToOneStudentInClass_success() {
        // Setup: Add one student with Physics class
        Model modelWithPhysicsClass = new ModelManager(new AddressBook(), new UserPrefs());
        Person charlie = new PersonBuilder().withName("Charlie").withPhone("93456789")
                .withLevel("3").withClassGroups(VALID_CLASSGROUP_PHYSICS).build();
        modelWithPhysicsClass.addPerson(charlie);

        Assignment assignment = new Assignment(VALID_ASSIGNMENT_PHYSICS.toLowerCase(), VALID_CLASSGROUP_PHYSICS.toLowerCase());
        AssignAllCommand command = new AssignAllCommand(VALID_CLASSGROUP_PHYSICS.toLowerCase(), assignment);

        String expectedMessage = String.format(AssignAllCommand.MESSAGE_SUCCESS,
                VALID_ASSIGNMENT_PHYSICS.toLowerCase(), 1, VALID_CLASSGROUP_PHYSICS.toLowerCase());

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person charlieWithAssignment = new PersonBuilder(charlie).withAssignments(VALID_CLASSGROUP_PHYSICS.toLowerCase(), VALID_ASSIGNMENT_PHYSICS.toLowerCase()).build();
        expectedModel.addPerson(charlieWithAssignment);

        assertCommandSuccess(command, modelWithPhysicsClass, expectedMessage, expectedModel);
    }

    /**
     * Tests that assigning an assignment to multiple students in a class succeeds.
     * Verifies that only students enrolled in the specified class receive the assignment.
     */
    @Test
    public void execute_assignToMultipleClassStudents_success() {
        // Setup: Add students, some with Math class, some without
        Model modelWithMixedClasses = new ModelManager(new AddressBook(), new UserPrefs());
        Person alice = new PersonBuilder().withName("Alice").withPhone("91234567")
                .withLevel("1").withClassGroups(VALID_CLASSGROUP_MATH).build();
        Person bob = new PersonBuilder().withName("Bob").withPhone("92345678")
                .withLevel("2").withClassGroups(VALID_CLASSGROUP_PHYSICS).build();
        Person charlie = new PersonBuilder().withName("Charlie").withPhone("93456789")
                .withLevel("3").withClassGroups(VALID_CLASSGROUP_MATH).build();
        modelWithMixedClasses.addPerson(alice);
        modelWithMixedClasses.addPerson(bob);
        modelWithMixedClasses.addPerson(charlie);

        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH.toLowerCase(), VALID_CLASSGROUP_MATH.toLowerCase());
        AssignAllCommand command = new AssignAllCommand(VALID_CLASSGROUP_MATH.toLowerCase(), assignment);

        // Only Alice and Charlie should get the assignment (they have Math class)
        String expectedMessage = String.format(AssignAllCommand.MESSAGE_SUCCESS,
                VALID_ASSIGNMENT_MATH.toLowerCase(), 2, VALID_CLASSGROUP_MATH.toLowerCase());

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person aliceWithAssignment = new PersonBuilder(alice).withAssignments(VALID_CLASSGROUP_MATH.toLowerCase(), VALID_ASSIGNMENT_MATH.toLowerCase()).build();
        Person charlieWithAssignment = new PersonBuilder(charlie).withAssignments(VALID_CLASSGROUP_MATH.toLowerCase(), VALID_ASSIGNMENT_MATH.toLowerCase()).build();
        expectedModel.addPerson(aliceWithAssignment);
        expectedModel.addPerson(bob); // Bob doesn't have Math class, so no assignment
        expectedModel.addPerson(charlieWithAssignment);

        assertCommandSuccess(command, modelWithMixedClasses, expectedMessage, expectedModel);
    }

    /**
     * Tests that assigning an assignment skips students who already have the assignment.
     * Verifies that students who already possess the assignment are not reassigned it.
     */
    @Test
    public void execute_studentAlreadyHasAssignment_skipsStudent() {
        // Setup: Add student with Math class and already has the assignment
        Model modelWithAssignment = new ModelManager(new AddressBook(), new UserPrefs());
        Person alice = new PersonBuilder().withName("Alice").withPhone("91234567")
                .withLevel("1").withClassGroups(VALID_CLASSGROUP_MATH)
                .withAssignments(VALID_CLASSGROUP_MATH, VALID_ASSIGNMENT_MATH).build();
        Person bob = new PersonBuilder().withName("Bob").withPhone("92345678")
                .withLevel("2").withClassGroups(VALID_CLASSGROUP_MATH).build();
        modelWithAssignment.addPerson(alice);
        modelWithAssignment.addPerson(bob);

        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH.toLowerCase(), VALID_CLASSGROUP_MATH.toLowerCase());
        AssignAllCommand command = new AssignAllCommand(VALID_CLASSGROUP_MATH.toLowerCase(), assignment);

        // Only Bob should get the assignment (Alice already has it)
        String expectedMessage = String.format(AssignAllCommand.MESSAGE_SUCCESS,
                VALID_ASSIGNMENT_MATH.toLowerCase(), 1, VALID_CLASSGROUP_MATH.toLowerCase());

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person bobWithAssignment = new PersonBuilder(bob).withAssignments(VALID_CLASSGROUP_MATH.toLowerCase(), VALID_ASSIGNMENT_MATH.toLowerCase()).build();
        expectedModel.addPerson(alice); // Alice already has assignment
        expectedModel.addPerson(bobWithAssignment);

        assertCommandSuccess(command, modelWithAssignment, expectedMessage, expectedModel);
    }

    /**
     * Tests that assigning an assignment already assigned to all student of the class throws a CommandException.
     * Verifies that the command fails when all students in the specified class have the assignment.
     */
    @Test
    public void execute_assignmentsAlreadyAssignedToAllStudents_throwsCommandException() {
        // Setup: Add students with Math class who already have the assignment
        Model modelAllHaveAssignment = new ModelManager(new AddressBook(), new UserPrefs());
        Person alice = new PersonBuilder().withName("Alice").withPhone("91234567")
                .withLevel("1").withClassGroups(VALID_CLASSGROUP_MATH)
                .withAssignments(VALID_CLASSGROUP_MATH, VALID_ASSIGNMENT_MATH).build();
        Person bob = new PersonBuilder().withName("Bob").withPhone("92345678")
                .withLevel("2").withClassGroups(VALID_CLASSGROUP_MATH)
                .withAssignments(VALID_CLASSGROUP_MATH, VALID_ASSIGNMENT_MATH).build();
        modelAllHaveAssignment.addPerson(alice);
        modelAllHaveAssignment.addPerson(bob);

        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH.toLowerCase(), VALID_CLASSGROUP_MATH.toLowerCase());
        AssignAllCommand command = new AssignAllCommand(VALID_CLASSGROUP_MATH.toLowerCase(), assignment);

        assertCommandFailure(command, modelAllHaveAssignment,
                String.format(AssignAllCommand.MESSAGE_ALREADY_ASSIGNED,
                        StringUtil.toTitleCase(VALID_CLASSGROUP_MATH.toLowerCase()),
                        StringUtil.toTitleCase(VALID_ASSIGNMENT_MATH.toLowerCase())));
    }

    /**
     * Tests that unassigning from a non-existent class group throws a CommandException.
     * Verifies that the command fails when no students are enrolled in the specified class.
     */
    @Test
    public void execute_noStudentsInClass_throwsCommandException() {
        // Setup: Empty model or students without the specified class
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person alice = new PersonBuilder().withName("Alice").withPhone("91234567")
                .withLevel("1").withClassGroups(VALID_CLASSGROUP_PHYSICS).build();
        emptyModel.addPerson(alice);

        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH.toLowerCase(), VALID_CLASSGROUP_MATH.toLowerCase());
        AssignAllCommand command = new AssignAllCommand(VALID_CLASSGROUP_MATH.toLowerCase(), assignment);

        assertCommandFailure(command, emptyModel,
                String.format(AssignAllCommand.MESSAGE_NO_STUDENTS_FOUND,
                        StringUtil.toTitleCase(VALID_CLASSGROUP_MATH.toLowerCase())));
    }

    /**
     * Tests that assigning to an empty address book throws a CommandException.
     * Verifies that the command fails when there are no students in the address book.
     */
    @Test
    public void execute_emptyAddressBook_throwsCommandException() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH.toLowerCase(), VALID_CLASSGROUP_MATH.toLowerCase());
        AssignAllCommand command = new AssignAllCommand(VALID_CLASSGROUP_MATH.toLowerCase(), assignment);

        assertCommandFailure(command, emptyModel,
                String.format(AssignAllCommand.MESSAGE_NO_STUDENTS_FOUND,
                        StringUtil.toTitleCase(VALID_CLASSGROUP_MATH.toLowerCase())));
    }

    /**
     * Tests that class group name matching is case-insensitive.
     * Verifies that "math 3pm" matches students enrolled in "Math 3PM".
     */
    @Test
    public void execute_caseInsensitiveClassGroupMatch_success() {
        // Setup: Add student with "Math 3PM" class
        Model modelWithMathClass = new ModelManager(new AddressBook(), new UserPrefs());
        Person alice = new PersonBuilder().withName("Alice").withPhone("91234567")
                .withLevel("1").withClassGroups("Math 3PM").build();
        modelWithMathClass.addPerson(alice);

        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH.toLowerCase(), "math 3pm");
        // Use different case for class group name
        AssignAllCommand command = new AssignAllCommand("math 3pm", assignment);

        String expectedMessage = String.format(AssignAllCommand.MESSAGE_SUCCESS,
                VALID_ASSIGNMENT_MATH.toLowerCase(), 1, "math 3pm");

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person aliceWithAssignment = new PersonBuilder(alice).withAssignments("math 3pm", VALID_ASSIGNMENT_MATH.toLowerCase()).build();
        expectedModel.addPerson(aliceWithAssignment);

        assertCommandSuccess(command, modelWithMathClass, expectedMessage, expectedModel);
    }

    /**
     * Tests the equals method for AssignAllCommand.
     * Verifies correct equality behavior for same objects, same values, different types,
     * null values, different class groups, and different assignments.
     */
    @Test
    public void equals() {
        Assignment mathAssignment = new Assignment(VALID_ASSIGNMENT_MATH, VALID_CLASSGROUP_MATH);
        Assignment physicsAssignment = new Assignment(VALID_ASSIGNMENT_PHYSICS, VALID_CLASSGROUP_PHYSICS);

        AssignAllCommand assignMathToMathClass = new AssignAllCommand(VALID_CLASSGROUP_MATH, mathAssignment);
        AssignAllCommand assignMathToPhysicsClass = new AssignAllCommand(VALID_CLASSGROUP_PHYSICS, mathAssignment);
        AssignAllCommand assignPhysicsToMathClass = new AssignAllCommand(VALID_CLASSGROUP_MATH, physicsAssignment);

        // same object -> returns true
        assertTrue(assignMathToMathClass.equals(assignMathToMathClass));

        // same values -> returns true
        AssignAllCommand assignMathToMathClassCopy = new AssignAllCommand(VALID_CLASSGROUP_MATH, mathAssignment);
        assertTrue(assignMathToMathClass.equals(assignMathToMathClassCopy));

        // different types -> returns false
        assertFalse(assignMathToMathClass.equals(1));

        // null -> returns false
        assertFalse(assignMathToMathClass.equals(null));

        // different class group -> returns false
        assertFalse(assignMathToMathClass.equals(assignMathToPhysicsClass));

        // different assignment -> returns false
        assertFalse(assignMathToMathClass.equals(assignPhysicsToMathClass));
    }

    /**
     * Tests the toString method for AssignAllCommand.
     * Verifies that the string representation includes the class group name and assignment details.
     */
    @Test
    public void toStringMethod() {
        Assignment assignment = new Assignment(VALID_ASSIGNMENT_MATH, VALID_CLASSGROUP_MATH);
        AssignAllCommand command = new AssignAllCommand(VALID_CLASSGROUP_MATH, assignment);

        String expected = AssignAllCommand.class.getCanonicalName()
                + "{classGroupName=" + VALID_CLASSGROUP_MATH
                + ", assignment=" + assignment + "}";
        assertEquals(expected, command.toString());
    }
}
