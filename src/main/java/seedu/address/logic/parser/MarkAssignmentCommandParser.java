package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MarkAssignmentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;

/**
 * Parses input arguments and creates a new {@link seedu.address.logic.commands.MarkAssignmentCommand}.
 */
public class MarkAssignmentCommandParser implements Parser<MarkAssignmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MarkAssignmentCommand
     * and returns a MarkAssignmentCommand object for execution.
     *
     * The expected format is: INDEX a/ASSIGNMENT_NAME
     *
     * @param args full user input string (arguments portion)
     * @return a MarkAssignmentCommand containing the parsed index and assignment
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public MarkAssignmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ASSIGNMENT);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MarkAssignmentCommand.MESSAGE_USAGE), pe);
        }

        Optional<String> assignmentValue = argMultimap.getValue(PREFIX_ASSIGNMENT);
        if (assignmentValue.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MarkAssignmentCommand.MESSAGE_USAGE));
        }

        Assignment assignment = ParserUtil.parseAssignment(assignmentValue.get());

        return new MarkAssignmentCommand(index, assignment);
    }

}
