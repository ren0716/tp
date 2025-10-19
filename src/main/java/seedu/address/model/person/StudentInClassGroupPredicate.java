package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code ClassGroup} matches the specified class group keyword.
 * Keyword matching is case-insensitive.
 */
public class StudentInClassGroupPredicate implements Predicate<Person> {
    private final String classGroup;

    /**
     * Constructs a {@code StudentInClassGroupPredicate} with the specified class group keyword.
     *
     * @param classGroup The class group keyword to match against.
     */
    public StudentInClassGroupPredicate(String classGroup) {
        this.classGroup = classGroup;
    }

    /**
     * Tests whether the given person belongs to the class group specified by this predicate.
     * The comparison is case-insensitive.
     *
     * @param person The person to test.
     * @return true if the person belongs to the specified class group, false otherwise.
     */
    @Override
    public boolean test(Person person) {
        // Treats cg as String, update when ClassGroup is created
        return person.getClassGroups().stream()
                .anyMatch(cg -> cg.equalsIgnoreCase(classGroup));
    }

    /**
     * Returns true if both predicates have the same class group keyword.
     *
     * @param other The other object to compare with.
     * @return true if both predicates are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StudentInClassGroupPredicate)) {
            return false;
        }

        StudentInClassGroupPredicate otherStudentInClassGroupPredicate = (StudentInClassGroupPredicate) other;
        return classGroup.equals(otherStudentInClassGroupPredicate.classGroup);
    }

    /**
     * Returns a string representation of this predicate.
     *
     * @return A string containing the class group keyword.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).add("classGroup", classGroup).toString();
    }
}
