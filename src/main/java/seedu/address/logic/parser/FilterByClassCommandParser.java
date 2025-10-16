package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FilterByClassGroupCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FilterByClassGroupCommandParser implements Parser<FilterByClassGroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterByClassGroupCommand parse(String args) throws ParseException {
        requireNonNull(args);
        
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args, PREFIX_CLASSGROUP);

        if (!arePrefixesPresent(argMultimap, REFIX_CLASSGROUP)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterByClassGroupCommand.MESSAGE_USAGE));
        }
        Set<String> classGroups = ParserUtil.parseClassGroups(argMultimap.getAllValues(PREFIX_CLASSGROUP));

        if (classGroups.size() != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterByClassGroupCommand.MESSAGE_USAGE));
        }

        Iterator<String> i = classGroups.iterator();
        String classGroup = i.next();
        return new FilterByClassGroupCommand(classGroup);
    }

}