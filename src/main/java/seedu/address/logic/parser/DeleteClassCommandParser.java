package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_CLASS_NOT_PROVIDED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CLASSGROUP);

        Index index = ParserUtil.parseIndexFromPreamble(argMultimap.getPreamble(), DeleteClassCommand.MESSAGE_USAGE);

        DeleteClassDescriptor deleteClassDescriptor = new DeleteClassDescriptor();

        ParserUtil.parseOptionalClassGroups(argMultimap.getAllValues(PREFIX_CLASSGROUP))
                .ifPresent(deleteClassDescriptor::setClassGroups);

        if (!deleteClassDescriptor.isClassDeleted()) {
            throw new ParseException(MESSAGE_CLASS_NOT_PROVIDED);
        }

        return new DeleteClassCommand(index, deleteClassDescriptor);
    }
}
