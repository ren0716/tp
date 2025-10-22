package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.classgroup.ClassGroup;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;

    // Data fields
    private final Level level;
    private final Set<ClassGroup> classGroups = new HashSet<>();
    private final Set<Assignment> assignments = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Level level, Set<ClassGroup> classGroups, Set<Assignment> assignments) {
        requireAllNonNull(name, phone, level, classGroups);
        this.name = name;
        this.phone = phone;
        this.level = level;
        this.classGroups.addAll(classGroups);
        this.assignments.addAll(assignments);

    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Level getLevel() {
        return level;
    }

    /**
     * Returns an immutable classGroup set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<ClassGroup> getClassGroups() {
        return Collections.unmodifiableSet(classGroups);
    }

    /**
     * Returns an immutable assignment set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Assignment> getAssignments() {
        return Collections.unmodifiableSet(assignments);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        // instanceof handles nulls
        if (otherPerson == null) {
            return false;
        }

        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone);
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && level.equals(otherPerson.level)
                && classGroups.equals(otherPerson.classGroups)
                && assignments.equals(otherPerson.assignments);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, level, classGroups, assignments);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("level", level)
                .add("classes", classGroups)
                .add("assignments", assignments)
                .toString();
    }

    /**
     * Returns a new Person with the same attributes as this person but with the given assignments.
     * @param newAssignments The new set of assignments
     * @return A new Person instance with updated assignments
     */
    public Person withAssignments(Set<Assignment> newAssignments) {
        return new Person(this.name, this.phone, this.level, this.classGroups, newAssignments);
    }

}
