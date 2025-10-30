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
    public void parseValidSingleAssignment_returnsCommand() throws Exception {
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
    public void parseValidMultipleAssignments_returnsCommand() throws Exception {
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
    public void parse_missingClassPrefix_allowsDescriptorWithNoClassOrAssignments() throws Exception {
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
    public void parseEmptyClassToken_noAssignmentsParsed() throws Exception {
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
    public void parse_invalidIndex_throwsParseException() {
        String input = "one c/Math a/HW1";
        try {
            parser.parse(input);
            // if no exception, fail
            assertTrue(false, "Expected ParseException for invalid index");
        } catch (ParseException pe) {
            // message should indicate invalid command format or index error; assert non-empty message
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

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        String input = "1 c/Math a/HW1 p/john";
        try {
            parser.parse(input);
            assertTrue(false, "Expected ParseException for invalid name prefix");
        } catch (ParseException pe) {
            assertTrue(pe.getMessage() != null && !pe.getMessage().isEmpty());
        }
    }
}
