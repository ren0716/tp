package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddAssignmentCommand;
import seedu.address.logic.commands.AddAssignmentCommand.AddAssignmentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddAssignmentCommand object
 */
public class AddAssignmentCommandParser implements Parser<AddAssignmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddAssignmentCommand
     * and returns an AddAssignmentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddAssignmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        Index index = ParserUtil.parseOneIndex(argMultimap.getPreamble(), AddAssignmentCommand.MESSAGE_USAGE);

        // allow missing / empty c/ to be represented in the descriptor (parser-side duplication check still useful)
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSGROUP);
        argMultimap.verifyNoInvalidPrefixesFor(PREFIX_NAME, PREFIX_LEVEL, PREFIX_PHONE);

        AddAssignmentDescriptor addAssignmentDescriptor = new AddAssignmentDescriptor();

        // set classGroupName in descriptor (may be null if missing, may be empty if c/ provided with empty token)
        Optional<String> rawClassValueOpt = argMultimap.getValue(PREFIX_CLASSGROUP);
        String rawClassValue = rawClassValueOpt.orElse(null);
        addAssignmentDescriptor.setClassGroupName(rawClassValue);

        // Only parse Assignment objects if a non-empty class group name is provided.
        if (rawClassValue != null && !rawClassValue.trim().isEmpty()) {
            String classGroupName = rawClassValue.trim().toLowerCase();
            ParserUtil.parseOptionalAssignments(argMultimap.getAllValues(PREFIX_ASSIGNMENT), classGroupName)
                    .ifPresent(addAssignmentDescriptor::setAssignments);
        }

        return new AddAssignmentCommand(index, addAssignmentDescriptor);
    }
}
