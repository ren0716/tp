package seedu.address.model.versionedaddressbook;

import java.util.Stack;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;

/**
 * Tracks the history of address book states to support undo and redo operations.
 * Version history is reset when the application is closed and does not persist across sessions.
 */
public class VersionedAddressBook extends AddressBook {
    private final Stack<ReadOnlyAddressBook> undoStack;
    private final Stack<ReadOnlyAddressBook> redoStack;

    /**
     * Creates a VersionedAddressBook with the given initial address book data.
     * The initial data becomes the first state in the version history.
     *
     * @param initialData The initial address book state to track.
     */
    public VersionedAddressBook(ReadOnlyAddressBook initialData) {
        this.undoStack = new Stack<>();
        this.undoStack.add(initialData);
        this.redoStack = new Stack<>();
    }

    /**
     * Saves the current address book state to version history.
     * Clears any redo history when a new commit is made.
     *
     * @param updated The updated address book state to save.
     */
    public void commit(ReadOnlyAddressBook updated) {
        this.undoStack.add(updated);
        this.redoStack.clear();
    }

    /**
     * Reverts to the previous address book state.
     * The current state is saved to redo history.
     *
     * @return The previous address book state.
     * @throws NoPreviousCommitException If no previous version exists in the current session.
     */
    public ReadOnlyAddressBook undo() throws NoPreviousCommitException {
        if (this.undoStack.size() <= 1) {
            throw new NoPreviousCommitException();
        }
        this.redoStack.add(undoStack.peek());
        this.undoStack.pop();
        return undoStack.peek();
    }

    /**
     * Restores the address book state that was undone by the most recent undo command.
     * Only available immediately after an undo operation.
     *
     * @return The restored address book state.
     * @throws NoPreviousUndoException If no undone version exists in the current session.
     */
    public ReadOnlyAddressBook redo() throws NoPreviousUndoException {
        if (this.redoStack.isEmpty()) {
            throw new NoPreviousUndoException();
        }
        ReadOnlyAddressBook targetVersion = this.redoStack.pop();
        this.undoStack.add(targetVersion);
        return targetVersion;
    }

    /**
     * Returns the stack containing the undo history.
     *
     * @return The undo stack.
     */
    public Stack<ReadOnlyAddressBook> getUndoStack() {
        return undoStack;
    }

    /**
     * Returns the stack containing the redo history.
     *
     * @return The redo stack.
     */
    public Stack<ReadOnlyAddressBook> getRedoStack() {
        return redoStack;
    }
}
