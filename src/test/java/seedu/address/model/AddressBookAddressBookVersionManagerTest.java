package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.versionmanager.AddressBookVersionManager;
import seedu.address.model.versionmanager.NoPreviousCommitException;
import seedu.address.model.versionmanager.NoPreviousUndoException;
import seedu.address.testutil.AddressBookBuilder;

public class AddressBookAddressBookVersionManagerTest {

    private ReadOnlyAddressBook addressBookState1;
    private ReadOnlyAddressBook addressBookState2;
    private ReadOnlyAddressBook addressBookState3;
    private AddressBookVersionManager addressBookVersionManager;

    @BeforeEach
    public void setUp() {
        // Create different address book states
        addressBookState1 = new AddressBookBuilder().withPerson(ALICE).build();
        addressBookState2 = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        addressBookState3 = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON)
                .withPerson(CARL).build();

        addressBookVersionManager = new AddressBookVersionManager(addressBookState1);
    }

    @Test
    public void constructor() {
        assertEquals(1, addressBookVersionManager.getVersionStack().size());
        assertEquals(0, addressBookVersionManager.getRedoStack().size());
        assertEquals(addressBookState1, addressBookVersionManager.getVersionStack().peek());
    }

    @Test
    public void commit_validState_success() {
        addressBookVersionManager.commit(addressBookState2);

        assertEquals(2, addressBookVersionManager.getVersionStack().size());
        assertEquals(addressBookState2, addressBookVersionManager.getVersionStack().peek());
        assertEquals(0, addressBookVersionManager.getRedoStack().size());
    }

    @Test
    public void commit_afterUndo_clearsRedoStack() {
        addressBookVersionManager.commit(addressBookState2);
        addressBookVersionManager.undo();

        // Verify redo stack has content before commit
        assertEquals(1, addressBookVersionManager.getRedoStack().size());

        // Commit should clear redo stack
        addressBookVersionManager.commit(addressBookState3);

        assertEquals(0, addressBookVersionManager.getRedoStack().size());
        assertEquals(2, addressBookVersionManager.getVersionStack().size());
        assertEquals(addressBookState3, addressBookVersionManager.getVersionStack().peek());
    }

    @Test
    public void undo_onlyInitialState_throwsNoPreviousCommitException() {
        assertThrows(NoPreviousCommitException.class, () -> addressBookVersionManager.undo());
    }

    @Test
    public void undo_validUndo_success() {
        addressBookVersionManager.commit(addressBookState2);
        addressBookVersionManager.commit(addressBookState3);

        ReadOnlyAddressBook result = addressBookVersionManager.undo();

        assertEquals(addressBookState2, result);
        assertEquals(2, addressBookVersionManager.getVersionStack().size());
        assertEquals(1, addressBookVersionManager.getRedoStack().size());
        assertEquals(addressBookState3, addressBookVersionManager.getRedoStack().peek());
    }

    @Test
    public void undo_multipleUndos_success() {
        addressBookVersionManager.commit(addressBookState2);
        addressBookVersionManager.commit(addressBookState3);

        // First undo
        ReadOnlyAddressBook result1 = addressBookVersionManager.undo();
        assertEquals(addressBookState2, result1);
        assertEquals(2, addressBookVersionManager.getVersionStack().size());

        // Second undo
        ReadOnlyAddressBook result2 = addressBookVersionManager.undo();
        assertEquals(addressBookState1, result2);
        assertEquals(1, addressBookVersionManager.getVersionStack().size());
        assertEquals(2, addressBookVersionManager.getRedoStack().size());
    }

    @Test
    public void undo_atInitialState_throwsNoPreviousCommitException() {
        addressBookVersionManager.commit(addressBookState2);
        addressBookVersionManager.undo();

        // Trying to undo past initial state
        assertThrows(NoPreviousCommitException.class, () -> addressBookVersionManager.undo());
    }

    @Test
    public void redo_noUndoPerformed_throwsNoPreviousUndoException() {
        addressBookVersionManager.commit(addressBookState2);

        assertThrows(NoPreviousUndoException.class, () -> addressBookVersionManager.redo());
    }

    @Test
    public void redo_validRedo_success() {
        addressBookVersionManager.commit(addressBookState2);
        addressBookVersionManager.undo();

        ReadOnlyAddressBook result = addressBookVersionManager.redo();

        assertEquals(addressBookState2, result);
        assertEquals(2, addressBookVersionManager.getVersionStack().size());
        assertEquals(0, addressBookVersionManager.getRedoStack().size());
    }

    @Test
    public void redo_multipleRedos_success() {
        addressBookVersionManager.commit(addressBookState2);
        addressBookVersionManager.commit(addressBookState3);
        addressBookVersionManager.undo();
        addressBookVersionManager.undo();

        // First redo
        ReadOnlyAddressBook result1 = addressBookVersionManager.redo();
        assertEquals(addressBookState2, result1);
        assertEquals(2, addressBookVersionManager.getVersionStack().size());

        // Second redo
        ReadOnlyAddressBook result2 = addressBookVersionManager.redo();
        assertEquals(addressBookState3, result2);
        assertEquals(3, addressBookVersionManager.getVersionStack().size());
        assertEquals(0, addressBookVersionManager.getRedoStack().size());
    }

    @Test
    public void redo_afterAllRedos_throwsNoPreviousUndoException() {
        addressBookVersionManager.commit(addressBookState2);
        addressBookVersionManager.undo();
        addressBookVersionManager.redo();

        assertThrows(NoPreviousUndoException.class, () -> addressBookVersionManager.redo());
    }

    @Test
    public void getUndoStack_modifyStack_affectsOriginalStack() {
        // Unlike ModelManager's filtered list which is unmodifiable,
        // the stack is directly returned and can be modified
        addressBookVersionManager.commit(addressBookState2);

        int originalSize = addressBookVersionManager.getVersionStack().size();
        addressBookVersionManager.getVersionStack().push(addressBookState3);

        assertEquals(originalSize + 1, addressBookVersionManager.getVersionStack().size());
    }

    @Test
    public void getRedoStack_modifyStack_affectsOriginalStack() {
        addressBookVersionManager.commit(addressBookState2);
        addressBookVersionManager.undo();

        int originalSize = addressBookVersionManager.getRedoStack().size();
        addressBookVersionManager.getRedoStack().push(addressBookState3);

        assertEquals(originalSize + 1, addressBookVersionManager.getRedoStack().size());
    }

    @Test
    public void undoRedo_complexSequence_success() {
        // Commit multiple states
        addressBookVersionManager.commit(addressBookState2);
        addressBookVersionManager.commit(addressBookState3);

        // Undo twice
        addressBookVersionManager.undo();
        addressBookVersionManager.undo();
        assertEquals(addressBookState1, addressBookVersionManager.getVersionStack().peek());

        // Redo once
        ReadOnlyAddressBook redoResult = addressBookVersionManager.redo();
        assertEquals(addressBookState2, redoResult);

        // Commit new state (should clear remaining redo)
        ReadOnlyAddressBook newState = new AddressBookBuilder().withPerson(ALICE).build();
        addressBookVersionManager.commit(newState);

        assertEquals(0, addressBookVersionManager.getRedoStack().size());
        assertThrows(NoPreviousUndoException.class, () -> addressBookVersionManager.redo());
    }

}
