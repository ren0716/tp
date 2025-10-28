package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddAssignmentCommand;
import seedu.address.logic.commands.AddAssignmentCommand.AddAssignmentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddAssignmentCommand object
 */
public class AddAssignmentCommandParser implements Parser<AddAssignmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddAssignmentCommand
     * and returns an AddAssignmentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddAssignmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ASSIGNMENT, PREFIX_CLASSGROUP);

        Index index = ParserUtil.parseIndexFromPreamble(argMultimap.getPreamble(), AddAssignmentCommand.MESSAGE_USAGE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSGROUP);

        String classGroupName = ParserUtil.parseClassGroupName(argMultimap, AddAssignmentCommand.MESSAGE_USAGE);

        AddAssignmentDescriptor addAssignmentDescriptor = new AddAssignmentDescriptor();
        ParserUtil.parseOptionalAssignments(argMultimap.getAllValues(PREFIX_ASSIGNMENT), classGroupName)
                .ifPresent(addAssignmentDescriptor::setAssignments);

        return new AddAssignmentCommand(index, addAssignmentDescriptor);
    }
}
