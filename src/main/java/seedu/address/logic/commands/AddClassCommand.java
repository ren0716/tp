package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_ADD_CLASS_SUCCESS;
import static seedu.address.logic.Messages.MESSAGE_CLASSES_NOT_ADDED;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_CLASSES;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_PERSON;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
 * Adds class(es) to an existing person in the address book.
 */
public class AddClassCommand extends Command {

    public static final String COMMAND_WORD = "addclass";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds class(es) to the student identified "
            + "by the index number used in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_CLASSGROUP + "CLASS "
            + "[" + PREFIX_CLASSGROUP + "CLASS]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CLASSGROUP + "Math-1000 "
            + PREFIX_CLASSGROUP + "Physics-2000";

    private final Index index;
    private final AddClassDescriptor addClassDescriptor;

    /**
     * @param index of the person in the filtered person list to add class to
     * @param addClassDescriptor details to add class to the person with
     */
    public AddClassCommand(Index index, AddClassDescriptor addClassDescriptor) {
        requireNonNull(index);
        requireNonNull(addClassDescriptor);

        this.index = index;
        this.addClassDescriptor = new AddClassDescriptor(addClassDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        if (addClassDescriptor.getClassGroups().isEmpty()
                || addClassDescriptor.getClassGroups().get().isEmpty()) {
            throw new CommandException(MESSAGE_CLASSES_NOT_ADDED);
        }

        Set<ClassGroup> duplicates = findDuplicateClasses(personToEdit, addClassDescriptor);
        if (!duplicates.isEmpty()) {
            String duplicateNames = duplicates.stream()
                    .map(ClassGroup::getClassGroupName)
                    .sorted()
                    .collect(Collectors.joining(", "));
            throw new CommandException(String.format(MESSAGE_DUPLICATE_CLASSES, duplicateNames));
        }

        Person editedPerson = createEditedPerson(personToEdit, addClassDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_ADD_CLASS_SUCCESS, Messages.format(editedPerson)));
    }

    private static Set<ClassGroup> findDuplicateClasses(Person person, AddClassDescriptor desc) {
        Set<ClassGroup> newClasses = desc.getClassGroups().orElse(Set.of());
        return person.getClassGroups().stream()
                .filter(newClasses::contains)
                .collect(Collectors.toSet());
    }

    private static Person createEditedPerson(Person personToEdit, AddClassDescriptor addClassDescriptor) {
        assert personToEdit != null;

        Name updatedName = personToEdit.getName();
        Phone updatedPhone = personToEdit.getPhone();
        Level updatedLevel = personToEdit.getLevel();
        Set<Assignment> updatedAssignments = personToEdit.getAssignments();

        Set<ClassGroup> updatedClasses = Stream.concat(
                personToEdit.getClassGroups().stream(),
                addClassDescriptor.getClassGroups().orElse(Set.of()).stream()
        ).collect(Collectors.toSet());

        return new Person(updatedName, updatedPhone, updatedLevel , updatedClasses, updatedAssignments);
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

        if (!(other instanceof AddClassCommand)) {
            return false;
        }

        AddClassCommand otherCommand = (AddClassCommand) other;
        return index.equals(otherCommand.index)
                && addClassDescriptor.equals(otherCommand.addClassDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("addClassDescriptor", addClassDescriptor)
                .toString();
    }

    /**
     * Stores the details to add class(es) to a person. All other fields will remain unchanged.
     */
    public static class AddClassDescriptor {
        private Set<ClassGroup> classGroups;

        public AddClassDescriptor() {}

        public AddClassDescriptor(AddClassDescriptor toCopy) {
            setClassGroups(toCopy.classGroups);
        }

        public boolean hasClasses() {
            return CollectionUtil.isAnyNonNull(classGroups) && !classGroups.isEmpty();
        }

        public void setClassGroups(Set<ClassGroup> classGroups) {
            this.classGroups = (classGroups != null) ? new HashSet<>(classGroups) : null;
        }

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

            if (!(other instanceof AddClassDescriptor)) {
                return false;
            }

            AddClassDescriptor otherDescriptor = (AddClassDescriptor) other;
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
