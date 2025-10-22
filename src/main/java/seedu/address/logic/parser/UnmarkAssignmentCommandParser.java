package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnmarkAssignmentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;

/**
 * Parses input arguments and creates a new {@link seedu.address.logic.commands.UnmarkAssignmentCommand}.
 */
public class UnmarkAssignmentCommandParser implements Parser<UnmarkAssignmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnmarkAssignmentCommand
     * and returns an UnmarkAssignmentCommand object for execution.
     *
     * The expected format is: INDEX a/ASSIGNMENT_NAME
     *
     * @param args full user input string (arguments portion)
     * @return an UnmarkAssignmentCommand containing the parsed index and assignment
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public UnmarkAssignmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = tokenizeArguments(args);

        Index index = parseIndexFromPreamble(argMultimap);
        Assignment assignment = parseAssignmentValue(argMultimap);

        return new UnmarkAssignmentCommand(index, assignment);
    }

    /**
     * Tokenizes the raw argument string using the assignment prefix.
     */
    private ArgumentMultimap tokenizeArguments(String args) {
        return ArgumentTokenizer.tokenize(args, PREFIX_ASSIGNMENT);
    }

    /**
     * Parses and returns the index from the preamble portion of the tokenized arguments.
     *
     * @throws ParseException if the index is invalid
     */
    private Index parseIndexFromPreamble(ArgumentMultimap argMultimap) throws ParseException {
        try {
            return ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UnmarkAssignmentCommand.MESSAGE_USAGE), pe);
        }
    }

    /**
     * Extracts, validates and parses the assignment value from the tokenized arguments.
     *
     * @throws ParseException if the assignment value is missing or invalid
     */
    private Assignment parseAssignmentValue(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> assignmentValue = argMultimap.getValue(PREFIX_ASSIGNMENT);
        if (assignmentValue.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UnmarkAssignmentCommand.MESSAGE_USAGE));
        }

        return ParserUtil.parseAssignment(assignmentValue.get());
    }
}
