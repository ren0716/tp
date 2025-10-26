package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;

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
     * The expected format is: INDEX c/CLASS_GROUP a/ASSIGNMENT_NAME
     *
     * @param args full user input string (arguments portion)
     * @return a MarkAssignmentCommand containing the parsed index and assignment
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public MarkAssignmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = tokenizeArguments(args);

        Index index = ParserUtil.parseIndexFromPreamble(argMultimap, MarkAssignmentCommand.MESSAGE_USAGE);
        String classGroupName = ParserUtil.parseClassGroupName(argMultimap, MarkAssignmentCommand.MESSAGE_USAGE);
        Assignment assignment = ParserUtil.parseAssignmentValue(argMultimap, classGroupName,
                MarkAssignmentCommand.MESSAGE_USAGE);

        return new MarkAssignmentCommand(index, assignment);
    }

    /**
     * Tokenizes the raw argument string using the assignment and classgroup prefixes.
     */
    private ArgumentMultimap tokenizeArguments(String args) {
        return ArgumentTokenizer.tokenize(args, PREFIX_ASSIGNMENT, PREFIX_CLASSGROUP);
    }
}
