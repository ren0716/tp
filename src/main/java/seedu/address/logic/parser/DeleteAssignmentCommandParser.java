package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_ASSIGNMENT_NOT_DELETED;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
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
public class DeleteAssignmentCommandParser implements Parser<DeleteAssignmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteAssignmentCommand
     * and returns an DeleteAssignmentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteAssignmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args, PREFIX_ASSIGNMENT, PREFIX_CLASSGROUP);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteAssignmentCommand.MESSAGE_USAGE), pe);
        }

        // Check if class group is provided
        if (!argMultimap.getValue(PREFIX_CLASSGROUP).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteAssignmentCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSGROUP);
        String classGroupName = argMultimap.getValue(PREFIX_CLASSGROUP).get().trim().toLowerCase();

        if (classGroupName.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteAssignmentCommand.MESSAGE_USAGE));
        }

        DeleteAssignmentDescriptor deleteAssignmentDescriptor = new DeleteAssignmentDescriptor();

        parseAssignmentsForEdit(argMultimap.getAllValues(PREFIX_ASSIGNMENT), classGroupName)
                .ifPresent(deleteAssignmentDescriptor::setAssignments);

        if (!deleteAssignmentDescriptor.isAssignmentDeleted()) {
            throw new ParseException(MESSAGE_ASSIGNMENT_NOT_DELETED);
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
