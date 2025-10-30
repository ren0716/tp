package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_STUDENT_NOT_IN_CLASS_GROUP;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteAssignmentCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.testutil.AssignmentBuilder;


/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());


    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_studentNotInClassGroup_failure() {
        Assignment assignmentWithInvalidClass = new AssignmentBuilder()
                .withName("HW1")
                .withClassGroup("nonexistent-class")
                .build();

        DeleteAssignmentCommand.DeleteAssignmentDescriptor descriptor =
                new DeleteAssignmentCommand.DeleteAssignmentDescriptor();
        descriptor.setClassGroupName(assignmentWithInvalidClass.getClassGroupName());
        descriptor.setAssignments(Set.of(assignmentWithInvalidClass));
        DeleteAssignmentCommand command = new DeleteAssignmentCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(MESSAGE_STUDENT_NOT_IN_CLASS_GROUP,
                assignmentWithInvalidClass.getClassGroupName());
        assertCommandFailure(command, model, expectedMessage);
    }


}
