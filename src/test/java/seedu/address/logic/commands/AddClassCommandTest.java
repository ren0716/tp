package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_ADD_CLASS_SUCCESS;
import static seedu.address.logic.Messages.MESSAGE_CLASSES_NOT_ADDED;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_CLASSES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASSGROUP_MATH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASSGROUP_PHYSICS;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddClassCommand.AddClassDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.classgroup.ClassGroup;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for AddClassCommand.
 */
public class AddClassCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addSingleClassUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<ClassGroup> classesToAdd = new HashSet<>();
        classesToAdd.add(new ClassGroup(VALID_CLASSGROUP_MATH));

        AddClassDescriptor descriptor = new AddClassDescriptor();
        descriptor.setClassGroups(classesToAdd);
        AddClassCommand addClassCommand = new AddClassCommand(INDEX_FIRST_PERSON, descriptor);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withClassGroups(VALID_CLASSGROUP_MATH).build();

        String expectedMessage = String.format(MESSAGE_ADD_CLASS_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(addClassCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addMultipleClassesUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<ClassGroup> classesToAdd = new HashSet<>();
        classesToAdd.add(new ClassGroup(VALID_CLASSGROUP_MATH));
        classesToAdd.add(new ClassGroup(VALID_CLASSGROUP_PHYSICS));

        AddClassDescriptor descriptor = new AddClassDescriptor();
        descriptor.setClassGroups(classesToAdd);
        AddClassCommand addClassCommand = new AddClassCommand(INDEX_FIRST_PERSON, descriptor);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withClassGroups(VALID_CLASSGROUP_MATH, VALID_CLASSGROUP_PHYSICS).build();

        String expectedMessage = String.format(MESSAGE_ADD_CLASS_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(addClassCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addClassFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<ClassGroup> classesToAdd = new HashSet<>();
        classesToAdd.add(new ClassGroup(VALID_CLASSGROUP_MATH));

        AddClassDescriptor descriptor = new AddClassDescriptor();
        descriptor.setClassGroups(classesToAdd);
        AddClassCommand addClassCommand = new AddClassCommand(INDEX_FIRST_PERSON, descriptor);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withClassGroups(VALID_CLASSGROUP_MATH).build();

        String expectedMessage = String.format(MESSAGE_ADD_CLASS_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(addClassCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateClass_throwsCommandException() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // First add a class
        Set<ClassGroup> classesToAdd = new HashSet<>();
        classesToAdd.add(new ClassGroup(VALID_CLASSGROUP_MATH));
        Person editedPerson = new PersonBuilder(personToEdit)
                .withClassGroups(VALID_CLASSGROUP_MATH).build();
        model.setPerson(personToEdit, editedPerson);

        // Try to add the same class again
        AddClassDescriptor descriptor = new AddClassDescriptor();
        descriptor.setClassGroups(classesToAdd);
        AddClassCommand addClassCommand = new AddClassCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(MESSAGE_DUPLICATE_CLASSES, VALID_CLASSGROUP_MATH);
        assertCommandFailure(addClassCommand, model, expectedMessage);
    }

    @Test
    public void execute_noClassProvided_throwsCommandException() {
        AddClassDescriptor descriptor = new AddClassDescriptor();
        descriptor.setClassGroups(new HashSet<>());
        AddClassCommand addClassCommand = new AddClassCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(addClassCommand, model, MESSAGE_CLASSES_NOT_ADDED);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<ClassGroup> classesToAdd = new HashSet<>();
        classesToAdd.add(new ClassGroup(VALID_CLASSGROUP_MATH));

        AddClassDescriptor descriptor = new AddClassDescriptor();
        descriptor.setClassGroups(classesToAdd);
        AddClassCommand addClassCommand = new AddClassCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(addClassCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        Set<ClassGroup> classesToAdd = new HashSet<>();
        classesToAdd.add(new ClassGroup(VALID_CLASSGROUP_MATH));

        AddClassDescriptor descriptor = new AddClassDescriptor();
        descriptor.setClassGroups(classesToAdd);
        AddClassCommand addClassCommand = new AddClassCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(addClassCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<ClassGroup> mathClass = new HashSet<>();
        mathClass.add(new ClassGroup(VALID_CLASSGROUP_MATH));
        Set<ClassGroup> physicsClass = new HashSet<>();
        physicsClass.add(new ClassGroup(VALID_CLASSGROUP_PHYSICS));

        AddClassDescriptor mathDescriptor = new AddClassDescriptor();
        mathDescriptor.setClassGroups(mathClass);
        AddClassDescriptor physicsDescriptor = new AddClassDescriptor();
        physicsDescriptor.setClassGroups(physicsClass);

        AddClassCommand addMathToFirstCommand = new AddClassCommand(INDEX_FIRST_PERSON, mathDescriptor);
        AddClassCommand addMathToSecondCommand = new AddClassCommand(INDEX_SECOND_PERSON, mathDescriptor);
        AddClassCommand addPhysicsToFirstCommand = new AddClassCommand(INDEX_FIRST_PERSON, physicsDescriptor);

        // same object -> returns true
        assertTrue(addMathToFirstCommand.equals(addMathToFirstCommand));

        // same values -> returns true
        AddClassCommand addMathToFirstCommandCopy = new AddClassCommand(INDEX_FIRST_PERSON, mathDescriptor);
        assertTrue(addMathToFirstCommand.equals(addMathToFirstCommandCopy));

        // different types -> returns false
        assertFalse(addMathToFirstCommand.equals(1));

        // null -> returns false
        assertFalse(addMathToFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(addMathToFirstCommand.equals(addMathToSecondCommand));

        // different class -> returns false
        assertFalse(addMathToFirstCommand.equals(addPhysicsToFirstCommand));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        AddClassDescriptor descriptor = new AddClassDescriptor();
        Set<ClassGroup> classesToAdd = new HashSet<>();
        classesToAdd.add(new ClassGroup(VALID_CLASSGROUP_MATH));
        descriptor.setClassGroups(classesToAdd);
        AddClassCommand addClassCommand = new AddClassCommand(index, descriptor);
        String expected = AddClassCommand.class.getCanonicalName() + "{index=" + index
                + ", addClassDescriptor=" + descriptor + "}";
        assertEquals(expected, addClassCommand.toString());
    }
}

