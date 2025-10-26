package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.classgroup.ClassGroup;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String level} into an {@code level}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code level} is invalid.
     */
    public static Level parseLevel(String level) throws ParseException {
        requireNonNull(level);
        String trimmedLevel = level.trim();
        if (!Level.isValidLevel(trimmedLevel)) {
            throw new ParseException(Level.MESSAGE_CONSTRAINTS);
        }
        return new Level(trimmedLevel);
    }

    /**
     * Parses a {@code String classGroup} into a {@code ClassGroup}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code assignment} is invalid.
     */
    public static ClassGroup parseClassGroup(String classGroup) throws ParseException {
        requireNonNull(classGroup);
        String trimmedClassGroup = classGroup.trim().toLowerCase();
        if (!ClassGroup.isValidClassGroupName(trimmedClassGroup)) {
            throw new ParseException(ClassGroup.MESSAGE_CONSTRAINTS);
        }
        return new ClassGroup(trimmedClassGroup);
    }

    /**
     * Parses {@code Collection<String> assignments} into a {@code Set<ClassGroup>}.
     */
    public static Set<ClassGroup> parseClassGroups(Collection<String> classGroups) throws ParseException {
        requireNonNull(classGroups);
        final Set<ClassGroup> classGroupSet = new HashSet<>();
        for (String classGroupName : classGroups) {
            classGroupSet.add(parseClassGroup(classGroupName));
        }
        return classGroupSet;
    }

    /**
     * Parses a {@code String assignment} into a {@code Assignment}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code assignment} is invalid.
     */
    public static Assignment parseAssignment(String assignment, String classGroupName) throws ParseException {
        requireNonNull(assignment);
        requireNonNull(classGroupName);
        String trimmedAssignment = assignment.trim().toLowerCase();
        String trimmedClassGroupName = classGroupName.trim().toLowerCase();
        if (!Assignment.isValidAssignmentName(trimmedAssignment)) {
            throw new ParseException(Assignment.MESSAGE_CONSTRAINTS);
        }
        if (!Assignment.isValidClassGroupName(trimmedClassGroupName)) {
            throw new ParseException(Assignment.MESSAGE_CLASSGROUP_CONSTRAINTS);
        }
        return new Assignment(trimmedAssignment, trimmedClassGroupName);
    }

    /**
     * Parses {@code Collection<String> assignments} into a {@code Set<Assignments>}.
     * All assignments will be assigned to the specified class group.
     */
    public static Set<Assignment> parseAssignments(Collection<String> assignments, String classGroupName)
            throws ParseException {
        requireNonNull(assignments);
        requireNonNull(classGroupName);
        final Set<Assignment> assignmentSet = new HashSet<>();
        for (String assignmentName : assignments) {
            assignmentSet.add(parseAssignment(assignmentName, classGroupName));
        }
        return assignmentSet;
    }

    /**
     * Parses and returns the index from the preamble portion of the tokenized arguments.
     *
     * @throws ParseException if the index is invalid
     */
    public static Index parseIndexFromPreamble(ArgumentMultimap argMultimap, String messageUsage)
            throws ParseException {
        try {
            return ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    messageUsage), pe);
        }
    }

    /**
     * Extracts, validates and parses the classGroupName from the tokenized arguments.
     *
     * @throws ParseException if the classGroupName is missing or invalid
     */
    public static String parseClassGroupName(ArgumentMultimap argMultimap, String messageUsage)
            throws ParseException {

        // Check if class group is provided
        if (!argMultimap.getValue(PREFIX_CLASSGROUP).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    messageUsage));
        }

        String classGroupName = argMultimap.getValue(PREFIX_CLASSGROUP).get().trim().toLowerCase();
        if (classGroupName.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    messageUsage));
        }

        return classGroupName;
    }

    /**
     * Extracts, validates and parses the assignment value from the tokenized arguments.
     *
     * @throws ParseException if the assignment value is missing or invalid
     */
    public static Assignment parseAssignmentValue(ArgumentMultimap argMultimap, String classGroupName,
                                                  String messageUsage) throws ParseException {
        Optional<String> assignmentValue = argMultimap.getValue(PREFIX_ASSIGNMENT);
        if (assignmentValue.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    messageUsage));
        }

        return ParserUtil.parseAssignment(assignmentValue.get(), classGroupName);
    }
}
