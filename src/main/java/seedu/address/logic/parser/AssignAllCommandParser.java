package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_ASSIGNMENT_NOT_ADDED;
import static seedu.address.logic.Messages.MESSAGE_CLASSES_NOT_ADDED;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;

import java.util.Optional;
import java.util.stream.Stream;

import seedu.address.logic.commands.AssignAllCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;

/**
 * Parses input arguments and creates a new AssignAllCommand object
 */
public class AssignAllCommandParser implements Parser<AssignAllCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AssignAllCommand
     * and returns an AssignAllCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AssignAllCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT);

        // keep preamble check as invalid format
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignAllCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT);

        Optional<Prefix> missingOrEmpty = firstMissingOrEmptyPrefix(argMultimap, PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT);
        if (missingOrEmpty.isPresent()) {
            Prefix p = missingOrEmpty.get();
            if (p.equals(PREFIX_CLASSGROUP)) {
                throw new ParseException(MESSAGE_CLASSES_NOT_ADDED);
            } else {
                throw new ParseException(MESSAGE_ASSIGNMENT_NOT_ADDED);
            }
        }

        String classGroupName = argMultimap.getValue(PREFIX_CLASSGROUP).get().trim().toLowerCase();
        String assignmentName = argMultimap.getValue(PREFIX_ASSIGNMENT).get().trim().toLowerCase();

        Assignment assignment = ParserUtil.parseAssignment(assignmentName, classGroupName);

        return new AssignAllCommand(classGroupName, assignment);
    }

    /**
     * Returns the first prefix that is either missing or present with an empty (trimmed) value.
     * If all prefixes are present and non-empty, returns Optional.empty().
     */
    private static Optional<Prefix> firstMissingOrEmptyPrefix(ArgumentMultimap argumentMultimap,
                                                              Prefix... prefixes) {
        return Stream.of(prefixes)
                .filter(prefix -> {
                    Optional<String> val = argumentMultimap.getValue(prefix);
                    return val.isEmpty() || val.get().trim().isEmpty();
                })
                .findFirst();
    }
}
