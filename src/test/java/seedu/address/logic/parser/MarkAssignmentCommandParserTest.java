package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MarkAssignmentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;
import seedu.address.testutil.AssignmentBuilder;

/**
 * Unit tests for {@code MarkAssignmentCommandParser}.
 */
public class MarkAssignmentCommandParserTest {

    private final MarkAssignmentCommandParser parser = new MarkAssignmentCommandParser();

    /**
     * Tests that a valid input string is successfully parsed into a {@code MarkAssignmentCommand}.
     */
    @Test
    public void parse_validInput_success() {
        Assignment expectedAssignment = new AssignmentBuilder()
                .withName("physics-1800")
                .withClassGroup("physics-1800")
                .build();

        // Test single index
        String singleIndexInput = "1 c/physics-1800 a/Physics-1800";
        List<Index> expectedSingleIndex = Arrays.asList(Index.fromOneBased(1));
        try {
            var command = parser.parse(singleIndexInput);
            var expectedCommand = new MarkAssignmentCommand(expectedSingleIndex, expectedAssignment);
            assertEquals(expectedCommand, command);
        } catch (ParseException pe) {
            fail("Unexpected ParseException thrown for valid single index input.");
        }

        // Test index range
        String rangeInput = "1-3 c/physics-1800 a/Physics-1800";
        List<Index> expectedRange = Arrays.asList(
                Index.fromOneBased(1),
                Index.fromOneBased(2),
                Index.fromOneBased(3)
        );
        try {
            var command = parser.parse(rangeInput);
            var expectedCommand = new MarkAssignmentCommand(expectedRange, expectedAssignment);
            assertEquals(expectedCommand, command);
        } catch (ParseException pe) {
            fail("Unexpected ParseException thrown for valid range input.");
        }
    }

    /**
     * Tests that parsing fails when the assignment prefix is missing.
     */
    @Test
    public void parse_missingAssignment_throwsParseException() {
        String userInput = "1"; // Missing assignment prefix
        String expectedMessage = String.format(
            MESSAGE_INVALID_COMMAND_FORMAT,
            MarkAssignmentCommand.MESSAGE_USAGE
        );
        assertParseFailure(parser, userInput, expectedMessage);
    }

    /**
     * Tests that parsing fails when an invalid index is provided.
     */
    @Test
    public void parse_invalidIndex_throwsParseException() {
        String userInput = "a c/physics-1800 a/Physics-1800"; // 'a' is not a valid index
        String expectedMessage = ParserUtil.MESSAGE_INVALID_INDEX;
        assertParseFailure(parser, userInput, expectedMessage);
    }

    /**
     * Helper method to assert that parsing fails with the expected message.
     *
     * @param parser the parser to test
     * @param userInput the input string to parse
     * @param expectedMessage the expected error message
     */
    private void assertParseFailure(MarkAssignmentCommandParser parser, String userInput, String expectedMessage) {
        try {
            parser.parse(userInput);
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(expectedMessage, pe.getMessage());
        }
    }
}
