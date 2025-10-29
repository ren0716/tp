package seedu.address.model.versionmanager;

/**
 * Thrown when a redo operation is attempted but no undone version exists
 * in the current session's history.
 */
public class NoPreviousUndoException extends RuntimeException {

    public NoPreviousUndoException() {
        super("No undone version available to redo in the current session");
    }
}
