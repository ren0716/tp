package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_ASSIGNMENT_NOT_FOUND;
import static seedu.address.logic.Messages.MESSAGE_CLASS_NOT_EXIST;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_PERSON;
import static seedu.address.logic.Messages.MESSAGE_UNASSIGNALL_SUCCESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSGROUP;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.classgroup.ClassGroup;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Unassigns an assignment from all students in a specified class group.
 * This command filters students by class group name and removes the specified assignment
 * from all students enrolled in that class.
 */
public class UnassignAllCommand extends Command {

    public static final String COMMAND_WORD = "unassignall";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unassigns an assignment from all students in the specified class.\n"
            + "Parameters: "
            + PREFIX_CLASSGROUP + "CLASS "
            + PREFIX_ASSIGNMENT + "ASSIGNMENT\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CLASSGROUP + "Math 3PM "
            + PREFIX_ASSIGNMENT + "Homework1";

    private final String classGroupName;
    private final Assignment assignment;

    /**
     * Creates an UnassignAllCommand to unassign the specified assignment from all students
     * in the specified class group.
     *
     * @param classGroupName The name of the class group to filter students by. Must not be null.
     * @param assignment The assignment to be unassigned from all students in the class group. Must not be null.
     * @throws NullPointerException if classGroupName or assignment is null.
     */
    public UnassignAllCommand(String classGroupName, Assignment assignment) {
        requireNonNull(classGroupName);
        requireNonNull(assignment);
        this.classGroupName = classGroupName;
        this.assignment = assignment;
    }

    /**
     * Executes the unassign all command by removing the specified assignment from all students
     * in the specified class group.
     *
     * @param model The model which the command should operate on. Must not be null.
     * @return A CommandResult containing the success message with the number of students affected.
     * @throws CommandException if no students are found in the class group or if a duplicate person is detected.
     * @throws NullPointerException if model is null.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> fullList = model.getAddressBook().getPersonList();

        // Filter students who have the specified class group
        List<Person> studentsInClass = fullList.stream()
                .filter(person -> hasClassGroup(person, classGroupName))
                .collect(Collectors.toList());

        if (studentsInClass.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_CLASS_NOT_EXIST,
                    StringUtil.toTitleCase(classGroupName)));
        }

        int unassignedCount = 0;
        // Unassign the assignment from each student in the class
        for (Person person : studentsInClass) {
            // Checks if the person has this assignment
            if (person.getAssignments().contains(assignment)) {
                Person editedPerson = createPersonWithoutAssignment(person, assignment);

                if (!person.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
                    throw new CommandException(MESSAGE_DUPLICATE_PERSON);
                }

                model.setPerson(person, editedPerson);
                unassignedCount++;
            }
        }

        // If no students had the assignment, output error message
        if (unassignedCount == 0) {
            throw new CommandException(String.format(MESSAGE_ASSIGNMENT_NOT_FOUND,
                    assignment.getAssignmentName(), classGroupName));
        }

        return new CommandResult(String.format(MESSAGE_UNASSIGNALL_SUCCESS,
                assignment.getAssignmentName(), unassignedCount, classGroupName));
    }

    /**
     * Checks if the person has a class group with the specified name.
     * The comparison is case-insensitive.
     *
     * @param person The person to check. Must not be null.
     * @param classGroupName The name of the class group to look for. Must not be null.
     * @return true if the person has a class group with the specified name (case-insensitive), false otherwise.
     */
    private boolean hasClassGroup(Person person, String classGroupName) {
        return person.getClassGroups().stream()
                .map(ClassGroup::getClassGroupName)
                .anyMatch(name -> name.equalsIgnoreCase(classGroupName));
    }

    /**
     * Creates and returns a {@code Person} with the assignment removed from their existing assignments.
     * All other fields (name, phone, level, class groups) remain unchanged.
     *
     * @param person The original person. Must not be null.
     * @param assignment The assignment to remove. Must not be null.
     * @return A new Person object with the assignment removed from the assignments set.
     */
    private static Person createPersonWithoutAssignment(Person person, Assignment assignment) {
        assert person != null;

        Name name = person.getName();
        Phone phone = person.getPhone();
        Level level = person.getLevel();
        Set<ClassGroup> classGroups = person.getClassGroups();
        Set<Assignment> updatedAssignments = person.getAssignments().stream()
                .filter(a -> !a.equals(assignment))
                .collect(Collectors.toSet());

        return new Person(name, phone, level, classGroups, updatedAssignments);
    }

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    /**
     * Checks if this UnassignAllCommand is equal to another object.
     * Two UnassignAllCommand objects are considered equal if they have the same class group name
     * and assignment.
     *
     * @param other The object to compare with.
     * @return true if the other object is an UnassignAllCommand with the same class group name and assignment,
     *         false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnassignAllCommand)) {
            return false;
        }

        UnassignAllCommand otherCommand = (UnassignAllCommand) other;
        return classGroupName.equals(otherCommand.classGroupName)
                && assignment.equals(otherCommand.assignment);
    }

    /**
     * Returns a string representation of this UnassignAllCommand for debugging purposes.
     *
     * @return A string containing the class group name and assignment details.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("classGroupName", classGroupName)
                .add("assignment", assignment)
                .toString();
    }
}
