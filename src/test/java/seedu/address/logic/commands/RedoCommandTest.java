package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.versionedaddressbook.NoPreviousUndoException;
import seedu.address.testutil.PersonBuilder;

public class RedoCommandTest {

    @Test
    public void execute_redoAfterUndo_success() {
        // Set up model with a command that can be redone
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Make a change
        Person personToAdd = new PersonBuilder().build();
        model.addPerson(personToAdd);
        expectedModel.addPerson(personToAdd);

        // Undo the change
        model.undo();

        // Redo should restore the change
        assertCommandSuccess(new RedoCommand(), model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_noPreviousUndo_throwsCommandException() {
        // Fresh model with no undo commands to redo
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        assertCommandFailure(new RedoCommand(), model, new NoPreviousUndoException().getMessage());
    }

    @Test
    public void execute_redoAfterNewCommand_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Make a change and undo it
        model.addPerson(new PersonBuilder().build());
        model.undo();

        // Make a new change (this should clear redo history)
        model.addPerson(new PersonBuilder().withName("Alice").build());

        // Redo should fail because redo history was cleared
        assertCommandFailure(new RedoCommand(), model, new NoPreviousUndoException().getMessage());
    }

    @Test
    public void execute_redoMultipleTimes_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedAfterFirstRedo = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedAfterSecondRedo = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Person personToAdd = new PersonBuilder().build();

        // Make first change
        model.addPerson(personToAdd);
        expectedAfterFirstRedo.addPerson(personToAdd);
        expectedAfterSecondRedo.addPerson(personToAdd);

        // Make second change
        Person personToDelete = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        model.deletePerson(personToDelete);
        expectedAfterSecondRedo.deletePerson(
                expectedAfterSecondRedo.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased())
        );

        // Undo both changes
        model.undo();
        model.undo();

        // First and second redo should work
        assertCommandSuccess(new RedoCommand(), model, RedoCommand.MESSAGE_SUCCESS, expectedAfterFirstRedo);
        assertCommandSuccess(new RedoCommand(), model, RedoCommand.MESSAGE_SUCCESS, expectedAfterSecondRedo);
    }
}
