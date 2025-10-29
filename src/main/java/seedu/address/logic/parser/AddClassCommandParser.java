package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_CLASSES_NOT_ADDED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddClassCommand;
import seedu.address.logic.commands.AddClassCommand.AddClassDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.classgroup.ClassGroup;

/**
 * Parses input arguments and creates a new AddClassCommand object
 */
public class AddClassCommandParser implements Parser<AddClassCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddClassCommand
     * and returns an AddClassCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public AddClassCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CLASSGROUP);

        Index index = ParserUtil.parseIndexFromPreamble(argMultimap.getPreamble(), AddClassCommand.MESSAGE_USAGE);

        AddClassDescriptor addClassDescriptor = new AddClassDescriptor();

        parseClassesForEdit(argMultimap.getAllValues(PREFIX_CLASSGROUP))
                .ifPresent(addClassDescriptor::setClassGroups);

        if (!addClassDescriptor.hasClasses()) {
            throw new ParseException(MESSAGE_CLASSES_NOT_ADDED);
        }

        return new AddClassCommand(index, addClassDescriptor);
    }

    /**
     * Parses {@code Collection<String> classes} into a {@code Set<ClassGroup>} if non-empty.
     * If {@code classes} contains only one element which is an empty string, returns an empty set.
     */
    private Optional<Set<ClassGroup>> parseClassesForEdit(Collection<String> classes) throws ParseException {
        assert classes != null;

        if (classes.isEmpty()) {
            return Optional.empty();
        }

        Collection<String> classSet =
                classes.size() == 1 && classes.contains("") ? Collections.emptySet() : classes;

        return Optional.of(ParserUtil.parseClassGroups(classSet));
    }
}
