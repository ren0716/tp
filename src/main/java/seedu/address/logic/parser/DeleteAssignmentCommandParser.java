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
import seedu.address.logic.commands.DeleteAssignmentCommand;
import seedu.address.logic.commands.DeleteAssignmentCommand.DeleteAssignmentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;

/**
 * Parses input arguments and creates a new DeleteAssignmentCommand object
 */
@SuppressWarnings("checkstyle:Regexp")
public class DeleteAssignmentCommandParser implements Parser<DeleteAssignmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteAssignmentCommand
     * and returns an DeleteAssignmentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @SuppressWarnings("checkstyle:Regexp")
    public DeleteAssignmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ASSIGNMENT, PREFIX_CLASSGROUP);

        Index index = parseIndexFromPreamble(argMultimap.getPreamble(), DeleteAssignmentCommand.MESSAGE_USAGE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSGROUP); // 4) Duplicate prefixes detection
        if (!argMultimap.getValue(PREFIX_CLASSGROUP).isPresent()) { // 5) Check class group presence and non-empty
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, DeleteAssignmentCommand.MESSAGE_USAGE));
        }
        String classGroupName = argMultimap.getValue(PREFIX_CLASSGROUP).get().trim().toLowerCase();
        if (classGroupName.isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, DeleteAssignmentCommand.MESSAGE_USAGE));
        }

        // 6) Parse assignments: assignment-specific MESSAGE_CONSTRAINTS on invalid values
        DeleteAssignmentDescriptor deleteAssignmentDescriptor = new DeleteAssignmentDescriptor();
        parseAssignmentsForEdit(argMultimap.getAllValues(PREFIX_ASSIGNMENT), classGroupName)
                .ifPresent(deleteAssignmentDescriptor::setAssignments);

        return new DeleteAssignmentCommand(index, deleteAssignmentDescriptor);
    }

    /**
     * Parses and validates the preamble as a single index.
     *
     * <p>Expected behavior:
     * - Ensures the preamble is present and contains exactly one token.
     * - Delegates numeric validation to {@link ParserUtil#parseIndex(String)} which
     *   will throw index-specific {@link ParseException} messages:
     *   {@code MESSAGE_INVALID_PERSON_DISPLAYED_INDEX}, {@code MESSAGE_INVALID_INDEX_FORMAT},
     *   or {@code MESSAGE_INVALID_INDEX_RANGE}.
     * - Any other {@link ParseException} thrown by the delegate is wrapped and rethrown
     *   as a generic invalid command format error using {@code usageMessage}, so that
     *   command-specific usage is shown to the user.
     *
     * @param preamble the raw preamble string extracted from the user's input (may be null/blank)
     * @param usageMessage the command usage message to include when reporting generic format errors
     * @return the parsed {@link Index} corresponding to the preamble
     * @throws ParseException if the preamble is missing, contains extra tokens, or the index is invalid
     */
    private Index parseIndexFromPreamble(String preamble, String usageMessage) throws ParseException {
        // 1) Missing preamble or empty: MESSAGE_INVALID_COMMAND_FORMAT
        if (preamble == null || preamble.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, usageMessage));
        }

        String trimmed = preamble.trim();
        if (trimmed.split("\\s+").length > 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, usageMessage));
        }

        // 2) Index parsing and validation delegated to ParserUtil
        try {
            return ParserUtil.parseIndex(trimmed);
        } catch (ParseException pe) {
            String msg = pe.getMessage();
            if (MESSAGE_INVALID_PERSON_DISPLAYED_INDEX.equals(msg)
                    || MESSAGE_INVALID_INDEX_FORMAT.equals(msg)
                    || MESSAGE_INVALID_INDEX_RANGE.equals(msg)) {
                // Throw index-related errors
                throw pe;
            }
            // Any other parse exceptions: MESSAGE_INVALID_COMMAND_FORMAT
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
        Collection<String> assignmentSet =
                assignments.size() == 1 && assignments.contains("") ? Collections.emptySet() : assignments;
        return Optional.of(ParserUtil.parseAssignments(assignmentSet, classGroupName));
    }

}
