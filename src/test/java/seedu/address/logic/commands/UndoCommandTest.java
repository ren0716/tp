package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.versionmanager.NoPreviousCommitException;
import seedu.address.testutil.PersonBuilder;

public class UndoCommandTest {

    @Test
    public void execute_undoAfterCommand_success() {
        // Set up model with a command that can be undone
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Simulate a change by committing the new state
        model.addPerson(new PersonBuilder().build());
        model.commit();

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
        model.commit();

        // Make second change
        Person personToDelete = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        model.deletePerson(personToDelete);
        model.commit();

        // first and second undo should work
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedFirstUndo);
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedSecondUndo);

    }

    @Test
    public void execute_undoNonMutatingCommand_throwsCommandExeception() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        //execute a non-mutating command on model
        HelpCommand nonMutatingCommand = new HelpCommand();
        nonMutatingCommand.execute(model);

        // undo command should not work
        assertCommandFailure(new UndoCommand(), model, new NoPreviousCommitException().getMessage());
    }

    @Test
    public void execute_undoWithMixedCommand_success() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Execute a mutating command (e.g., AddCommand)
        Person newPerson = new PersonBuilder().withName("Alice").build();
        AddCommand addCommand = new AddCommand(newPerson);
        addCommand.execute(model);
        model.commit();

        // Execute multiple non-mutating command (e.g., HelpCommand)
        HelpCommand helpCommand = new HelpCommand();
        helpCommand.execute(model);

        ListCommand listCommand = new ListCommand();
        listCommand.execute(model);

        // Undo should undo the last mutating command
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.execute(model);

        // Check that the added person was removed
        assertFalse(model.hasPerson(newPerson));

    }
}
