package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteAssignmentCommand;
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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        Index index = ParserUtil.parseIndexFromPreamble(
                argMultimap.getPreamble(), DeleteAssignmentCommand.MESSAGE_USAGE);

        // allow missing / empty c/ to be represented in the descriptor
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSGROUP);
        argMultimap.verifyNoInvalidPrefixesFor(PREFIX_LEVEL, PREFIX_NAME, PREFIX_PHONE);

        DeleteAssignmentCommand.DeleteAssignmentDescriptor deleteAssignmentDescriptor =
                new DeleteAssignmentCommand.DeleteAssignmentDescriptor();

        // set classGroupName in descriptor (may be null if missing, may be empty if c/ provided with empty token)
        Optional<String> rawClassValueOpt = argMultimap.getValue(PREFIX_CLASSGROUP);
        String rawClassValue = rawClassValueOpt.orElse(null);
        deleteAssignmentDescriptor.setClassGroupName(rawClassValue);

        // Only parse Assignment objects if a non-empty class group name is provided.
        if (rawClassValue != null && !rawClassValue.trim().isEmpty()) {
            String classGroupName = rawClassValue.trim().toLowerCase();
            parseAssignmentsForEdit(argMultimap.getAllValues(PREFIX_ASSIGNMENT), classGroupName)
                    .ifPresent(deleteAssignmentDescriptor::setAssignments);
        }

        return new DeleteAssignmentCommand(index, deleteAssignmentDescriptor);
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
