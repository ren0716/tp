package seedu.address.model.versionedaddressbook;

public class NoPreviousCommitException extends RuntimeException {
    public NoPreviousCommitException() {
        super("No prior version in the current session");
    }
}
