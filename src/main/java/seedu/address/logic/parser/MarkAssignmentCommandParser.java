package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import java.util.List;

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
     * The expected format is either:
     * - Single index: INDEX c/CLASS_GROUP a/ASSIGNMENT_NAME
     * - Index range: START-END c/CLASS_GROUP a/ASSIGNMENT_NAME
     *
     * @param args full user input string (arguments portion)
     * @return a MarkAssignmentCommand containing the parsed indices and assignment
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public MarkAssignmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = tokenizeArguments(args);

        if (!arePrefixesPresent(argMultimap, PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT)
                || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkAssignmentCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT);

        // Parse index or index range
        List<Index> indices = ParserUtil.parseMultipleIndex(argMultimap.getPreamble());
        String classGroupName = ParserUtil.parseClassGroupName(argMultimap, MarkAssignmentCommand.MESSAGE_USAGE);
        Assignment assignment = ParserUtil.parseAssignmentValue(argMultimap, classGroupName,
                MarkAssignmentCommand.MESSAGE_USAGE);

        return new MarkAssignmentCommand(indices, assignment);
    }

    /**
     * Tokenizes the raw argument string using the assignment and classgroup prefixes.
     */
    private ArgumentMultimap tokenizeArguments(String args) {
        return ArgumentTokenizer.tokenize(args, PREFIX_ASSIGNMENT, PREFIX_CLASSGROUP);
    }

}
