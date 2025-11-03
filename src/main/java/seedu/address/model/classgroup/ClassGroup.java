package seedu.address.model.classgroup;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a ClassGroup in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidClassGroupName(String)}
 */
public class ClassGroup {

    public static final String MESSAGE_CONSTRAINTS = "Class names should start with and "
            + "contain alphanumeric characters, and it should not be blank.\n"
            + "Allowed special characters are hyphens (-) and spaces.";
    // Allow alphanumeric characters, spaces and hyphens; must contain at least one alphanumeric character
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} \\-]*";

    public final String classGroupName;

    /**
     * Constructs a {@code ClassGroup}.
     *
     * @param classGroupName A valid class group name.
     */
    public ClassGroup(String classGroupName) {
        requireNonNull(classGroupName);
        checkArgument(isValidClassGroupName(classGroupName), MESSAGE_CONSTRAINTS);
        this.classGroupName = classGroupName;
    }
    /**
     * Returns true if a given string is a valid class group name.
     */
    public static boolean isValidClassGroupName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClassGroup)) {
            return false;
        }

        ClassGroup otherClassGroup = (ClassGroup) other;
        return classGroupName.equals(otherClassGroup.classGroupName);
    }

    public String getClassGroupName() {
        return classGroupName;
    }

    @Override
    public int hashCode() {
        return classGroupName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        return '[' + classGroupName + ']';
    }

}
