package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;

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

        if (!arePrefixesPresent(argMultimap, PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignAllCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT);

        String classGroupName = argMultimap.getValue(PREFIX_CLASSGROUP).get().trim();
        String assignmentName = argMultimap.getValue(PREFIX_ASSIGNMENT).get().trim();

        if (classGroupName.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignAllCommand.MESSAGE_USAGE));
        }

        if (assignmentName.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignAllCommand.MESSAGE_USAGE));
        }

        Assignment assignment = ParserUtil.parseAssignment(assignmentName);

        return new AssignAllCommand(classGroupName, assignment);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}

