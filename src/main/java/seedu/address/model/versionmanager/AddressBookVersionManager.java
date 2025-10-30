package seedu.address.model.versionmanager;

import java.util.Stack;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.versionmanager.exceptions.NoPreviousCommitException;
import seedu.address.model.versionmanager.exceptions.NoPreviousUndoException;

/**
 * A {@code AddressBookVersionManager} maintains a history of {@link ReadOnlyAddressBook} states
 * to support undo and redo operations within a single application session.
 * <p>
 * Version history is <b>not persisted</b> between sessions; it is reset when the application closes.
 */
public class AddressBookVersionManager {

    private final Stack<ReadOnlyAddressBook> versionStack;
    private final Stack<ReadOnlyAddressBook> redoStack;

    /**
     * Constructs a {@code AddressBookVersionManager} with the given initial address book data.
     * The initial state is recorded as the first version in the history.
     *
     * @param initialData the initial {@link ReadOnlyAddressBook} state to track
     */
    public AddressBookVersionManager(ReadOnlyAddressBook initialData) {
        this.versionStack = new Stack<>();
        this.versionStack.add(initialData);
        this.redoStack = new Stack<>();
    }

    /**
     * Commits the specified address book state as a new version in the history.
     * <p>
     * Clears any redo history when a new commit is made, as redo states are only
     * valid immediately after an undo.
     *
     * @param updated the new {@link ReadOnlyAddressBook} state to record
     */
    public void commit(ReadOnlyAddressBook updated) {
        this.versionStack.add(updated);
        this.redoStack.clear();
    }

    /**
     * Reverts the address book to its previous committed state.
     * <p>
     * The current state is pushed to the redo stack, allowing it to be restored later via {@link #redo()}.
     *
     * @return the previous {@link ReadOnlyAddressBook} state
     * @throws NoPreviousCommitException if there is no earlier version to revert to
     */
    public ReadOnlyAddressBook undo() throws NoPreviousCommitException {
        if (this.versionStack.size() <= 1) {
            throw new NoPreviousCommitException();
        }
        this.redoStack.add(versionStack.peek());
        this.versionStack.pop();
        return versionStack.peek();
    }

    /**
     * Restores the most recently undone address book state.
     * <p>
     * This operation can only be performed immediately after an {@link #undo()} call.
     *
     * @return the restored {@link ReadOnlyAddressBook} state
     * @throws NoPreviousUndoException if there is no undone version to restore
     */
    public ReadOnlyAddressBook redo() throws NoPreviousUndoException {
        if (this.redoStack.isEmpty()) {
            throw new NoPreviousUndoException();
        }
        ReadOnlyAddressBook targetVersion = this.redoStack.pop();
        this.versionStack.add(targetVersion);
        return targetVersion;
    }

    /**
     * Returns the stack containing all committed address book versions.
     *
     * @return the version history stack
     */
    public Stack<ReadOnlyAddressBook> getVersionStack() {
        return versionStack;
    }

    /**
     * Returns the stack containing address book states that can be redone.
     *
     * @return the redo history stack
     */
    public Stack<ReadOnlyAddressBook> getRedoStack() {
        return redoStack;
    }

}
