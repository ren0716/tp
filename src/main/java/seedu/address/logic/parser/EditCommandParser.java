package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
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
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.classgroup.ClassGroup;


/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args, PREFIX_NAME, PREFIX_PHONE, PREFIX_LEVEL, PREFIX_CLASSGROUP, PREFIX_ASSIGNMENT);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_LEVEL);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_LEVEL).isPresent()) {
            editPersonDescriptor.setLevel(ParserUtil.parseLevel(argMultimap.getValue(PREFIX_LEVEL).get()));
        }

        parseClassGroupsForEdit(argMultimap.getAllValues(PREFIX_CLASSGROUP))
                .ifPresent(editPersonDescriptor::setClassGroups);

        parseAssignmentsForEdit(argMultimap.getAllValues(PREFIX_ASSIGNMENT))
                .ifPresent(editPersonDescriptor::setAssignments);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> classGroups} into a {@code Set<ClassGroup>} if {@code classGroups} is non-empty.
     * If {@code classGroup} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<ClassGroup>} containing zero class groups.
     * 
     * Note: This method is disabled as editing class groups could create inconsistencies with existing assignments.
     * Assignments contain a classGroupName field, and removing a class group while assignments reference it
     * would create an invalid state. Use dedicated commands to manage class groups and assignments together.
     */
    private Optional<Set<ClassGroup>> parseClassGroupsForEdit(Collection<String> classGroups) throws ParseException {
        assert classGroups != null;

        if (classGroups.isEmpty()) {
            return Optional.empty();
        }
        // Class groups can no longer be edited via the edit command to prevent inconsistencies
        // with assignments that reference class group names
        throw new ParseException("Class groups cannot be edited via the edit command to maintain consistency "
                + "with assignments. Use dedicated commands to manage class groups and assignments.");
    }

    /**
     * Parses {@code Collection<String> assignments} into a {@code Set<Assignment>} if {@code assignments} is non-empty.
     * If {@code assignments} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Assignment>} containing zero assignments.
     *
     * Note: This method is disabled as assignments now require a class group.
     * Use the assign command to add assignments to a person.
     */
    private Optional<Set<Assignment>> parseAssignmentsForEdit(Collection<String> assignments) throws ParseException {
        assert assignments != null;

        if (assignments.isEmpty()) {
            return Optional.empty();
        }
        // Assignments can no longer be edited via the edit command since they require a class group
        // Use assign/unassign commands instead
        throw new ParseException("Assignments cannot be edited via the edit command. "
                + "Use 'assign' command to add assignments with a class group.");
    }

}
