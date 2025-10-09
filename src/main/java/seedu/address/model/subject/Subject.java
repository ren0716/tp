package seedu.address.model.subject;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an academic subject in TutorTrack.
 * Subjects are limited to a fixed, predefined set of values.
 * This class provides validation, conversion from string, and a standardized string representation.
 */
public class Subject {
    /**
     * Enumerates all valid subjects in TutorTrack.
     * Add or modify subjects here to change the allowed set.
     */
    public enum SubjectType {
        ENGLISH("English"),
        MATH("Math"),
        PHYSICS("Physics"),
        CHEMISTRY("Chemistry"),
        BIOLOGY("Biology"),
        HISTORY("History"),
        GEOGRAPHY("Geography");

        private final String displayName;

        SubjectType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }

        /**
         * Returns the {@code SubjectType} corresponding to the given string.
         * Comparison is case-insensitive.
         *
         * @param input The string to convert (e.g., "math" or "Physics").
         * @return The matching {@code SubjectType}.
         * @throws IllegalArgumentException If the input does not match any valid subject.
         */
        public static SubjectType fromString(String input) {
            requireNonNull(input, "Subject cannot be null");
            for (SubjectType type : values()) {
                if (type.displayName.equalsIgnoreCase(input.trim())) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid subject. Allowed subjects are: "
                    + Subject.getAllowedSubjects());
        }
    }

    /** Message shown when subject input fails validation. */
    public static final String MESSAGE_CONSTRAINTS =
            "Subject must be one of the following (case-insensitive): "
                    + getAllowedSubjects() + ".";

    /** The subject type for this subject. */
    private final SubjectType subjectType;

    /**
     * Constructs a {@code Subject} with the specified subject string.
     *
     * @param subject A valid subject string.
     * @throws IllegalArgumentException If the subject is invalid.
     */
    public Subject(String subject) {
        requireNonNull(subject);
        checkArgument(isValidSubject(subject), MESSAGE_CONSTRAINTS);
        this.subjectType = SubjectType.fromString(subject);
    }

    /**
     * Returns true if a given string is a valid subject.
     *
     * @param test The subject string to validate.
     * @return True if the string corresponds to a valid subject; false otherwise.
     */
    public static boolean isValidSubject(String test) {
        if (test == null) {
            return false;
        }
        for (SubjectType type : SubjectType.values()) {
            if (type.toString().equalsIgnoreCase(test.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a comma-separated string of all allowed subjects.
     *
     * @return A formatted list of allowed subjects.
     */
    public static String getAllowedSubjects() {
        StringBuilder sb = new StringBuilder();
        for (SubjectType type : SubjectType.values()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(type.toString());
        }
        return sb.toString();
    }

    /**
     * Returns the string representation of this subject (e.g., "Math").
     *
     * @return The formatted subject name.
     */
    @Override
    public String toString() {
        return subjectType.toString();
    }

    /**
     * Returns the internal {@code SubjectType} for this subject.
     *
     * @return The {@code SubjectType}.
     */
    public SubjectType getSubjectType() {
        return subjectType;
    }

    /**
     * Compares this {@code Subject} to another object for equality.
     *
     * @param other The object to compare.
     * @return True if both represent the same subject; false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Subject)) {
            return false;
        }
        Subject otherSubject = (Subject) other;
        return subjectType == otherSubject.subjectType;
    }

    @Override
    public int hashCode() {
        return subjectType.hashCode();
    }
}
