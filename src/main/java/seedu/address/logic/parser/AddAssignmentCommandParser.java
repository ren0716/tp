package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_ASSIGNMENT_NOT_ADDED;
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

        Index index;
        // 1) Missing index: MESSAGE_INVALID_COMMAND_FORMAT
        if (argMultimap.getPreamble() == null || argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAssignmentCommand.MESSAGE_USAGE));
        }

        // 2) Preamble contains extra tokens (e.g. "1 extra"): MESSAGE_INVALID_COMMAND_FORMAT
        String preamble = argMultimap.getPreamble().trim();
        if (preamble.split("\\s+").length > 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAssignmentCommand.MESSAGE_USAGE));
        }

        // 3) Parse index: ParserUtil.parseIndex throws Invalid INDEX / command specific error
        try {
            index = ParserUtil.parseIndex(preamble);
        } catch (ParseException pe) {
            String msg = pe.getMessage();
            if (MESSAGE_INVALID_PERSON_DISPLAYED_INDEX.equals(msg)
                    || MESSAGE_INVALID_INDEX_FORMAT.equals(msg)
                    || MESSAGE_INVALID_INDEX_RANGE.equals(msg)) {
                // index-specific errors
                throw pe;
            }
            // command specific parse errors
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAssignmentCommand.MESSAGE_USAGE), pe);
        }

        // 4) Duplicate prefixes detection
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSGROUP);

        // 5) Check class group presence and non-empty
        if (!argMultimap.getValue(PREFIX_CLASSGROUP).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAssignmentCommand.MESSAGE_USAGE));
        }
        String classGroupName = argMultimap.getValue(PREFIX_CLASSGROUP).get().trim().toLowerCase();
        if (classGroupName.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAssignmentCommand.MESSAGE_USAGE));
        }

        // 6) Parse assignments: assignment-specific MESSAGE_CONSTRAINTS on invalid values
        AddAssignmentDescriptor addAssignmentDescriptor = new AddAssignmentDescriptor();
        parseAssignmentsForEdit(argMultimap.getAllValues(PREFIX_ASSIGNMENT), classGroupName)
                .ifPresent(addAssignmentDescriptor::setAssignments);

        // 7) No assignments provided: MESSAGE_ASSIGNMENT_NOT_ADDED
        if (!addAssignmentDescriptor.isAssignmentAdded()) {
            throw new ParseException(MESSAGE_ASSIGNMENT_NOT_ADDED);
        }

        return new AddAssignmentCommand(index, addAssignmentDescriptor);
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
