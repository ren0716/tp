package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASSGROUP_PHYSICS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LEVEL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getClassGroups().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name and phone number, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE)
                .withLevel(VALID_LEVEL_BOB).withClassGroups(VALID_CLASSGROUP_PHYSICS).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        Person editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different level -> returns false
        editedAlice = new PersonBuilder(ALICE).withLevel(VALID_LEVEL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different classGroup -> returns false
        editedAlice = new PersonBuilder(ALICE).withClassGroups(VALID_CLASSGROUP_PHYSICS).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void hashCode_sameData_sameHash() {
        Person person1 = new PersonBuilder(ALICE).build();
        Person person2 = new PersonBuilder(ALICE).build();

        assertEquals(person1.hashCode(), person2.hashCode());
    }

    @Test
    public void hashCode_differentData_differentHash() {
        Person person1 = new PersonBuilder(ALICE).build();
        Person person2 = new PersonBuilder(BOB).build();

        assertNotEquals(person1.hashCode(), person2.hashCode());
    }


    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", level=" + ALICE.getLevel() + ", classes=" + ALICE.getClassGroups()
                + ", assignments=" + ALICE.getAssignments() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
