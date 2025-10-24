package seedu.address.model.versionedaddressbook;

/**
 * Thrown when an undo operation is attempted but no previous version exists
 * in the current session's history.
 */
public class NoPreviousCommitException extends RuntimeException {

    public NoPreviousCommitException() {
        super("No previous version available to undo in the current session");
    }
}
