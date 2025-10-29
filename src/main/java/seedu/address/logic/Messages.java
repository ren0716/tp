package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    // general messages
    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d students listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_INVALID_FIELDS =
            "Following field(s) should not be part of command: ";
    public static final String MESSAGE_DUPLICATE_PERSON = "This student already exists in the student list!\n"
            + "(NOTE: NAME is case-insensitive)";

    // add & delete student
    public static final String MESSAGE_ADD_SUCCESS = "New student added: %1$s";
    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted student: %1$s";
    public static final String MESSAGE_NAME_ALREADY_EXISTS = "Warning: A student with the name '%1$s' already exists\n"
            + "(NOTE: NAME is case-insensitive)";
    public static final String MESSAGE_PHONE_ALREADY_EXISTS = "Warning: A student with the phone number "
            + "'%1$s' already exists\n";

    // edit
    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited student: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided";

    // assign & unassign
    public static final String MESSAGE_CLASS_NOT_PROVIDED = "At least one class must be provided";
    public static final String MESSAGE_ASSIGN_SUCCESS = "Added assignment(s) to: %1$s";
    public static final String MESSAGE_ASSIGNMENT_NOT_ADDED = "At least one assignment to add must be provided";
    public static final String MESSAGE_DUPLICATE_ASSIGNMENT = "Duplicate assignment(s): %s\n"
            + "(NOTE: ASSIGNMENT is case-insensitive)";
    public static final String MESSAGE_STUDENT_NOT_IN_CLASS_GROUP = "Student does not belong to the class: %s";
    public static final String MESSAGE_DELETE_ASSIGNMENT_SUCCESS = "Deleted assignment(s) from: %1$s";
    public static final String MESSAGE_ASSIGNMENT_NOT_DELETED = "At least one assignment to delete must be provided";
    public static final String MESSAGE_ASSIGNMENT_NOT_EXIST = "Cannot delete non-existent assignment(s): %s";

    // assignall & unassignall
    public static final String MESSAGE_ASSIGNALL_SUCCESS =
            "Assigned assignment '%1$s' to %2$d student(s) in class '%3$s'";
    public static final String MESSAGE_CLASS_NOT_EXIST = "Non-existent class: %1$s";
    public static final String MESSAGE_ALREADY_ASSIGNED =
            "All students in class '%1$s' already have the assignment '%2$s' assigned\n"
                    + "(NOTE: ASSIGNMENT is case-insensitive)";
    public static final String MESSAGE_UNASSIGNALL_SUCCESS =
            "Unassigned assignment '%1$s' from %2$d student(s) in class '%3$s'";
    public static final String MESSAGE_ASSIGNMENT_NOT_FOUND = "Assignment '%1$s' not found in class '%2$s'";

    // add & delete class
    public static final String MESSAGE_ADD_CLASS_SUCCESS = "Added class(es) to: %1$s";
    public static final String MESSAGE_CLASSES_NOT_ADDED = "At least one class to add must be provided";
    public static final String MESSAGE_DUPLICATE_CLASSES = "Duplicate class(es): %s\n"
            + "(NOTE: CLASS is case-insensitive)";
    public static final String MESSAGE_DELETE_CLASS_SUCCESS = "Deleted class(es) from: %1$s";
    public static final String MESSAGE_CLASS_NOT_DELETED = "At least one class to delete must be provided";
    public static final String MESSAGE_CLASS_NOT_FOUND = "Cannot delete non-existent class(es): %s";

    // mark & unmark
    public static final String MESSAGE_INVALID_ASSIGNMENT_IN_PERSON = "Assignment '%1$s' not found for some student(s)";
    public static final String MESSAGE_MARK_PERSON_SUCCESS = "Marked assignment %1$s of %2$s";
    public static final String ALREADY_MARKED = "Assignment is already marked!";
    public static final String MESSAGE_UNMARK_PERSON_SUCCESS = "Unmarked assignment %1$s of %2$s";
    public static final String ALREADY_UNMARKED = "Assignment is already unmarked!";

    // parserutil
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The student index provided is invalid";
    public static final String MESSAGE_INVALID_INDEX_RANGE = "Invalid index range format!\n"
            + "Expected format: START-END where START and END are positive integers "
            + "and START <= END";
    public static final String MESSAGE_INVALID_INDEX_FORMAT = "Invalid index format!\n"
            + "Use space-separated indices or ranges\n"
            + "Examples: '1 2 3' or '1-3' or '1 2-4 6'";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForInvalidPrefixes(Prefix... InvalidPrefixes) {
        assert InvalidPrefixes.length > 0;

        Set<String> invalidFields =
                Stream.of(InvalidPrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_INVALID_FIELDS + String.join(" ", invalidFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(StringUtil.toTitleCase(person.getName().fullName))
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Level: ")
                .append(person.getLevel())
                .append("; Classes: ");
        // join ClassGroups with ", " between entries
        String classGroups = person.getClassGroups().stream()
                .map(cg -> StringUtil.toTitleCase(cg.classGroupName))
                .collect(Collectors.joining(", "));
        builder.append(classGroups);

        // Only show assignments if the person has any
        if (!person.getAssignments().isEmpty()) {
            builder.append("; Assignments: ");
            // join Assignments with ", "
            String assignments = person.getAssignments().stream()
                    .map(assignment -> StringUtil.toTitleCase(assignment.toString()))
                    .collect(Collectors.joining(", "));
            builder.append(assignments);
        }

        return builder.toString();
    }

}
