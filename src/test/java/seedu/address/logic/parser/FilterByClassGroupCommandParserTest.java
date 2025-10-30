package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterByClassGroupCommand;
import seedu.address.model.person.StudentInClassGroupPredicate;

/**
 * Unit tests for {@link FilterByClassGroupCommandParser}.
 */
public class FilterByClassGroupCommandParserTest {

    private final FilterByClassGroupCommandParser parser = new FilterByClassGroupCommandParser();

    @Test
    public void parse_validClassGroup_success() {
        String classGroupName = "1a";
        String userInput = " " + PREFIX_CLASSGROUP + classGroupName;

        StudentInClassGroupPredicate predicate = new StudentInClassGroupPredicate(classGroupName);
        FilterByClassGroupCommand expectedCommand = new FilterByClassGroupCommand(predicate);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_classGroupWithWhitespace_success() {
        String classGroupName = "math-1800";
        String userInput = "  " + PREFIX_CLASSGROUP + classGroupName + "   ";

        StudentInClassGroupPredicate predicate = new StudentInClassGroupPredicate(classGroupName);
        FilterByClassGroupCommand expectedCommand = new FilterByClassGroupCommand(predicate);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingClassGroupPrefix_failure() {
        String userInput = "1a"; // no prefix
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterByClassGroupCommand.MESSAGE_USAGE);

        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_multipleClassGroups_failure() {
        String userInput = " " + PREFIX_CLASSGROUP + "1a " + PREFIX_CLASSGROUP + "1b";
        String expectedMessage = getErrorMessageForDuplicatePrefixes(PREFIX_CLASSGROUP);

        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_invalidPrefixes_failure() {
        String userInput = " " + PREFIX_CLASSGROUP + "1A " + PREFIX_NAME + "Alice";
        String expectedMessage = seedu.address.logic.Messages.getErrorMessageForInvalidPrefixes(PREFIX_NAME);

        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_preamblePresent_failure() {
        String userInput = " some random text " + PREFIX_CLASSGROUP + "1a";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterByClassGroupCommand.MESSAGE_USAGE);

        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_emptyInput_failure() {
        String userInput = "   ";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterByClassGroupCommand.MESSAGE_USAGE);

        assertParseFailure(parser, userInput, expectedMessage);
    }
}
