package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_ASSIGNMENT_NOT_ADDED;
import static seedu.address.logic.Messages.MESSAGE_CLASS_NOT_PROVIDED;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        // keep preamble check as invalid format
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignAllCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT);
        argMultimap.verifyNoInvalidPrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_LEVEL);

        Optional<Prefix> missingOrEmpty = firstMissingOrEmptyPrefix(argMultimap, PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT);
        if (missingOrEmpty.isPresent()) { // There is a missing or empty prefix
            Prefix p = missingOrEmpty.get();
            if (p.equals(PREFIX_CLASSGROUP)) {
                throw new ParseException(MESSAGE_CLASS_NOT_PROVIDED);
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
     * Returns the first {@code Prefix} that is either missing from the provided
     * {@code ArgumentMultimap} or present with an empty (trimmed) value.
     *
     * <p>Prefixes are checked in the order they are supplied. A prefix is considered
     * "missing" when {@code argumentMultimap.getValue(prefix).isEmpty()}. A prefix is
     * considered "empty" when it is present but its trimmed value is an empty string.
     * The first prefix meeting either condition is returned wrapped in an {@link Optional}.
     * If all prefixes are present with non-empty trimmed values, {@link Optional#empty()}
     * is returned.
     *
     * @param argumentMultimap the tokenized arguments to inspect; must not be {@code null}
     * @param prefixes the prefixes to check, examined in order; must not be {@code null} and
     *                 must not contain {@code null} elements
     * @return an {@code Optional} containing the first missing or empty {@code Prefix},
     *         or {@code Optional.empty()} if all prefixes are present with non-empty trimmed values
     * @throws NullPointerException if {@code argumentMultimap}, {@code prefixes}, or any element
     *         of {@code prefixes} is {@code null}
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
