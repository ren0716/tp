package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_INDEX_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_INDEX_RANGE;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddAssignmentCommand;
import seedu.address.logic.commands.AddAssignmentCommand.AddAssignmentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ASSIGNMENT, PREFIX_CLASSGROUP);

        Index index = parseIndexFromPreamble(argMultimap.getPreamble(), AddAssignmentCommand.MESSAGE_USAGE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSGROUP);

        String classGroupName = ParserUtil.parseClassGroupName(argMultimap, AddAssignmentCommand.MESSAGE_USAGE);

        AddAssignmentDescriptor addAssignmentDescriptor = new AddAssignmentDescriptor();
        parseAssignmentsForEdit(argMultimap.getAllValues(PREFIX_ASSIGNMENT), classGroupName)
                .ifPresent(addAssignmentDescriptor::setAssignments);

        return new AddAssignmentCommand(index, addAssignmentDescriptor);
    }

    /**
     * Helper to parse and validate the preamble as an index.
     */
    private Index parseIndexFromPreamble(String preamble, String usageMessage) throws ParseException {
        if (preamble == null || preamble.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, usageMessage));
        }
        String trimmed = preamble.trim();
        if (trimmed.split("\\s+").length > 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, usageMessage));
        }
        try {
            return ParserUtil.parseIndex(trimmed);
        } catch (ParseException pe) {
            String msg = pe.getMessage();
            if (MESSAGE_INVALID_PERSON_DISPLAYED_INDEX.equals(msg)
                    || MESSAGE_INVALID_INDEX_FORMAT.equals(msg)
                    || MESSAGE_INVALID_INDEX_RANGE.equals(msg)) {
                throw pe;
            }
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, usageMessage), pe);
        }
    }

    /**
     * Parses {@code Collection<String> assignments} into a {@code Set<Assignment>} if {@code assignments} is non-empty.
     * If {@code assignments} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Assignment>} containing zero assignments.
     */
    private Optional<Set<Assignment>> parseAssignmentsForEdit(Collection<String> assignments, String classGroupName)
            throws ParseException {
        assert assignments != null;
        if (assignments.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> assignmentSet = assignments.size() == 1 && assignments.contains(
                "") ? Collections.emptySet() : assignments;
        return Optional.of(ParserUtil.parseAssignments(assignmentSet, classGroupName));
    }
}
