package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddAssignmentCommand;
import seedu.address.logic.commands.AddAssignmentCommand.AddAssignmentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;

/**
 * Unit tests for AddAssignmentCommandParser.
 */
public class AddAssignmentCommandParserTest {

    private final AddAssignmentCommandParser parser = new AddAssignmentCommandParser();

    @Test
    public void parseValidArgsSingleAssignmentReturnsCommand() throws Exception {
        String input = "1 c/Math a/HW1";
        Index expectedIndex = Index.fromOneBased(1);

        AddAssignmentDescriptor expectedDescriptor = new AddAssignmentDescriptor();
        expectedDescriptor.setClassGroupName("Math");
        expectedDescriptor.setAssignments(Set.of(new Assignment("HW1", "math")));

        AddAssignmentCommand expectedCommand = new AddAssignmentCommand(expectedIndex, expectedDescriptor);

        AddAssignmentCommand result = parser.parse(input);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parseValidArgsMultipleAssignmentsReturnsCommand() throws Exception {
        String input = "2 c/Physics a/Lab1 a/Quiz2";
        Index expectedIndex = Index.fromOneBased(2);

        AddAssignmentDescriptor expectedDescriptor = new AddAssignmentDescriptor();
        expectedDescriptor.setClassGroupName("Physics");
        expectedDescriptor.setAssignments(Set.of(
                new Assignment("Lab1", "physics"),
                new Assignment("Quiz2", "physics")
        ));

        AddAssignmentCommand expectedCommand = new AddAssignmentCommand(expectedIndex, expectedDescriptor);

        AddAssignmentCommand result = parser.parse(input);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parseMissingClassPrefixAllowsDescriptorWithNoClassOrAssignments() throws Exception {
        // class prefix missing; parser should still succeed but descriptor has null class and no assignments
        String input = "3 a/HW1";
        Index expectedIndex = Index.fromOneBased(3);

        AddAssignmentDescriptor expectedDescriptor = new AddAssignmentDescriptor();
        // no class provided -> classGroupName remains null, assignments not set
        AddAssignmentCommand expectedCommand = new AddAssignmentCommand(expectedIndex, expectedDescriptor);

        AddAssignmentCommand result = parser.parse(input);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parseEmptyClassTokenParsedAsEmptyClassNoAssignmentsParsed() throws Exception {
        // class provided but empty token -> descriptor.classGroupName == "" and no assignments parsed
        String input = "1 c/ a/HW1";
        Index expectedIndex = Index.fromOneBased(1);

        AddAssignmentDescriptor expectedDescriptor = new AddAssignmentDescriptor();
        expectedDescriptor.setClassGroupName("");
        // assignments should not be parsed when class token is empty
        AddAssignmentCommand expectedCommand = new AddAssignmentCommand(expectedIndex, expectedDescriptor);

        AddAssignmentCommand result = parser.parse(input);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        String input = "one c/Math a/HW1";
        try {
            parser.parse(input);
            // if no exception, fail
            assertTrue(false, "Expected ParseException for invalid index");
        } catch (ParseException pe) {
            // message should indicate invalid command format for this parser usage
            // just assert that an exception was thrown and message is non-empty
            assertTrue(pe.getMessage() != null && !pe.getMessage().isEmpty());
        }
    }

    @Test
    public void parse_duplicateClassPrefix_throwsParseException() {
        // duplicate c/ prefix should be rejected by ArgumentMultimap.verifyNoDuplicatePrefixesFor
        String input = "1 c/Math c/Science a/HW1";
        try {
            parser.parse(input);
            assertTrue(false, "Expected ParseException for duplicate class prefix");
        } catch (ParseException pe) {
            assertTrue(pe.getMessage() != null && !pe.getMessage().isEmpty());
        }
    }
}
