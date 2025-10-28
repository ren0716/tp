package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteAssignmentCommand;
import seedu.address.logic.commands.DeleteAssignmentCommand.DeleteAssignmentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;

public class DeleteAssignmentCommandParserTest {

    private final DeleteAssignmentCommandParser parser = new DeleteAssignmentCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteAssignmentCommand() throws Exception {
        String classGroup = "maths";
        String userInput = "1 " + PREFIX_CLASSGROUP + classGroup + " "
                + PREFIX_ASSIGNMENT + "HW1 "
                + PREFIX_ASSIGNMENT + "Lab2";

        DeleteAssignmentDescriptor descriptor = new DeleteAssignmentDescriptor();
        Set<Assignment> expectedAssignments = Set.of(
                new Assignment("hw1", classGroup),
                new Assignment("lab2", classGroup)
        );
        descriptor.setAssignments(expectedAssignments);

        DeleteAssignmentCommand expectedCommand =
                new DeleteAssignmentCommand(INDEX_FIRST_PERSON, descriptor);

        DeleteAssignmentCommand result = parser.parse(userInput);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parse_noAssignmentPrefix_descriptorHasNoAssignments() throws Exception {
        String classGroup = "no-assign-class";
        String userInput = "1 " + PREFIX_CLASSGROUP + classGroup;

        DeleteAssignmentDescriptor descriptor = new DeleteAssignmentDescriptor();
        DeleteAssignmentCommand expectedCommand =
                new DeleteAssignmentCommand(INDEX_FIRST_PERSON, descriptor);

        DeleteAssignmentCommand result = parser.parse(userInput);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parse_singleEmptyAssignmentToken_setsEmptyAssignmentSet() throws Exception {
        String classGroup = "clear-class";
        // explicit empty assignment token: prefix present but no value
        String userInput = "1 " + PREFIX_CLASSGROUP + classGroup + " " + PREFIX_ASSIGNMENT;

        DeleteAssignmentDescriptor descriptor = new DeleteAssignmentDescriptor();
        descriptor.setAssignments(Collections.emptySet()); // explicit clear -> present empty set

        DeleteAssignmentCommand expectedCommand =
                new DeleteAssignmentCommand(INDEX_FIRST_PERSON, descriptor);

        DeleteAssignmentCommand result = parser.parse(userInput);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parse_missingClassGroup_throwsParseException() {
        String userInput = "1 " + PREFIX_ASSIGNMENT + "HW1";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAssignmentCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(userInput));
    }

    @Test
    public void parse_emptyClassGroup_throwsParseException() {
        // prefix present but no value for class group
        String userInput = "1 " + PREFIX_CLASSGROUP + " " + PREFIX_ASSIGNMENT + "HW1";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAssignmentCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        String userInput = "abc " + PREFIX_CLASSGROUP + "someclass " + PREFIX_ASSIGNMENT + "HW1";
        assertThrows(ParseException.class, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidAssignmentName_throwsParseException() {
        String userInput = "1 " + PREFIX_CLASSGROUP + "cs101 " + PREFIX_ASSIGNMENT + "!badname";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
