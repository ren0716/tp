package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;

import java.util.List;
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
     * The expected format is: INDEX c/CLASS_GROUP a/ASSIGNMENT_NAME
     *
     * @param args full user input string (arguments portion)
     * @return an UnmarkAssignmentCommand containing the parsed index and assignment
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public UnmarkAssignmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = tokenizeArguments(args);

        // Parse index or index range
        List<Index> indices = ParserUtil.parseIndexSpecification(argMultimap.getPreamble());
        String classGroupName = ParserUtil.parseClassGroupName(argMultimap, UnmarkAssignmentCommand.MESSAGE_USAGE);
        Assignment assignment = ParserUtil.parseAssignmentValue(argMultimap, classGroupName,
                UnmarkAssignmentCommand.MESSAGE_USAGE);

        return new UnmarkAssignmentCommand(indices, assignment);
    }

    /**
     * Tokenizes the raw argument string using the assignment and classgroup prefixes.
     */
    private ArgumentMultimap tokenizeArguments(String args) {
        return ArgumentTokenizer.tokenize(args, PREFIX_ASSIGNMENT, PREFIX_CLASSGROUP);
    }
}
