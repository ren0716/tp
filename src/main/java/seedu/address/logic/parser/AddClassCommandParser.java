package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddClassCommand;
import seedu.address.logic.commands.AddClassCommand.AddClassDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

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

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClassCommand.MESSAGE_USAGE), pe);
        }

        AddClassDescriptor addClassDescriptor = new AddClassDescriptor();

        parseClasses(argMultimap.getAllValues(PREFIX_CLASSGROUP))
                .ifPresent(addClassDescriptor::setClassGroups);

        if (!addClassDescriptor.hasClasses()) {
            throw new ParseException(AddClassCommand.MESSAGE_CLASSES_NOT_ADDED);
        }

        return new AddClassCommand(index, addClassDescriptor);
    }

    /**
     * Parses {@code Collection<String> classes} into a {@code Set<String>} if non-empty.
     * If {@code classes} contains only one element which is an empty string, returns an empty set.
     */
    private Optional<Set<String>> parseClasses(Collection<String> classes) {
        assert classes != null;

        if (classes.isEmpty()) {
            return Optional.empty();
        }

        Collection<String> classSet =
                classes.size() == 1 && classes.contains("") ? Collections.emptySet() : classes;

        // Optional: Validate or clean class names here if needed
        Set<String> cleanedClasses = classSet.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        return Optional.of(cleanedClasses);
    }
}