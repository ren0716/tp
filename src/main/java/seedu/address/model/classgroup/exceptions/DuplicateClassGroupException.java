package seedu.address.model.classgroup.exceptions;

public class DuplicateClassGroupException extends RuntimeException {
    public DuplicateClassGroupException() {
        super("Operation will result in duplicate class groups");
    }
}
