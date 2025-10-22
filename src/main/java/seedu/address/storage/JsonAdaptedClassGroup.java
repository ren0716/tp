package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.classgroup.ClassGroup;

/**
 * Jackson-friendly version of {@link ClassGroup}.
 */
class JsonAdaptedClassGroup {

    private final String classGroupName;

    /**
     * Constructs a {@code JsonUpdatedClassGroup} with the given {@code classGroupName}.
     */
    @JsonCreator
    public JsonAdaptedClassGroup(String classGroupName) {
        this.classGroupName = classGroupName;
    }

    /**
     * Converts a given {@code ClassGroup} into this class for Jackson use.
     */
    public JsonAdaptedClassGroup(ClassGroup source) {
        classGroupName = source.classGroupName;
    }

    @JsonValue
    public String getClassGroupName() {
        return classGroupName;
    }

    /**
     * Converts this Jackson-friendly adapted ClassGroup object into the model's {@code ClassGroup} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted classGroup.
     */
    public ClassGroup toModelType() throws IllegalValueException {
        if (!ClassGroup.isValidClassGroupName(classGroupName)) {
            throw new IllegalValueException(ClassGroup.MESSAGE_CONSTRAINTS);
        }
        return new ClassGroup(classGroupName);
    }

}
