package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ASSIGNMENT_MATH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ASSIGNMENT_PHYSICS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASSGROUP_MATH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASSGROUP_PHYSICS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AssignAllCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;

/**
 * Contains unit tests for AssignAllCommandParser.
 */
public class AssignAllCommandParserTest {

    private AssignAllCommandParser parser = new AssignAllCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        String classGroup = VALID_CLASSGROUP_MATH;
        String assignmentName = VALID_ASSIGNMENT_MATH;
        Assignment expectedAssignment = new Assignment(assignmentName.toLowerCase(), classGroup.toLowerCase());

        // standard input
        assertParseSuccess(parser,
                " " + PREFIX_CLASSGROUP + classGroup + " " + PREFIX_ASSIGNMENT + assignmentName,
                new AssignAllCommand(classGroup.toLowerCase(), expectedAssignment));

        // whitespace in between
        assertParseSuccess(parser,
                "  " + PREFIX_CLASSGROUP + classGroup + "   " + PREFIX_ASSIGNMENT + assignmentName,
                new AssignAllCommand(classGroup.toLowerCase(), expectedAssignment));

        // different class group and assignment
        String physicsClass = VALID_CLASSGROUP_PHYSICS;
        String physicsAssignment = VALID_ASSIGNMENT_PHYSICS;
        Assignment expectedPhysicsAssignment =
                new Assignment(physicsAssignment.toLowerCase(), physicsClass.toLowerCase());
        assertParseSuccess(parser,
                " " + PREFIX_CLASSGROUP + physicsClass + " " + PREFIX_ASSIGNMENT + physicsAssignment,
                new AssignAllCommand(physicsClass.toLowerCase(), expectedPhysicsAssignment));
    }

    @Test
    public void parse_classGroupWithSpaces_success() {
        String classGroupWithSpaces = "Math 3PM";
        String assignmentName = VALID_ASSIGNMENT_MATH;
        Assignment expectedAssignment =
                new Assignment(assignmentName.toLowerCase(), classGroupWithSpaces.toLowerCase());

        assertParseSuccess(parser,
                " " + PREFIX_CLASSGROUP + classGroupWithSpaces + " " + PREFIX_ASSIGNMENT + assignmentName,
                new AssignAllCommand(classGroupWithSpaces.toLowerCase(), expectedAssignment));
    }

    @Test
    public void parse_assignmentWithSpaces_success() {
        String classGroup = VALID_CLASSGROUP_MATH;
        String assignmentWithSpaces = "Homework 1";
        Assignment expectedAssignment = new Assignment(assignmentWithSpaces.toLowerCase(), classGroup.toLowerCase());

        assertParseSuccess(parser,
                " " + PREFIX_CLASSGROUP + classGroup + " " + PREFIX_ASSIGNMENT + assignmentWithSpaces,
                new AssignAllCommand(classGroup.toLowerCase(), expectedAssignment));
    }

    @Test
    public void parse_missingClassGroupPrefix_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignAllCommand.MESSAGE_USAGE);

        // missing class group prefix
        assertParseFailure(parser,
                " " + VALID_CLASSGROUP_MATH + " " + PREFIX_ASSIGNMENT + VALID_ASSIGNMENT_MATH,
                expectedMessage);
    }

    @Test
    public void parse_missingAssignmentPrefix_failure() {
        String expectedMessage = Messages.MESSAGE_ASSIGNMENT_NOT_ADDED;

        // class prefix present, assignment provided without its prefix
        assertParseFailure(parser,
                " " + PREFIX_CLASSGROUP + VALID_CLASSGROUP_MATH + " " + VALID_ASSIGNMENT_MATH,
                expectedMessage);
    }

    @Test
    public void parse_missingBothPrefixes_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignAllCommand.MESSAGE_USAGE);

        // missing both prefixes
        assertParseFailure(parser,
                " " + VALID_CLASSGROUP_MATH + " " + VALID_ASSIGNMENT_MATH,
                expectedMessage);
    }

    @Test
    public void parse_emptyClassGroup_failure() {
        String expectedMessage = Messages.MESSAGE_CLASSES_NOT_ADDED;

        // class prefix present but empty, assignment prefix present with value
        assertParseFailure(parser,
                " " + PREFIX_CLASSGROUP + " " + PREFIX_ASSIGNMENT + VALID_ASSIGNMENT_MATH,
                expectedMessage);
    }

    @Test
    public void parse_emptyAssignment_failure() {
        AssignAllCommandParser parser = new AssignAllCommandParser();
        // class group provided, assignment prefix present but empty
        String userInput = " c/math a/";
        ParseException pe = org.junit.jupiter.api.Assertions.assertThrows(ParseException.class, (
        ) -> parser.parse(userInput));
        org.junit.jupiter.api.Assertions.assertEquals(seedu.address.logic.Messages.MESSAGE_ASSIGNMENT_NOT_ADDED,
                pe.getMessage());
    }

    @Test
    public void parse_duplicateClassGroupPrefix_failure() {
        // duplicate class group prefix
        assertParseFailure(parser,
                " " + PREFIX_CLASSGROUP + VALID_CLASSGROUP_MATH
                + " " + PREFIX_CLASSGROUP + VALID_CLASSGROUP_PHYSICS
                + " " + PREFIX_ASSIGNMENT + VALID_ASSIGNMENT_MATH,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CLASSGROUP));
    }

    @Test
    public void parse_duplicateAssignmentPrefix_failure() {
        // duplicate assignment prefix
        assertParseFailure(parser,
                " " + PREFIX_CLASSGROUP + VALID_CLASSGROUP_MATH
                + " " + PREFIX_ASSIGNMENT + VALID_ASSIGNMENT_MATH
                + " " + PREFIX_ASSIGNMENT + VALID_ASSIGNMENT_PHYSICS,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ASSIGNMENT));
    }

    @Test
    public void parse_preamblePresent_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignAllCommand.MESSAGE_USAGE);

        // preamble before prefixes
        assertParseFailure(parser,
                " some random text " + PREFIX_CLASSGROUP + VALID_CLASSGROUP_MATH
                + " " + PREFIX_ASSIGNMENT + VALID_ASSIGNMENT_MATH,
                expectedMessage);

        // number as preamble
        assertParseFailure(parser,
                " 1 " + PREFIX_CLASSGROUP + VALID_CLASSGROUP_MATH
                + " " + PREFIX_ASSIGNMENT + VALID_ASSIGNMENT_MATH,
                expectedMessage);
    }

    @Test
    public void parse_invalidPrefixOrder_success() {
        // assignment prefix before class group prefix should still work
        String classGroup = VALID_CLASSGROUP_MATH;
        String assignmentName = VALID_ASSIGNMENT_MATH;
        Assignment expectedAssignment = new Assignment(assignmentName.toLowerCase(), classGroup.toLowerCase());

        assertParseSuccess(parser,
                " " + PREFIX_ASSIGNMENT + assignmentName + " " + PREFIX_CLASSGROUP + classGroup,
                new AssignAllCommand(classGroup.toLowerCase(), expectedAssignment));
    }

    @Test
    public void parse_trailingWhitespace_success() {
        String classGroup = VALID_CLASSGROUP_MATH;
        String assignmentName = VALID_ASSIGNMENT_MATH;
        Assignment expectedAssignment = new Assignment(assignmentName.toLowerCase(), classGroup.toLowerCase());

        // trailing whitespace should be trimmed
        assertParseSuccess(parser,
                " " + PREFIX_CLASSGROUP + classGroup + "  " + PREFIX_ASSIGNMENT + assignmentName + "   ",
                new AssignAllCommand(classGroup.toLowerCase(), expectedAssignment));
    }

    @Test
    public void parse_leadingWhitespace_success() {
        String classGroup = VALID_CLASSGROUP_MATH;
        String assignmentName = VALID_ASSIGNMENT_MATH;
        Assignment expectedAssignment = new Assignment(assignmentName.toLowerCase(), classGroup.toLowerCase());

        // leading whitespace should be handled
        assertParseSuccess(parser,
                "   " + PREFIX_CLASSGROUP + classGroup + " " + PREFIX_ASSIGNMENT + assignmentName,
                new AssignAllCommand(classGroup.toLowerCase(), expectedAssignment));
    }
}
