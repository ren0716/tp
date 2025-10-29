package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import seedu.address.logic.commands.UnassignAllCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;

/**
 * Parses input arguments and creates a new UnassignAllCommand object.
 */
public class UnassignAllCommandParser implements Parser<UnassignAllCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnassignAllCommand
     * and returns an UnassignAllCommand object for execution.
     *
     * @param args The user input arguments.
     * @return An UnassignAllCommand object with the parsed class group name and assignment.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public UnassignAllCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT);

        if (!arePrefixesPresent(argMultimap, PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UnassignAllCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT);

        String classGroupName = argMultimap.getValue(PREFIX_CLASSGROUP).get().trim().toLowerCase();
        String assignmentName = argMultimap.getValue(PREFIX_ASSIGNMENT).get().trim().toLowerCase();

        if (classGroupName.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UnassignAllCommand.MESSAGE_USAGE));
        }

        if (assignmentName.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UnassignAllCommand.MESSAGE_USAGE));
        }

        Assignment assignment = ParserUtil.parseAssignment(assignmentName, classGroupName);

        return new UnassignAllCommand(classGroupName, assignment);
    }

}
