package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_ASSIGNMENT_NOT_DELETED;
import static seedu.address.logic.Messages.MESSAGE_STUDENT_NOT_IN_CLASS_GROUP;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteAssignmentCommand;
import seedu.address.logic.commands.DeleteAssignmentCommand.DeleteAssignmentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.testutil.AssignmentBuilder;

/**
 * Unit tests for DeleteAssignmentCommandParser.
 */
public class DeleteAssignmentCommandParserTest {

    private final DeleteAssignmentCommandParser parser = new DeleteAssignmentCommandParser();
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void parseValidSingleAssignmentThrowsParseException() throws ParseException {
        String input = "1 c/Math a/HW1";
        Index expectedIndex = Index.fromOneBased(1);

        DeleteAssignmentDescriptor expectedDescriptor = new DeleteAssignmentDescriptor();
        expectedDescriptor.setClassGroupName("Math");
        expectedDescriptor.setAssignments(Set.of(new Assignment("HW1", "math")));

        DeleteAssignmentCommand expectedCommand = new DeleteAssignmentCommand(expectedIndex, expectedDescriptor);

        DeleteAssignmentCommand result = parser.parse(input);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parseValidMultipleAssignmentsThrowsParseException() throws ParseException {
        String input = "2 c/Physics a/Lab1 a/Quiz2";
        Index expectedIndex = Index.fromOneBased(2);

        DeleteAssignmentDescriptor expectedDescriptor = new DeleteAssignmentDescriptor();
        expectedDescriptor.setClassGroupName("Physics");
        expectedDescriptor.setAssignments(Set.of(
                new Assignment("Lab1", "physics"),
                new Assignment("Quiz2", "physics")
        ));

        DeleteAssignmentCommand expectedCommand = new DeleteAssignmentCommand(expectedIndex, expectedDescriptor);

        DeleteAssignmentCommand result = parser.parse(input);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parseMissingClassPrefixAllowsDescriptorWithNoClassOrAssignmentsThrowsParseException()
            throws ParseException {
        // class prefix missing; parser should still succeed but descriptor has null class and no assignments
        String input = "3 a/HW1";
        Index expectedIndex = Index.fromOneBased(3);

        DeleteAssignmentDescriptor expectedDescriptor = new DeleteAssignmentDescriptor();
        // no class provided: classGroupName remains null, assignments not set
        DeleteAssignmentCommand expectedCommand = new DeleteAssignmentCommand(expectedIndex, expectedDescriptor);

        DeleteAssignmentCommand result = parser.parse(input);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parseEmptyClassTokenNoAssignmentsParsedThrowsParseException() throws ParseException {
        // class provided but empty token and no assignments parsed
        String input = "1 c/ a/HW1";
        Index expectedIndex = Index.fromOneBased(1);

        DeleteAssignmentDescriptor expectedDescriptor = new DeleteAssignmentDescriptor();
        expectedDescriptor.setClassGroupName("");
        // assignments should not be parsed when class token is empty
        DeleteAssignmentCommand expectedCommand = new DeleteAssignmentCommand(expectedIndex, expectedDescriptor);

        DeleteAssignmentCommand result = parser.parse(input);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parseInvalidIndexThrowsParseException() {
        String input = "one c/Math a/HW1";
        try {
            parser.parse(input);
            assertTrue(false, "Expected ParseException for invalid index");
        } catch (ParseException pe) {
            assertTrue(pe.getMessage() != null && !pe.getMessage().isEmpty());
        }
    }

    @Test
    public void parseDuplicateClassPrefixThrowsParseException() {
        // duplicate c/ prefix should be rejected
        String input = "1 c/Math c/Science a/HW1";
        try {
            parser.parse(input);
            assertTrue(false, "Expected ParseException for duplicate class prefix");
        } catch (ParseException pe) {
            assertTrue(pe.getMessage() != null && !pe.getMessage().isEmpty());
        }
    }

    @Test
    public void parseInvalidPrefixThrowsParseException() {
        String input = "1 c/Math a/HW1 p/john";
        try {
            parser.parse(input);
            assertTrue(false, "Expected ParseException for invalid name prefix");
        } catch (ParseException pe) {
            assertTrue(pe.getMessage() != null && !pe.getMessage().isEmpty());
        }
    }

    @Test
    public void parseClassOnlyNoAssignmentsThrowsParseException() throws ParseException {
        String input = "1 c/Math";
        Index expectedIndex = Index.fromOneBased(1);

        DeleteAssignmentDescriptor expectedDescriptor = new DeleteAssignmentDescriptor();
        expectedDescriptor.setClassGroupName("Math");

        DeleteAssignmentCommand expectedCommand = new DeleteAssignmentCommand(expectedIndex, expectedDescriptor);

        DeleteAssignmentCommand result = parser.parse(input);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parseSingleEmptyAssignmentTokenParsesAsEmptyAssignmentSetThrowsParseException() throws ParseException {
        String input = "1 c/Math a/";
        Index expectedIndex = Index.fromOneBased(1);

        DeleteAssignmentDescriptor expectedDescriptor = new DeleteAssignmentDescriptor();
        expectedDescriptor.setClassGroupName("Math");
        // explicit empty assignment set (clear token) should be parsed as an empty Set
        expectedDescriptor.setAssignments(Set.of());

        DeleteAssignmentCommand expectedCommand = new DeleteAssignmentCommand(expectedIndex, expectedDescriptor);

        DeleteAssignmentCommand result = parser.parse(input);
        assertEquals(expectedCommand, result);
    }

    /**
     * Ensures parser normalizes class name for constructed Assignment objects
     * so later command validation receives lowercase class names.
     */
    @Test
    public void parseValidSingleAssignmentMixedCaseClassThrowsParseException() throws ParseException {
        String input = "1 c/MaTh 3PM a/HW1";
        Index expectedIndex = Index.fromOneBased(1);

        DeleteAssignmentDescriptor expectedDescriptor = new DeleteAssignmentDescriptor();
        // raw class token preserved in descriptor
        expectedDescriptor.setClassGroupName("MaTh 3PM");
        // assignments parsed with class normalized to lowercase
        expectedDescriptor.setAssignments(Set.of(new Assignment("HW1", "math 3pm")));

        DeleteAssignmentCommand expectedCommand = new DeleteAssignmentCommand(expectedIndex, expectedDescriptor);

        DeleteAssignmentCommand result = parser.parse(input);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parseInvalidAssignmentNameThrowsParseException() {
        String input = "1 c/Math a/@@@";
        try {
            parser.parse(input);
            assertTrue(false, "Expected ParseException for invalid assignment name");
        } catch (ParseException pe) {
            assertTrue(pe.getMessage() != null && !pe.getMessage().isEmpty());
        }
    }

    @Test
    public void execute_studentNotInClassGroup_failure() {
        // pick a class the typical first person does not belong to
        Assignment assignmentWithInvalidClass = new AssignmentBuilder()
                .withName("HW1")
                .withClassGroup("nonexistent-class")
                .build();

        DeleteAssignmentCommand.DeleteAssignmentDescriptor descriptor =
                new DeleteAssignmentCommand.DeleteAssignmentDescriptor();
        // ensure descriptor indicates a class was provided so command reaches validation
        descriptor.setClassGroupName(assignmentWithInvalidClass.getClassGroupName());
        descriptor.setAssignments(Set.of(assignmentWithInvalidClass));
        DeleteAssignmentCommand command = new DeleteAssignmentCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(MESSAGE_STUDENT_NOT_IN_CLASS_GROUP,
                assignmentWithInvalidClass.getClassGroupName());
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_emptyAssignmentSet_failure() {
        DeleteAssignmentCommand.DeleteAssignmentDescriptor descriptor =
                new DeleteAssignmentCommand.DeleteAssignmentDescriptor();
        // Provide a class group so command reaches the assignment validation logic
        descriptor.setClassGroupName("some-class");
        // Explicitly set empty assignment
        descriptor.setAssignments(Set.of());
        DeleteAssignmentCommand command = new DeleteAssignmentCommand(INDEX_FIRST_PERSON, descriptor);

        // Class provided but assignment set empty: MESSAGE_ASSIGNMENT_NOT_DELETED
        assertCommandFailure(command, model, MESSAGE_ASSIGNMENT_NOT_DELETED);
    }
}
