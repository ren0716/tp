package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.person.Person;


/**
 * Sets a student's assignment status as marked
 */
public class MarkAssignmentCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the assignment of the student identified by the index number"
            + "used in the displayed person list and the assignment name.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "a/ASSIGNMENT_NAME\n"
            + "Example: " + COMMAND_WORD + " 1" + " a/Physics-1800\n";
    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Marked Assignment %1$s of %2$s";

    private static final Logger logger = LogsCenter.getLogger(MarkAssignmentCommand.class);

    private final Index targetIndex;
    private final Assignment assignment;

    /**
     * Creates a MarkAssignmentCommand.
     *
     * @param targetIndex index of the student in the displayed list
     * @param assignment assignment to mark
     */
    public MarkAssignmentCommand(Index targetIndex, Assignment assignment) {
        this.targetIndex = targetIndex;
        this.assignment = assignment;
    }

    /**
     * Executes the mark assignment command.
     *
     * Steps:
     * 1. Validate and fetch the target person from the displayed list.
     * 2. Retrieve the person's assignment set and validate the requested assignment exists.
     * 3. Find the matching assignment instance, mark it as completed, and
     *    return a formatted success message.
     *
     * @param model the model which provides access to the displayed person list and persistence
     * @return a CommandResult containing the success message
     * @throws CommandException if the target index is invalid or the assignment is not present for the person
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();
        Person personToMark = getPersonToMark(lastShownList);

        Set<Assignment> personAssignments = getPersonAssignmentSet(personToMark);
        ensureAssignmentExists(personAssignments, personToMark);

        Assignment marked = findAndMarkAssignment(personAssignments);

        return new CommandResult(formatSuccessMessage(marked, personToMark), true);
    }

    /**
     * Returns the person at the configured index from the provided displayed list.
     *
     * @throws CommandException if the index is out of bounds
     */
    private Person getPersonToMark(List<Person> lastShownList) throws CommandException {
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return lastShownList.get(targetIndex.getZeroBased());
    }

    /**
     * Returns the set of assignments for the given person.
     */
    private Set<Assignment> getPersonAssignmentSet(Person person) {
        return person.getAssignments();
    }

    /**
     * Validates that the specified `assignment` exists in the person's assignment set.
     *
     * @throws CommandException if the assignment is not present
     */
    private void ensureAssignmentExists(Set<Assignment> assignments, Person person) throws CommandException {
        if (!assignments.contains(assignment)) {
            // Log helpful debug info to aid troubleshooting
            logger.warning(() -> String.format(
                    "Person %s (index %d) does not have assignment '%s'. Person assignments: %s",
                    person.getName(), targetIndex.getZeroBased(), assignment.getAssignmentName(),
                    assignmentsToString(assignments)));
            throw new CommandException(Messages.MESSAGE_INVALID_ASSIGNMENT_IN_PERSON);
        }
    }

    /**
     * Returns a compact string representation of the given assignments set for logging.
     */
    private String assignmentsToString(Set<Assignment> assignments) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Iterator<Assignment> it = assignments.iterator();
        while (it.hasNext()) {
            sb.append(it.next().getAssignmentName());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * Finds the matching Assignment instance in the given set and marks it.
     *
     * @return the Assignment instance that was marked, or null if none matched (should not happen when validated)
     */
    private Assignment findAndMarkAssignment(Set<Assignment> assignments) {
        Iterator<Assignment> iter = assignments.iterator();
        while (iter.hasNext()) {
            Assignment a = iter.next();
            if (a.getAssignmentName().equals(assignment.getAssignmentName())) {
                a.mark();
                return a;
            }
        }
        // should not reach here because ensureAssignmentExists was called before
        return null;
    }

    /**
     * Formats the user-visible success message after marking an assignment.
     */
    private String formatSuccessMessage(Assignment a, Person p) {
        return String.format(MESSAGE_DELETE_PERSON_SUCCESS, a.getAssignmentName(), p.getName());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MarkAssignmentCommand)) {
            return false;
        }

        MarkAssignmentCommand otherMarkAssignmentCommand = (MarkAssignmentCommand) other;
        return targetIndex.equals(otherMarkAssignmentCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("assignment", assignment)
                .toString();
    }
}
