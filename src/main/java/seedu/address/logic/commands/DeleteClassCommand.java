package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_CLASS_NOT_DELETED;
import static seedu.address.logic.Messages.MESSAGE_CLASS_NOT_FOUND;
import static seedu.address.logic.Messages.MESSAGE_DELETE_CLASS_SUCCESS;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_PERSON;
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
 * Deletes class group(s) from a specific person in the address book.
 */
public class DeleteClassCommand extends Command {

    public static final String COMMAND_WORD = "deleteclass";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes class(es) from the student identified "
            + "by the index number used in the displayed student list. \n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_CLASSGROUP + "CLASS "
            + "[" + PREFIX_CLASSGROUP + "CLASS]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CLASSGROUP + "Math2PM "
            + PREFIX_CLASSGROUP + "Physics3PM";

    private final Index index;
    private final DeleteClassDescriptor deleteClassDescriptor;

    /**
     * Creates a DeleteClassCommand.
     *
     * @param index Index of the person in the filtered list.
     * @param deleteClassDescriptor Descriptor containing classes to be deleted.
     */
    public DeleteClassCommand(Index index, DeleteClassDescriptor deleteClassDescriptor) {
        requireNonNull(index);
        requireNonNull(deleteClassDescriptor);
        this.index = index;
        this.deleteClassDescriptor = new DeleteClassDescriptor(deleteClassDescriptor);
    }

    /**
     * Executes the command to remove class group(s) from the person at the specified index.
     *
     * @param model The model containing the address book.
     * @return A CommandResult with the outcome message.
     * @throws CommandException If the index is invalid or if class group deletions are invalid.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        if (!deleteClassDescriptor.isClassDeleted()
                || deleteClassDescriptor.getClassGroups().get().isEmpty()) {
            throw new CommandException(MESSAGE_CLASS_NOT_DELETED);
        }

        Person editedPerson = createEditedPerson(personToEdit, deleteClassDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_DELETE_CLASS_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Returns the subset of class groups that can be deleted from the given person.
     *
     * @param person The person to check.
     * @param desc The descriptor with requested class deletions.
     * @return A set of deletable class group.
     */
    private static Set<ClassGroup> findDeletableClasses(Person person, DeleteClassDescriptor desc) {
        Set<ClassGroup> requested = desc.getClassGroups().orElse(Set.of());
        return person.getClassGroups().stream()
                .filter(requested::contains)
                .collect(Collectors.toSet());
    }

    /**
     * Returns the subset of requested class groups that do not exist in the person.
     *
     * @param person The person to check.
     * @param desc The descriptor with requested class deletions.
     * @return A set of non-existent class group names.
     */
    private static Set<ClassGroup> findNonExistentClasses(Person person, DeleteClassDescriptor desc) {
        Set<ClassGroup> requested = desc.getClassGroups().orElse(Set.of());
        Set<ClassGroup> existing = person.getClassGroups();
        return requested.stream()
                .filter(c -> !existing.contains(c))
                .collect(Collectors.toSet());
    }

    /**
     * Returns a new {@code Person} with the specified class groups removed.
     *
     * @param personToEdit The original person.
     * @param deleteClassDescriptor The descriptor specifying class groups to delete.
     * @return The edited person with updated class groups.
     * @throws CommandException If any of the specified class groups do not exist on the person.
     */
    private static Person createEditedPerson(Person personToEdit, DeleteClassDescriptor deleteClassDescriptor)
            throws CommandException {

        assert personToEdit != null;

        Name updatedName = personToEdit.getName();
        Phone updatedPhone = personToEdit.getPhone();
        Level updatedLevel = personToEdit.getLevel();
        Set<ClassGroup> currentClasses = new HashSet<>(personToEdit.getClassGroups());
        Set<ClassGroup> toDelete = findDeletableClasses(personToEdit, deleteClassDescriptor);
        Set<ClassGroup> nonExistent = findNonExistentClasses(personToEdit, deleteClassDescriptor);

        if (!nonExistent.isEmpty()) {
            String missingNames = nonExistent.stream()
                    .map(ClassGroup::getClassGroupName)
                    .sorted()
                    .collect(Collectors.joining(", "));
            throw new CommandException(String.format(MESSAGE_CLASS_NOT_FOUND, missingNames));
        }

        currentClasses.removeAll(toDelete);

        // Remove assignments associated with deleted class groups
        Set<String> deletedClassGroupNames = toDelete.stream()
                .map(ClassGroup::getClassGroupName)
                .collect(Collectors.toSet());

        Set<Assignment> updatedAssignments = personToEdit.getAssignments().stream()
                .filter(assignment -> !deletedClassGroupNames.contains(assignment.classGroupName))
                .collect(Collectors.toSet());

        return new Person(
                updatedName,
                updatedPhone,
                updatedLevel,
                currentClasses,
                updatedAssignments
        );
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

        if (!(other instanceof DeleteClassCommand)) {
            return false;
        }

        DeleteClassCommand otherCommand = (DeleteClassCommand) other;
        return index.equals(otherCommand.index)
                && deleteClassDescriptor.equals(otherCommand.deleteClassDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("deleteClassDescriptor", deleteClassDescriptor)
                .toString();
    }

    /**
     * Stores the details of class group(s) to delete from the person.
     * All other fields will remain unchanged.
     */
    public static class DeleteClassDescriptor {
        private Set<ClassGroup> classGroups;

        /**
         * Constructs an empty descriptor.
         */
        public DeleteClassDescriptor() {}

        /**
         * Constructs a copy of the given descriptor.
         *
         * @param toCopy The descriptor to copy.
         */
        public DeleteClassDescriptor(DeleteClassDescriptor toCopy) {
            setClassGroups(toCopy.classGroups);
        }

        /**
         * Returns true if at least one class group is specified for deletion.
         *
         * @return True if class groups are to be deleted.
         */
        public boolean isClassDeleted() {
            return CollectionUtil.isAnyNonNull(classGroups);
        }

        /**
         * Sets the class groups to be deleted.
         *
         * @param classGroups The set of class groups to delete.
         */
        public void setClassGroups(Set<ClassGroup> classGroups) {
            this.classGroups = (classGroups != null) ? new HashSet<>(classGroups) : null;
        }

        /**
         * Returns an unmodifiable set of class groups to be deleted.
         *
         * @return Optional of the class group set.
         */
        public Optional<Set<ClassGroup>> getClassGroups() {
            return (classGroups != null)
                    ? Optional.of(Collections.unmodifiableSet(classGroups))
                    : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof DeleteClassDescriptor)) {
                return false;
            }

            DeleteClassDescriptor otherDescriptor = (DeleteClassDescriptor) other;
            return Objects.equals(classGroups, otherDescriptor.classGroups);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("classGroups", classGroups)
                    .toString();
        }
    }
}
