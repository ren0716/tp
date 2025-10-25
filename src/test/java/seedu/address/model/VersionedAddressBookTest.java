package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.versionedaddressbook.NoPreviousCommitException;
import seedu.address.model.versionedaddressbook.NoPreviousUndoException;
import seedu.address.model.versionedaddressbook.VersionedAddressBook;
import seedu.address.testutil.AddressBookBuilder;

public class VersionedAddressBookTest {

    private ReadOnlyAddressBook addressBookState1;
    private ReadOnlyAddressBook addressBookState2;
    private ReadOnlyAddressBook addressBookState3;
    private VersionedAddressBook versionedAddressBook;

    @BeforeEach
    public void setUp() {
        // Create different address book states
        addressBookState1 = new AddressBookBuilder().withPerson(ALICE).build();
        addressBookState2 = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        addressBookState3 = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON)
                .withPerson(CARL).build();

        versionedAddressBook = new VersionedAddressBook(addressBookState1);
    }

    @Test
    public void constructor() {
        assertEquals(1, versionedAddressBook.getVersionStack().size());
        assertEquals(0, versionedAddressBook.getRedoStack().size());
        assertEquals(addressBookState1, versionedAddressBook.getVersionStack().peek());
    }

    @Test
    public void commit_validState_success() {
        versionedAddressBook.commit(addressBookState2);

        assertEquals(2, versionedAddressBook.getVersionStack().size());
        assertEquals(addressBookState2, versionedAddressBook.getVersionStack().peek());
        assertEquals(0, versionedAddressBook.getRedoStack().size());
    }

    @Test
    public void commit_afterUndo_clearsRedoStack() {
        versionedAddressBook.commit(addressBookState2);
        versionedAddressBook.undo();

        // Verify redo stack has content before commit
        assertEquals(1, versionedAddressBook.getRedoStack().size());

        // Commit should clear redo stack
        versionedAddressBook.commit(addressBookState3);

        assertEquals(0, versionedAddressBook.getRedoStack().size());
        assertEquals(2, versionedAddressBook.getVersionStack().size());
        assertEquals(addressBookState3, versionedAddressBook.getVersionStack().peek());
    }

    @Test
    public void undo_onlyInitialState_throwsNoPreviousCommitException() {
        assertThrows(NoPreviousCommitException.class, () -> versionedAddressBook.undo());
    }

    @Test
    public void undo_validUndo_success() {
        versionedAddressBook.commit(addressBookState2);
        versionedAddressBook.commit(addressBookState3);

        ReadOnlyAddressBook result = versionedAddressBook.undo();

        assertEquals(addressBookState2, result);
        assertEquals(2, versionedAddressBook.getVersionStack().size());
        assertEquals(1, versionedAddressBook.getRedoStack().size());
        assertEquals(addressBookState3, versionedAddressBook.getRedoStack().peek());
    }

    @Test
    public void undo_multipleUndos_success() {
        versionedAddressBook.commit(addressBookState2);
        versionedAddressBook.commit(addressBookState3);

        // First undo
        ReadOnlyAddressBook result1 = versionedAddressBook.undo();
        assertEquals(addressBookState2, result1);
        assertEquals(2, versionedAddressBook.getVersionStack().size());

        // Second undo
        ReadOnlyAddressBook result2 = versionedAddressBook.undo();
        assertEquals(addressBookState1, result2);
        assertEquals(1, versionedAddressBook.getVersionStack().size());
        assertEquals(2, versionedAddressBook.getRedoStack().size());
    }

    @Test
    public void undo_atInitialState_throwsNoPreviousCommitException() {
        versionedAddressBook.commit(addressBookState2);
        versionedAddressBook.undo();

        // Trying to undo past initial state
        assertThrows(NoPreviousCommitException.class, () -> versionedAddressBook.undo());
    }

    @Test
    public void redo_noUndoPerformed_throwsNoPreviousUndoException() {
        versionedAddressBook.commit(addressBookState2);

        assertThrows(NoPreviousUndoException.class, () -> versionedAddressBook.redo());
    }

    @Test
    public void redo_validRedo_success() {
        versionedAddressBook.commit(addressBookState2);
        versionedAddressBook.undo();

        ReadOnlyAddressBook result = versionedAddressBook.redo();

        assertEquals(addressBookState2, result);
        assertEquals(2, versionedAddressBook.getVersionStack().size());
        assertEquals(0, versionedAddressBook.getRedoStack().size());
    }

    @Test
    public void redo_multipleRedos_success() {
        versionedAddressBook.commit(addressBookState2);
        versionedAddressBook.commit(addressBookState3);
        versionedAddressBook.undo();
        versionedAddressBook.undo();

        // First redo
        ReadOnlyAddressBook result1 = versionedAddressBook.redo();
        assertEquals(addressBookState2, result1);
        assertEquals(2, versionedAddressBook.getVersionStack().size());

        // Second redo
        ReadOnlyAddressBook result2 = versionedAddressBook.redo();
        assertEquals(addressBookState3, result2);
        assertEquals(3, versionedAddressBook.getVersionStack().size());
        assertEquals(0, versionedAddressBook.getRedoStack().size());
    }

    @Test
    public void redo_afterAllRedos_throwsNoPreviousUndoException() {
        versionedAddressBook.commit(addressBookState2);
        versionedAddressBook.undo();
        versionedAddressBook.redo();

        assertThrows(NoPreviousUndoException.class, () -> versionedAddressBook.redo());
    }

    @Test
    public void getUndoStack_modifyStack_affectsOriginalStack() {
        // Unlike ModelManager's filtered list which is unmodifiable,
        // the stack is directly returned and can be modified
        versionedAddressBook.commit(addressBookState2);

        int originalSize = versionedAddressBook.getVersionStack().size();
        versionedAddressBook.getVersionStack().push(addressBookState3);

        assertEquals(originalSize + 1, versionedAddressBook.getVersionStack().size());
    }

    @Test
    public void getRedoStack_modifyStack_affectsOriginalStack() {
        versionedAddressBook.commit(addressBookState2);
        versionedAddressBook.undo();

        int originalSize = versionedAddressBook.getRedoStack().size();
        versionedAddressBook.getRedoStack().push(addressBookState3);

        assertEquals(originalSize + 1, versionedAddressBook.getRedoStack().size());
    }

    @Test
    public void undoRedo_complexSequence_success() {
        // Commit multiple states
        versionedAddressBook.commit(addressBookState2);
        versionedAddressBook.commit(addressBookState3);

        // Undo twice
        versionedAddressBook.undo();
        versionedAddressBook.undo();
        assertEquals(addressBookState1, versionedAddressBook.getVersionStack().peek());

        // Redo once
        ReadOnlyAddressBook redoResult = versionedAddressBook.redo();
        assertEquals(addressBookState2, redoResult);

        // Commit new state (should clear remaining redo)
        ReadOnlyAddressBook newState = new AddressBookBuilder().withPerson(ALICE).build();
        versionedAddressBook.commit(newState);

        assertEquals(0, versionedAddressBook.getRedoStack().size());
        assertThrows(NoPreviousUndoException.class, () -> versionedAddressBook.redo());
    }

}
