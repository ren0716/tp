package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteClassCommand;
import seedu.address.logic.commands.DeleteClassCommand.DeleteClassDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.classgroup.ClassGroup;

/**
 * Parses input arguments and creates a new DeleteClassCommand object
 */
public class DeleteClassCommandParser implements Parser<DeleteClassCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteClassCommand
     * and returns a DeleteClassCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public DeleteClassCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CLASSGROUP);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteClassCommand.MESSAGE_USAGE), pe);
        }

        DeleteClassDescriptor deleteClassDescriptor = new DeleteClassDescriptor();

        parseClassGroupsForEdit(argMultimap.getAllValues(PREFIX_CLASSGROUP))
                .ifPresent(deleteClassDescriptor::setClassGroups);

        if (!deleteClassDescriptor.isClassDeleted()) {
            throw new ParseException(DeleteClassCommand.MESSAGE_CLASS_NOT_PROVIDED);
        }

        return new DeleteClassCommand(index, deleteClassDescriptor);
    }

    /**
     * Parses {@code Collection<String> classGroups} into a {@code Set<ClassGroup>} if non-empty.
     * If it contains only one element which is an empty string, it will be parsed into an empty set.
     */
    private Optional<Set<ClassGroup>> parseClassGroupsForEdit(Collection<String> classGroups) throws ParseException {
        assert classGroups != null;

        if (classGroups.isEmpty()) {
            return Optional.empty();
        }

        Collection<String> classSet =
                classGroups.size() == 1 && classGroups.contains("") ? Collections.emptySet() : classGroups;
        return Optional.of(ParserUtil.parseClassGroups(classSet));
    }
}
