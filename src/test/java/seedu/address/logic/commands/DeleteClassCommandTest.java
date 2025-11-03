package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_CLASS_NOT_DELETED;
import static seedu.address.logic.Messages.MESSAGE_CLASS_NOT_FOUND;
import static seedu.address.logic.Messages.MESSAGE_DELETE_CLASS_SUCCESS;
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
import seedu.address.logic.commands.DeleteClassCommand.DeleteClassDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.classgroup.ClassGroup;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for DeleteClassCommand.
 */
public class DeleteClassCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_deleteSingleClassUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // First add a class to delete
        Person personWithClass = new PersonBuilder(personToEdit)
                .withClassGroups(VALID_CLASSGROUP_MATH).build();
        model.setPerson(personToEdit, personWithClass);

        Set<ClassGroup> classesToDelete = new HashSet<>();
        classesToDelete.add(new ClassGroup(VALID_CLASSGROUP_MATH));

        DeleteClassDescriptor descriptor = new DeleteClassDescriptor();
        descriptor.setClassGroups(classesToDelete);
        DeleteClassCommand deleteClassCommand = new DeleteClassCommand(INDEX_FIRST_PERSON, descriptor);

        Person editedPerson = new PersonBuilder(personWithClass)
                .withClassGroups().build();

        String expectedMessage = String.format(MESSAGE_DELETE_CLASS_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithClass, editedPerson);

        assertCommandSuccess(deleteClassCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteMultipleClassesUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // First add classes to delete
        Person personWithClasses = new PersonBuilder(personToEdit)
                .withClassGroups(VALID_CLASSGROUP_MATH, VALID_CLASSGROUP_PHYSICS).build();
        model.setPerson(personToEdit, personWithClasses);

        Set<ClassGroup> classesToDelete = new HashSet<>();
        classesToDelete.add(new ClassGroup(VALID_CLASSGROUP_MATH));
        classesToDelete.add(new ClassGroup(VALID_CLASSGROUP_PHYSICS));

        DeleteClassDescriptor descriptor = new DeleteClassDescriptor();
        descriptor.setClassGroups(classesToDelete);
        DeleteClassCommand deleteClassCommand = new DeleteClassCommand(INDEX_FIRST_PERSON, descriptor);

        Person editedPerson = new PersonBuilder(personWithClasses)
                .withClassGroups().build();

        String expectedMessage = String.format(MESSAGE_DELETE_CLASS_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithClasses, editedPerson);

        assertCommandSuccess(deleteClassCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteOneOfMultipleClasses_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Add two classes
        Person personWithClasses = new PersonBuilder(personToEdit)
                .withClassGroups(VALID_CLASSGROUP_MATH, VALID_CLASSGROUP_PHYSICS).build();
        model.setPerson(personToEdit, personWithClasses);

        // Delete only one class
        Set<ClassGroup> classesToDelete = new HashSet<>();
        classesToDelete.add(new ClassGroup(VALID_CLASSGROUP_MATH));

        DeleteClassDescriptor descriptor = new DeleteClassDescriptor();
        descriptor.setClassGroups(classesToDelete);
        DeleteClassCommand deleteClassCommand = new DeleteClassCommand(INDEX_FIRST_PERSON, descriptor);

        Person editedPerson = new PersonBuilder(personWithClasses)
                .withClassGroups(VALID_CLASSGROUP_PHYSICS).build();

        String expectedMessage = String.format(MESSAGE_DELETE_CLASS_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithClasses, editedPerson);

        assertCommandSuccess(deleteClassCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteClassFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // First add a class to delete
        Person personWithClass = new PersonBuilder(personToEdit)
                .withClassGroups(VALID_CLASSGROUP_MATH).build();
        model.setPerson(personToEdit, personWithClass);

        Set<ClassGroup> classesToDelete = new HashSet<>();
        classesToDelete.add(new ClassGroup(VALID_CLASSGROUP_MATH));

        DeleteClassDescriptor descriptor = new DeleteClassDescriptor();
        descriptor.setClassGroups(classesToDelete);
        DeleteClassCommand deleteClassCommand = new DeleteClassCommand(INDEX_FIRST_PERSON, descriptor);

        Person editedPerson = new PersonBuilder(personWithClass)
                .withClassGroups().build();

        String expectedMessage = String.format(MESSAGE_DELETE_CLASS_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.setPerson(personWithClass, editedPerson);

        assertCommandSuccess(deleteClassCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteClassWithAssignments_assignmentsAlsoDeleted() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Add a class with assignments
        Person personWithClassAndAssignments = new PersonBuilder(personToEdit)
                .withClassGroups(VALID_CLASSGROUP_MATH)
                .withAssignments(VALID_CLASSGROUP_MATH, "homework1")
                .build();
        model.setPerson(personToEdit, personWithClassAndAssignments);

        // Verify that the person has the class and assignment before deletion
        assertTrue(personWithClassAndAssignments.getClassGroups().contains(new ClassGroup(VALID_CLASSGROUP_MATH)));
        assertFalse(personWithClassAndAssignments.getAssignments().isEmpty());

        // Delete the class
        Set<ClassGroup> classesToDelete = new HashSet<>();
        classesToDelete.add(new ClassGroup(VALID_CLASSGROUP_MATH));

        DeleteClassDescriptor descriptor = new DeleteClassDescriptor();
        descriptor.setClassGroups(classesToDelete);
        DeleteClassCommand deleteClassCommand = new DeleteClassCommand(INDEX_FIRST_PERSON, descriptor);

        // The edited person should have no classes and no assignments
        Person editedPerson = new PersonBuilder(personWithClassAndAssignments)
                .withClassGroups()
                .build();
        // Clear assignments by building a new person with the base properties
        editedPerson = new Person(
                editedPerson.getName(),
                editedPerson.getPhone(),
                editedPerson.getLevel(),
                editedPerson.getClassGroups(),
                new HashSet<>()
        );

        String expectedMessage = String.format(MESSAGE_DELETE_CLASS_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithClassAndAssignments, editedPerson);

        assertCommandSuccess(deleteClassCommand, model, expectedMessage, expectedModel);

        // Verify that assignments were actually deleted
        Person actualEditedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertTrue(actualEditedPerson.getAssignments().isEmpty());
    }

    @Test
    public void execute_nonExistentClass_throwsCommandException() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<ClassGroup> classesToDelete = new HashSet<>();
        classesToDelete.add(new ClassGroup(VALID_CLASSGROUP_MATH));

        DeleteClassDescriptor descriptor = new DeleteClassDescriptor();
        descriptor.setClassGroups(classesToDelete);
        DeleteClassCommand deleteClassCommand = new DeleteClassCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(MESSAGE_CLASS_NOT_FOUND, VALID_CLASSGROUP_MATH);
        assertCommandFailure(deleteClassCommand, model, expectedMessage);
    }

    @Test
    public void execute_noClassProvided_throwsCommandException() {
        DeleteClassDescriptor descriptor = new DeleteClassDescriptor();
        descriptor.setClassGroups(new HashSet<>());
        DeleteClassCommand deleteClassCommand = new DeleteClassCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(deleteClassCommand, model, MESSAGE_CLASS_NOT_DELETED);
    }

    @Test
    public void execute_nullClassProvided_throwsCommandException() {
        DeleteClassDescriptor descriptor = new DeleteClassDescriptor();
        DeleteClassCommand deleteClassCommand = new DeleteClassCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(deleteClassCommand, model, MESSAGE_CLASS_NOT_DELETED);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<ClassGroup> classesToDelete = new HashSet<>();
        classesToDelete.add(new ClassGroup(VALID_CLASSGROUP_MATH));

        DeleteClassDescriptor descriptor = new DeleteClassDescriptor();
        descriptor.setClassGroups(classesToDelete);
        DeleteClassCommand deleteClassCommand = new DeleteClassCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(deleteClassCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        Set<ClassGroup> classesToDelete = new HashSet<>();
        classesToDelete.add(new ClassGroup(VALID_CLASSGROUP_MATH));

        DeleteClassDescriptor descriptor = new DeleteClassDescriptor();
        descriptor.setClassGroups(classesToDelete);
        DeleteClassCommand deleteClassCommand = new DeleteClassCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(deleteClassCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<ClassGroup> mathClass = new HashSet<>();
        mathClass.add(new ClassGroup(VALID_CLASSGROUP_MATH));
        Set<ClassGroup> physicsClass = new HashSet<>();
        physicsClass.add(new ClassGroup(VALID_CLASSGROUP_PHYSICS));

        DeleteClassDescriptor mathDescriptor = new DeleteClassDescriptor();
        mathDescriptor.setClassGroups(mathClass);
        DeleteClassDescriptor physicsDescriptor = new DeleteClassDescriptor();
        physicsDescriptor.setClassGroups(physicsClass);

        DeleteClassCommand deleteMathFromFirstCommand = new DeleteClassCommand(INDEX_FIRST_PERSON, mathDescriptor);
        DeleteClassCommand deleteMathFromSecondCommand = new DeleteClassCommand(INDEX_SECOND_PERSON, mathDescriptor);
        DeleteClassCommand deletePhysicsFromFirstCommand = new DeleteClassCommand(INDEX_FIRST_PERSON,
                physicsDescriptor);

        // same object -> returns true
        assertTrue(deleteMathFromFirstCommand.equals(deleteMathFromFirstCommand));

        // same values -> returns true
        DeleteClassCommand deleteMathFromFirstCommandCopy = new DeleteClassCommand(INDEX_FIRST_PERSON, mathDescriptor);
        assertTrue(deleteMathFromFirstCommand.equals(deleteMathFromFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteMathFromFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteMathFromFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteMathFromFirstCommand.equals(deleteMathFromSecondCommand));

        // different class -> returns false
        assertFalse(deleteMathFromFirstCommand.equals(deletePhysicsFromFirstCommand));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        DeleteClassDescriptor descriptor = new DeleteClassDescriptor();
        Set<ClassGroup> classesToDelete = new HashSet<>();
        classesToDelete.add(new ClassGroup(VALID_CLASSGROUP_MATH));
        descriptor.setClassGroups(classesToDelete);
        DeleteClassCommand deleteClassCommand = new DeleteClassCommand(index, descriptor);
        String expected = DeleteClassCommand.class.getCanonicalName() + "{index=" + index
                + ", deleteClassDescriptor=" + descriptor + "}";
        assertEquals(expected, deleteClassCommand.toString());
    }
}

