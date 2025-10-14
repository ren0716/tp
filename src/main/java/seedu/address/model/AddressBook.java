package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.classgroup.ClassGroup;
import seedu.address.model.classgroup.UniqueClassGroupList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level.
 * Duplicates are not allowed (by .isSamePerson / .isSameClassGroup comparison).
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueClassGroupList classGroups;

    /*
     * Non-static initialization block to avoid duplication between constructors.
     */
    {
        persons = new UniquePersonList();
        classGroups = new UniqueClassGroupList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the data in the {@code toBeCopied}.
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    // ======================== Data Overwrite Methods ========================

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the class group list with {@code classGroups}.
     * {@code classGroups} must not contain duplicate class groups.
     */
    public void setClassGroups(List<ClassGroup> classGroups) {
        this.classGroups.setClassGroups(classGroups);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setPersons(newData.getPersonList());
        setClassGroups(newData.getClassGroupList());
    }

    // ======================== Person Operations ========================

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);
        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    // ======================== ClassGroup Operations ========================

    /**
     * Returns true if a class group with the same identity as {@code classGroup} exists in the address book.
     */
    public boolean hasClassGroup(ClassGroup classGroup) {
        requireNonNull(classGroup);
        return classGroups.contains(classGroup);
    }

    /**
     * Adds a class group to the address book.
     * The class group must not already exist in the address book.
     */
    public void addClassGroup(ClassGroup classGroup) {
        classGroups.add(classGroup);
    }

    /**
     * Replaces the given class group {@code target} in the list with {@code editedClassGroup}.
     * {@code target} must exist in the address book.
     * The identity of {@code editedClassGroup} must not be the same as another existing class group.
     */
    public void setClassGroup(ClassGroup target, ClassGroup editedClassGroup) {
        requireNonNull(editedClassGroup);
        classGroups.setClassGroup(target, editedClassGroup);
    }

    /**
     * Removes {@code classGroup} from this {@code AddressBook}.
     * {@code classGroup} must exist in the address book.
     */
    public void deleteClassGroup(ClassGroup classGroup) {
        classGroups.remove(classGroup);
    }

    // ======================== Getters ========================

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<ClassGroup> getClassGroupList() {
        return classGroups.asUnmodifiableObservableList();
    }

    // ======================== Utility ========================

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("classGroups", classGroups)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof AddressBook)) return false;

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons)
                && classGroups.equals(otherAddressBook.classGroups);
    }

    @Override
    public int hashCode() {
        return persons.hashCode() + classGroups.hashCode();
    }
}

