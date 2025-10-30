package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteAssignmentCommand;
import seedu.address.logic.commands.DeleteAssignmentCommand.DeleteAssignmentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;

/**
 * Unit tests for DeleteAssignmentCommandParser.
 */
public class DeleteAssignmentCommandParserTest {

    private final DeleteAssignmentCommandParser parser = new DeleteAssignmentCommandParser();

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

    // language: java
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
}
