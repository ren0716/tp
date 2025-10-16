package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

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
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Deletes class(es) from an existing person in the address book.
 */
public class DeleteClassCommand extends Command {

    public static final String COMMAND_WORD = "deleteclass";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes class(es) from the person identified "
            + "by the index number used in the displayed person list. \n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_CLASSGROUP + "CLASS_NAME "
            + "[" + PREFIX_CLASSGROUP + "CLASS_NAME]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CLASSGROUP + "Math-1000 "
            + PREFIX_CLASSGROUP + "Physics-2000";

    public static final String MESSAGE_DELETE_CLASS_SUCCESS = "Deleted class(es) from: %1$s";
    public static final String MESSAGE_CLASS_NOT_PROVIDED = "At least one class to delete must be provided.";
    public static final String MESSAGE_CLASS_NOT_FOUND = "Cannot delete non-existent class(es): %s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final DeleteClassDescriptor deleteClassDescriptor;

    public DeleteClassCommand(Index index, DeleteClassDescriptor deleteClassDescriptor) {
        requireNonNull(index);
        requireNonNull(deleteClassDescriptor);

        this.index = index;
        this.deleteClassDescriptor = new DeleteClassDescriptor(deleteClassDescriptor);
    }

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
            throw new CommandException(MESSAGE_CLASS_NOT_PROVIDED);
        }

        Person editedPerson = createEditedPerson(personToEdit, deleteClassDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_DELETE_CLASS_SUCCESS, Messages.format(editedPerson)));
    }

    private static Set<String> findDeletableClasses(Person person, DeleteClassDescriptor desc) {
        Set<String> requested = desc.getClassGroups().orElse(Set.of());
        return person.getClassGroups().stream()
                .filter(requested::contains)
                .collect(Collectors.toSet());
    }

    private static Set<String> findNonExistentClasses(Person person, DeleteClassDescriptor desc) {
        Set<String> requested = desc.getClassGroups().orElse(Set.of());
        Set<String> existing = person.getClassGroups();
        return requested.stream()
                .filter(c -> !existing.contains(c))
                .collect(Collectors.toSet());
    }

    private static Person createEditedPerson(Person personToEdit, DeleteClassDescriptor deleteClassDescriptor)
            throws CommandException {

        assert personToEdit != null;

        Name updatedName = personToEdit.getName();
        Phone updatedPhone = personToEdit.getPhone();
        Level updatedLevel = personToEdit.getLevel();
        Set<String> currentClasses = new HashSet<>(personToEdit.getClassGroups());
        Set<String> toDelete = findDeletableClasses(personToEdit, deleteClassDescriptor);
        Set<String> nonExistent = findNonExistentClasses(personToEdit, deleteClassDescriptor);

        if (!nonExistent.isEmpty()) {
            String missingNames = nonExistent.stream().sorted().collect(Collectors.joining(", "));
            throw new CommandException(String.format(MESSAGE_CLASS_NOT_FOUND, missingNames));
        }

        currentClasses.removeAll(toDelete);

        return new Person(
                updatedName,
                updatedPhone,
                updatedLevel,
                currentClasses,
                personToEdit.getAssignments() // if assignments still exist
        );
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
     * Descriptor for deleting class(es) from a person.
     */
    public static class DeleteClassDescriptor {
        private Set<String> classGroups;

        public DeleteClassDescriptor() {}

        public DeleteClassDescriptor(DeleteClassDescriptor toCopy) {
            setClassGroups(toCopy.classGroups);
        }

        public boolean isClassDeleted() {
            return CollectionUtil.isAnyNonNull(classGroups);
        }

        public void setClassGroups(Set<String> classGroups) {
            this.classGroups = (classGroups != null) ? new HashSet<>(classGroups) : null;
        }

        public Optional<Set<String>> getClassGroups() {
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
