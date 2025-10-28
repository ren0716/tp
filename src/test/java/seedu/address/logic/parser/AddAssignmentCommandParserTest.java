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

import seedu.address.logic.commands.AddAssignmentCommand;
import seedu.address.logic.commands.AddAssignmentCommand.AddAssignmentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;

public class AddAssignmentCommandParserTest {

    private final AddAssignmentCommandParser parser = new AddAssignmentCommandParser();

    @Test
    public void parse_validArgs_returnsAddAssignmentCommand() throws Exception {
        String classGroup = "test-class";
        String userInput = "1 " + PREFIX_CLASSGROUP + classGroup + " "
                + PREFIX_ASSIGNMENT + "HW1 "
                + PREFIX_ASSIGNMENT + "Lab2";

        AddAssignmentDescriptor descriptor = new AddAssignmentDescriptor();
        Set<Assignment> expectedAssignments = Set.of(
                new Assignment("hw1", classGroup),
                new Assignment("lab2", classGroup)
        );
        descriptor.setAssignments(expectedAssignments);

        AddAssignmentCommand expectedCommand =
                new AddAssignmentCommand(INDEX_FIRST_PERSON, descriptor);

        AddAssignmentCommand result = parser.parse(userInput);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parse_noAssignmentPrefix_descriptorHasNoAssignments() throws Exception {
        String classGroup = "no-assign-class";
        String userInput = "1 " + PREFIX_CLASSGROUP + classGroup;

        AddAssignmentDescriptor descriptor = new AddAssignmentDescriptor();
        AddAssignmentCommand expectedCommand =
                new AddAssignmentCommand(INDEX_FIRST_PERSON, descriptor);

        AddAssignmentCommand result = parser.parse(userInput);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parse_singleEmptyAssignmentToken_setsEmptyAssignmentSet() throws Exception {
        String classGroup = "clear-class";
        // explicit empty assignment token: prefix present but no value
        String userInput = "1 " + PREFIX_CLASSGROUP + classGroup + " " + PREFIX_ASSIGNMENT;

        AddAssignmentDescriptor descriptor = new AddAssignmentDescriptor();
        descriptor.setAssignments(Collections.emptySet()); // explicit clear -> present empty set

        AddAssignmentCommand expectedCommand =
                new AddAssignmentCommand(INDEX_FIRST_PERSON, descriptor);

        AddAssignmentCommand result = parser.parse(userInput);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parse_missingClassGroup_throwsParseException() {
        String userInput = "1 " + PREFIX_ASSIGNMENT + "HW1";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAssignmentCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        String userInput = "abc " + PREFIX_CLASSGROUP + "someclass " + PREFIX_ASSIGNMENT + "HW1";
        assertThrows(ParseException.class, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () -> parser.parse(userInput));
    }
}
