package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.classgroup.ClassGroup;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String level;
    private final List<JsonAdaptedClassGroup> classGroups = new ArrayList<>();
    private final List<JsonAdaptedAssignment> assignments = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("level") String level, @JsonProperty("classGroups") List<JsonAdaptedClassGroup> classGroups,
            @JsonProperty("assignments") List<JsonAdaptedAssignment> assignments) {
        this.name = name;
        this.phone = phone;
        this.level = level;
        if (classGroups != null) {
            this.classGroups.addAll(classGroups);
        }
        if (assignments != null) {
            this.assignments.addAll(assignments);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        level = source.getLevel().value;
        classGroups.addAll(source.getClassGroups().stream()
                .map(JsonAdaptedClassGroup::new)
                .collect(Collectors.toList()));
        assignments.addAll(source.getAssignments().stream()
                .map(JsonAdaptedAssignment::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<ClassGroup> personClasses = new ArrayList<>();
        final List<Assignment> personAssignments = new ArrayList<>();

        for (JsonAdaptedClassGroup classGroup : classGroups) {
            personClasses.add(classGroup.toModelType());
        }

        for (JsonAdaptedAssignment assignment : assignments) {
            personAssignments.add(assignment.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (level == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Level.class.getSimpleName()));
        }
        if (!Level.isValidLevel(level)) {
            throw new IllegalValueException(Level.MESSAGE_CONSTRAINTS);
        }
        final Level modelLevel = new Level(level);

        final Set<ClassGroup> modelClasses = new HashSet<>(personClasses);
        final Set<Assignment> modelAssignments = new HashSet<>(personAssignments);
        return new Person(modelName, modelPhone, modelLevel, modelClasses, modelAssignments);
    }

}
