package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.UnassignAllCommand;
import seedu.address.model.assignment.Assignment;

/**
 * Contains unit tests for UnassignAllCommandParser.
 * Tests the parsing of user input arguments and validation of command format.
 */
public class UnassignAllCommandParserTest {

    private UnassignAllCommandParser parser = new UnassignAllCommandParser();

    /**
     * Tests that parsing with all valid fields succeeds.
     * Verifies that both class group and assignment are correctly parsed.
     */
    @Test
    public void parse_allFieldsPresent_success() {
        String classGroupName = "Math 3PM";
        String assignmentName = "Homework1";
        Assignment assignment = new Assignment(assignmentName);

        // Valid input with both prefixes
        assertParseSuccess(parser,
                " " + PREFIX_CLASSGROUP + classGroupName + " " + PREFIX_ASSIGNMENT + assignmentName,
                new UnassignAllCommand(classGroupName, assignment));

        // Valid input with prefixes in different order
        assertParseSuccess(parser,
                " " + PREFIX_ASSIGNMENT + assignmentName + " " + PREFIX_CLASSGROUP + classGroupName,
                new UnassignAllCommand(classGroupName, assignment));
    }

    /**
     * Tests that parsing with extra whitespace succeeds.
     * Verifies that the parser trims whitespace from input values.
     */
    @Test
    public void parse_extraWhitespace_success() {
        String classGroupName = "Math 3PM";
        String assignmentName = "Homework1";
        Assignment assignment = new Assignment(assignmentName);

        // Extra spaces before and after values
        assertParseSuccess(parser,
                " " + PREFIX_CLASSGROUP + "  " + classGroupName + "  " + PREFIX_ASSIGNMENT + "  " + assignmentName,
                new UnassignAllCommand(classGroupName, assignment));
    }

    /**
     * Tests that parsing fails when class group prefix is missing.
     * Verifies that the parser detects missing required prefixes.
     */
    @Test
    public void parse_missingClassGroupPrefix_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignAllCommand.MESSAGE_USAGE);

        // Missing class group prefix
        assertParseFailure(parser, " Math 3PM " + PREFIX_ASSIGNMENT + "Homework1", expectedMessage);
    }

    /**
     * Tests that parsing fails when assignment prefix is missing.
     * Verifies that the parser detects missing required prefixes.
     */
    @Test
    public void parse_missingAssignmentPrefix_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignAllCommand.MESSAGE_USAGE);

        // Missing assignment prefix
        assertParseFailure(parser, " " + PREFIX_CLASSGROUP + "Math 3PM Homework1", expectedMessage);
    }

    /**
     * Tests that parsing fails when both prefixes are missing.
     * Verifies that the parser detects when no required prefixes are present.
     */
    @Test
    public void parse_missingAllPrefixes_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignAllCommand.MESSAGE_USAGE);

        // No prefixes
        assertParseFailure(parser, " Math 3PM Homework1", expectedMessage);
    }

    /**
     * Tests that parsing fails when class group value is empty.
     * Verifies that the parser validates non-empty class group names.
     */
    @Test
    public void parse_emptyClassGroup_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignAllCommand.MESSAGE_USAGE);

        // Empty class group value
        assertParseFailure(parser, " " + PREFIX_CLASSGROUP + " " + PREFIX_ASSIGNMENT + "Homework1",
                expectedMessage);

        // Class group with only whitespace
        assertParseFailure(parser, " " + PREFIX_CLASSGROUP + "   " + PREFIX_ASSIGNMENT + "Homework1",
                expectedMessage);
    }

    /**
     * Tests that parsing fails when assignment value is empty.
     * Verifies that the parser validates non-empty assignment names.
     */
    @Test
    public void parse_emptyAssignment_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignAllCommand.MESSAGE_USAGE);

        // Empty assignment value
        assertParseFailure(parser, " " + PREFIX_CLASSGROUP + "Math 3PM " + PREFIX_ASSIGNMENT,
                expectedMessage);

        // Assignment with only whitespace
        assertParseFailure(parser, " " + PREFIX_CLASSGROUP + "Math 3PM " + PREFIX_ASSIGNMENT + "   ",
                expectedMessage);
    }

    /**
     * Tests that parsing fails when both values are empty.
     * Verifies that the parser detects when required values are missing.
     */
    @Test
    public void parse_emptyBothValues_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignAllCommand.MESSAGE_USAGE);

        // Both values empty
        assertParseFailure(parser, " " + PREFIX_CLASSGROUP + " " + PREFIX_ASSIGNMENT,
                expectedMessage);
    }

    /**
     * Tests that parsing fails when there is a preamble before the prefixes.
     * Verifies that the parser rejects input with unexpected text before required prefixes.
     */
    @Test
    public void parse_preamblePresent_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignAllCommand.MESSAGE_USAGE);

        // Preamble present
        assertParseFailure(parser, "some text " + PREFIX_CLASSGROUP + "Math 3PM " + PREFIX_ASSIGNMENT + "Homework1",
                expectedMessage);
    }

    /**
     * Tests that parsing fails when class group prefix is duplicated.
     * Verifies that the parser detects duplicate prefixes for class group.
     */
    @Test
    public void parse_duplicateClassGroupPrefix_failure() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CLASSGROUP);

        // Duplicate ClassGroup prefix
        assertParseFailure(parser,
                " " + PREFIX_CLASSGROUP + "Math 3PM " + PREFIX_CLASSGROUP + "Physics 2PM "
                        + PREFIX_ASSIGNMENT + "Homework1",
                expectedMessage);
    }

    /**
     * Tests that parsing fails when assignment prefix is duplicated.
     * Verifies that the parser detects duplicate prefixes for assignment.
     */
    @Test
    public void parse_duplicateAssignmentPrefix_failure() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ASSIGNMENT);

        // Duplicate assignment prefix
        assertParseFailure(parser,
                " " + PREFIX_CLASSGROUP + "Math 3PM " + PREFIX_ASSIGNMENT + "Homework1 "
                        + PREFIX_ASSIGNMENT + "Homework2",
                expectedMessage);
    }

    /**
     * Tests that parsing succeeds with class group names containing special characters.
     * Verifies that the parser handles class group names with spaces and special characters.
     */
    @Test
    public void parse_classGroupWithSpecialCharacters_success() {
        String classGroupName = "Math-3PM (Advanced)";
        String assignmentName = "Homework1";
        Assignment assignment = new Assignment(assignmentName);

        assertParseSuccess(parser,
                " " + PREFIX_CLASSGROUP + classGroupName + " " + PREFIX_ASSIGNMENT + assignmentName,
                new UnassignAllCommand(classGroupName, assignment));
    }

    /**
     * Tests that parsing succeeds with assignment names containing special characters.
     * Verifies that the parser handles assignment names with spaces and special characters.
     */
    @Test
    public void parse_assignmentWithSpecialCharacters_success() {
        String classGroupName = "Math 3PM";
        String assignmentName = "Homework-1 (Chapter 3)";
        Assignment assignment = new Assignment(assignmentName);

        assertParseSuccess(parser,
                " " + PREFIX_CLASSGROUP + classGroupName + " " + PREFIX_ASSIGNMENT + assignmentName,
                new UnassignAllCommand(classGroupName, assignment));
    }

    /**
     * Tests that parsing succeeds with numeric class group names.
     * Verifies that the parser handles class groups with numbers.
     */
    @Test
    public void parse_numericClassGroup_success() {
        String classGroupName = "Math101";
        String assignmentName = "Assignment1";
        Assignment assignment = new Assignment(assignmentName);

        assertParseSuccess(parser,
                " " + PREFIX_CLASSGROUP + classGroupName + " " + PREFIX_ASSIGNMENT + assignmentName,
                new UnassignAllCommand(classGroupName, assignment));
    }

    /**
     * Tests that parsing succeeds with numeric assignment names.
     * Verifies that the parser handles assignments with numbers only.
     */
    @Test
    public void parse_numericAssignment_success() {
        String classGroupName = "Math 3PM";
        String assignmentName = "123";
        Assignment assignment = new Assignment(assignmentName);

        assertParseSuccess(parser,
                " " + PREFIX_CLASSGROUP + classGroupName + " " + PREFIX_ASSIGNMENT + assignmentName,
                new UnassignAllCommand(classGroupName, assignment));
    }
}
