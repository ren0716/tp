package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_CLASS_NOT_DELETED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteClassCommand;
import seedu.address.logic.commands.DeleteClassCommand.DeleteClassDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteClassCommand object
 */
public class DeleteClassCommandParser implements Parser<DeleteClassCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteClassCommand
     * and returns a DeleteClassCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public DeleteClassCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        Index index = ParserUtil.parseOneIndex(argMultimap.getPreamble(), DeleteClassCommand.MESSAGE_USAGE);

        argMultimap.verifyNoInvalidPrefixesFor(PREFIX_LEVEL, PREFIX_ASSIGNMENT, PREFIX_PHONE, PREFIX_NAME);

        DeleteClassDescriptor deleteClassDescriptor = new DeleteClassDescriptor();

        ParserUtil.parseOptionalClassGroups(argMultimap.getAllValues(PREFIX_CLASSGROUP))
                .ifPresent(deleteClassDescriptor::setClassGroups);

        if (!deleteClassDescriptor.isClassDeleted()) {
            throw new ParseException(MESSAGE_CLASS_NOT_DELETED);
        }

        return new DeleteClassCommand(index, deleteClassDescriptor);
    }
}
