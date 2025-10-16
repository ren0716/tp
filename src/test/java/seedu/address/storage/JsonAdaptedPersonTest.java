package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_LEVEL = " ";
    private static final String INVALID_CLASSGROUP = "#";
    private static final String INVALID_ASSIGNMENT = "#Science";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_LEVEL = BENSON.getLevel().toString();
    private static final List<String> VALID_CLASSGROUP = BENSON.getClassGroups().stream()
            .collect(Collectors.toList());
    private static final List<JsonAdaptedAssignment> VALID_ASSIGNMENTS = BENSON.getAssignments().stream()
            .map(JsonAdaptedAssignment::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_LEVEL, VALID_CLASSGROUP, VALID_ASSIGNMENTS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_LEVEL,
                VALID_CLASSGROUP, VALID_ASSIGNMENTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_LEVEL, VALID_CLASSGROUP, VALID_ASSIGNMENTS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null, VALID_LEVEL,
                VALID_CLASSGROUP, VALID_ASSIGNMENTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidLevel_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_LEVEL, VALID_CLASSGROUP, VALID_ASSIGNMENTS);
        String expectedMessage = Level.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_null_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null,
                VALID_CLASSGROUP, VALID_ASSIGNMENTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Level.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<String> invalidClassGroups = new ArrayList<>(VALID_CLASSGROUP);
        invalidClassGroups.add(INVALID_CLASSGROUP);
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_LEVEL, invalidClassGroups, VALID_ASSIGNMENTS);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidAssignments_throwsIllegalValueException() {
        List<JsonAdaptedAssignment> invalidAssignments = new ArrayList<>(VALID_ASSIGNMENTS);
        invalidAssignments.add(new JsonAdaptedAssignment(INVALID_ASSIGNMENT));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_LEVEL, VALID_CLASSGROUP, invalidAssignments);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

}
