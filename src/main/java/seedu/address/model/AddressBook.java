package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.classgroup.ClassGroup;
import seedu.address.model.classgroup.UniqueClassGroupList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueClassGroupList classGroups;

    {
        persons = new UniquePersonList();
        classGroups = new UniqueClassGroupList();
    }

    public AddressBook() {}

    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    // ========== List overwrite operations ==========

    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    public void setClassGroups(List<ClassGroup> classGroups) {
        this.classGroups.setClassGroups(classGroups);
    }

    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setPersons(newData.getPersonList());
        setClassGroups(newData.getClassGroupList());
    }

    // ========== Person-level operations ==========

    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    public void addPerson(Person p) {
        persons.add(p);
    }

    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);
        persons.setPerson(target, editedPerson);
    }

    public void removePerson(Person key) {
        persons.remove(key);
    }

    // ========== ClassGroup-level operations ==========

    public boolean hasClassGroup(ClassGroup classGroup) {
        requireNonNull(classGroup);
        return classGroups.contains(classGroup);
    }

    public void addClassGroup(ClassGroup classGroup) {
        classGroups.add(classGroup);
    }

    public void deleteClassGroup(ClassGroup classGroup) {
        classGroups.remove(classGroup);
    }

    public void setClassGroup(ClassGroup target, ClassGroup editedClassGroup) {
        requireNonNull(editedClassGroup);
        classGroups.setClassGroup(target, editedClassGroup);
    }

    // ========== Getters ==========

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<ClassGroup> getClassGroupList() {
        return classGroups.asUnmodifiableObservableList();
    }

    // ========== Misc ==========

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof AddressBook)) return false;

        AddressBook otherBook = (AddressBook) other;
        return persons.equals(otherBook.persons)
                && classGroups.equals(otherBook.classGroups);
    }

    @Override
    public int hashCode() {
        return persons.hashCode() + classGroups.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("classGroups", classGroups)
                .toString();
    }
}

