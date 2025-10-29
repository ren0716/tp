package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

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
     * The expected format is either:
     * - Single index: INDEX c/CLASS_GROUP a/ASSIGNMENT_NAME
     * - Index range: START-END c/CLASS_GROUP a/ASSIGNMENT_NAME
     *
     * @param args full user input string (arguments portion)
     * @return an UnmarkAssignmentCommand containing the parsed indices and assignment
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public UnmarkAssignmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        if (!arePrefixesPresent(argMultimap, PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT)
                || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkAssignmentCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT);
        argMultimap.verifyNoInvalidPrefixesFor(PREFIX_NAME, PREFIX_LEVEL, PREFIX_PHONE);

        // Parse index or index range
        List<Index> indices = ParserUtil.parseIndexSpecification(argMultimap.getPreamble());
        String classGroupName = ParserUtil.parseClassGroupName(argMultimap, UnmarkAssignmentCommand.MESSAGE_USAGE);
        Assignment assignment = ParserUtil.parseAssignmentValue(argMultimap, classGroupName,
                UnmarkAssignmentCommand.MESSAGE_USAGE);

        return new UnmarkAssignmentCommand(indices, assignment);
    }
}
