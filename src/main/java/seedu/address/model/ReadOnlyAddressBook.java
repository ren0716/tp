package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.classgroup.ClassGroup;
import seedu.address.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the classgroups list.
     * This list will not contain any duplicate classgroups.
     */
    ObservableList<ClassGroup> getClassGroupList();

}
