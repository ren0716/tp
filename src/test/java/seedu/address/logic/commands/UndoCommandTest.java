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
import seedu.address.model.versionedaddressbook.NoPreviousCommitException;
import seedu.address.testutil.PersonBuilder;

public class UndoCommandTest {

    @Test
    public void execute_undoAfterCommand_success() {
        // Set up model with a command that can be undone
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Simulate a change by committing the new state
        model.addPerson(new PersonBuilder().build());

        // Undo should revert to the previous state
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_noPreviousCommit_throwsCommandException() {
        // Fresh model with no previous commits
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        assertCommandFailure(new UndoCommand(), model, new NoPreviousCommitException().getMessage());
    }

    @Test
    public void execute_undoMultipleTimes_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedSecondUndo = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedFirstUndo = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedFirstUndo.addPerson(new PersonBuilder().build());


        // Make first change
        model.addPerson(new PersonBuilder().build());

        // Make second change
        Person personToDelete = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        model.deletePerson(personToDelete);

        // first and second undo should work
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedFirstUndo);
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedSecondUndo);

    }
}
