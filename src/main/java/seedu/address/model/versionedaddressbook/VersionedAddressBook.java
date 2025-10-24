package seedu.address.model.versionedaddressbook;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;

import java.util.Stack;

public class VersionedAddressBook extends AddressBook {
    Stack<ReadOnlyAddressBook> undoStack;

    public VersionedAddressBook(ReadOnlyAddressBook initialData) {
        this.undoStack = new Stack<>();
        this.undoStack.add(initialData);
    }

    public void commit(ReadOnlyAddressBook updated) {
        this.undoStack.add(updated);
    }

    public ReadOnlyAddressBook undo() throws NoPreviousCommitException{
        if (this.undoStack.size() <= 1) {
            throw new NoPreviousCommitException();
        }
        this.undoStack.pop();
        return undoStack.peek();
    }
}
