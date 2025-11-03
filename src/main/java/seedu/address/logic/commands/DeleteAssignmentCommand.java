package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_ASSIGNMENT_NOT_DELETED;
import static seedu.address.logic.Messages.MESSAGE_ASSIGNMENT_NOT_EXIST;
import static seedu.address.logic.Messages.MESSAGE_CLASS_NOT_PROVIDED;
import static seedu.address.logic.Messages.MESSAGE_DELETE_ASSIGNMENT_SUCCESS;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_PERSON;
import static seedu.address.logic.Messages.MESSAGE_STUDENT_NOT_IN_CLASS_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.classgroup.ClassGroup;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Deletes assignment(s) from an existing person in the address book.
 */
public class DeleteAssignmentCommand extends Command {

    public static final String COMMAND_WORD = "unassign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Delete assignment(s) from the student identified "
            + "by the index number used in the displayed student list. \n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_CLASSGROUP + "CLASS "
            + PREFIX_ASSIGNMENT + "ASSIGNMENT "
            + "[" + PREFIX_ASSIGNMENT + "ASSIGNMENT]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CLASSGROUP + "Math 3PM "
            + PREFIX_ASSIGNMENT + "ScienceTopic2 "
            + PREFIX_ASSIGNMENT + "MathHW1";

    private final Index index;
    private final DeleteAssignmentDescriptor deleteAssignmentDescriptor;

    /**
     * @param index of the person in the filtered person list to add assignment to
     * @param deleteAssignmentDescriptor details to add assignment to the person with
     */
    public DeleteAssignmentCommand(Index index, DeleteAssignmentDescriptor deleteAssignmentDescriptor) {
        requireNonNull(index);
        requireNonNull(deleteAssignmentDescriptor);

        this.index = index;
        this.deleteAssignmentDescriptor = new DeleteAssignmentDescriptor(deleteAssignmentDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        // No class provided (missing prefix / present but empty):
        if (!deleteAssignmentDescriptor.hasClassGroup()) {
            throw new CommandException(MESSAGE_CLASS_NOT_PROVIDED);
        }

        // No assignments provided (missing prefix / present but empty): MESSAGE_ASSIGNMENT_NOT_DELETED
        if (!deleteAssignmentDescriptor.isAssignmentDeleted()
                || (deleteAssignmentDescriptor.getAssignments().isPresent()
                && deleteAssignmentDescriptor.getAssignments().get().isEmpty())) {
            throw new CommandException(MESSAGE_ASSIGNMENT_NOT_DELETED);
        }

        // Validate that the student belongs to the specified class group(s)
        validateStudentClassGroups(personToEdit, deleteAssignmentDescriptor);

        Person editedPerson = createEditedPerson(personToEdit, deleteAssignmentDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_DELETE_ASSIGNMENT_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Validates that the student belongs to all class groups specified in the assignments.
     *
     * @param person The person to validate
     * @param desc The descriptor containing the assignments to add
     * @throws CommandException if the student does not belong to any of the specified class groups
     */
    private static void validateStudentClassGroups(Person person, DeleteAssignmentDescriptor desc)
            throws CommandException {
        Set<Assignment> newAssignments = desc.getAssignments().orElse(Set.of());
        Set<String> personClassGroupNames = person.getClassGroups().stream()
                .map(ClassGroup::getClassGroupName)
                .collect(Collectors.toSet());

        for (Assignment assignment : newAssignments) {
            if (!personClassGroupNames.contains(assignment.classGroupName)) {
                throw new CommandException(String.format(MESSAGE_STUDENT_NOT_IN_CLASS_GROUP,
                        assignment.classGroupName));
            }
        }
    }

    /**
     * Returns the intersection of the person's current assignments and those requested for deletion.
     */
    private static Set<Assignment> findDeletableAssignments(Person person, DeleteAssignmentDescriptor desc) {
        Set<Assignment> newAssignments = desc.getAssignments().orElse(Set.of());
        return person.getAssignments().stream()
                .filter(newAssignments::contains)
                .collect(Collectors.toSet());
    }

    /**
     * Returns assignments requested for deletion that do not exist on the person.
     */
    private static Set<Assignment> findNonExistentAssignments(Person person, DeleteAssignmentDescriptor desc) {
        Set<Assignment> requested = desc.getAssignments().orElse(Set.of());
        Set<Assignment> existing = person.getAssignments();
        return requested.stream()
                .filter(a -> !existing.contains(a))
                .collect(Collectors.toSet());
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code deleteAssignmentDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, DeleteAssignmentDescriptor deleteAssignmentDescriptor)
            throws CommandException {

        assert personToEdit != null;

        Name updatedName = personToEdit.getName();
        Phone updatedPhone = personToEdit.getPhone();
        Level updatedLevel = personToEdit.getLevel();
        Set<ClassGroup> updatedClassGroup = personToEdit.getClassGroups();
        Set<Assignment> allAssignments = new HashSet<>(personToEdit.getAssignments());
        Set<Assignment> toDelete = findDeletableAssignments(personToEdit, deleteAssignmentDescriptor);
        Set<Assignment> nonExistent = findNonExistentAssignments(personToEdit, deleteAssignmentDescriptor);

        if (!nonExistent.isEmpty()) {
            String missingNames = nonExistent.stream()
                    .map(Assignment::toString)
                    .sorted()
                    .collect(Collectors.joining(", "));
            throw new CommandException(String.format(MESSAGE_ASSIGNMENT_NOT_EXIST, missingNames));
        }

        Set<Assignment> updatedAssignments = new HashSet<>(allAssignments);
        updatedAssignments.removeAll(toDelete);

        return new Person(updatedName, updatedPhone, updatedLevel, updatedClassGroup, updatedAssignments);
    }

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteAssignmentCommand)) {
            return false;
        }

        DeleteAssignmentCommand otherDeleteAssignmentCommand = (DeleteAssignmentCommand) other;
        return index.equals(otherDeleteAssignmentCommand.index)
                && deleteAssignmentDescriptor.equals(otherDeleteAssignmentCommand.deleteAssignmentDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("deleteAssignmentDescriptor", deleteAssignmentDescriptor)
                .toString();
    }

    /**
     * Stores the details of assignment(s) to delete from the person. All other fields will remain unchanged.
     */
    public static class DeleteAssignmentDescriptor {
        private Set<Assignment> assignments;
        private String classGroupName;

        public DeleteAssignmentDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code assignments} is used internally.
         */
        public DeleteAssignmentDescriptor(DeleteAssignmentDescriptor toCopy) {
            setAssignments(toCopy.assignments);
            setClassGroupName(toCopy.classGroupName);
        }

        /**
         * Returns true if at least one assignment is set to be deleted.
         */
        public boolean isAssignmentDeleted() {
            return CollectionUtil.isAnyNonNull(assignments);
        }

        /**
         * Returns true if a class group was provided (non-null and non-empty after trimming).
         */
        public boolean hasClassGroup() {
            return classGroupName != null && !classGroupName.trim().isEmpty();
        }

        public void setClassGroupName(String classGroupName) {
            this.classGroupName = classGroupName;
        }

        public Optional<String> getClassGroupName() {
            return (classGroupName != null) ? Optional.of(classGroupName) : Optional.empty();
        }

        /**
         * Sets {@code assignments} to this object's {@code assignments}.
         * A defensive copy of {@code assignments} is used internally.
         */
        public void setAssignments(Set<Assignment> assignments) {
            this.assignments = (assignments != null) ? new HashSet<>(assignments) : null;
        }

        /**
         * Returns an unmodifiable assignment set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code assignments} is null.
         */
        public Optional<Set<Assignment>> getAssignments() {
            return (assignments != null) ? Optional.of(Collections.unmodifiableSet(assignments)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof DeleteAssignmentDescriptor)) {
                return false;
            }

            DeleteAssignmentDescriptor otherDeleteAssignmentDescriptor = (DeleteAssignmentDescriptor) other;
            return Objects.equals(assignments, otherDeleteAssignmentDescriptor.assignments)
                    && Objects.equals(classGroupName, otherDeleteAssignmentDescriptor.classGroupName);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("assignments", assignments)
                    .toString();
        }
    }
}
