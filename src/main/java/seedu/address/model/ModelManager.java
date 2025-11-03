package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.versionmanager.AddressBookVersionManager;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final ObservableList<Person> visiblePersons;
    private final AddressBookVersionManager versions;
    private final CommandHistory history = new CommandHistory();
    private Predicate<Person> currentPredicate = PREDICATE_SHOW_ALL_PERSONS;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.versions = new AddressBookVersionManager(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.visiblePersons = FXCollections.observableArrayList(this.addressBook.getPersonList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
        updateVisiblePersonList();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public boolean hasName(Name name) {
        requireNonNull(name);
        return addressBook.hasName(name);
    }

    @Override
    public boolean hasPhone(Phone phone) {
        requireNonNull(phone);
        return addressBook.hasPhone(phone);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
        visiblePersons.remove(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        if (!visiblePersons.contains(person)) {
            visiblePersons.add(person);
        }
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
        int index = visiblePersons.indexOf(target);
        if (index != -1) {
            visiblePersons.set(index, editedPerson);
        }
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the visible person list.
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(visiblePersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        currentPredicate = predicate;
        if (predicate == PREDICATE_SHOW_ALL_PERSONS) {
            visiblePersons.setAll(addressBook.getPersonList());
        } else {
            visiblePersons.removeIf(person -> !predicate.test(person));
        }
    }

    private void updateVisiblePersonList() {
        if (currentPredicate == PREDICATE_SHOW_ALL_PERSONS) {
            visiblePersons.setAll(addressBook.getPersonList());
        } else {
            visiblePersons.clear();
            for (Person person : addressBook.getPersonList()) {
                if (currentPredicate.test(person)) {
                    visiblePersons.add(person);
                }
            }
        }
    }

    @Override
    public Predicate<Person> getCurrentPredicate() {
        return currentPredicate;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && visiblePersons.equals(otherModelManager.visiblePersons);
    }

    //=========== AddressBookVersionManager =======================================================================
    @Override
    public void undo() {
        ReadOnlyAddressBook previous = this.versions.undo();
        setAddressBook(previous);
    }

    @Override
    public void commit() {
        this.versions.commit(new AddressBook(getAddressBook()));
    }

    @Override
    public void redo() {
        ReadOnlyAddressBook next = this.versions.redo();
        setAddressBook(next);
    }

    //=========== Command History ============================================================================
    @Override
    public void setCommandHistory(CommandHistory commandHistory) {
        this.history.resetHistory(commandHistory);
    }

    public CommandHistory getHistory() {
        return this.history;
    }

    @Override
    public void addCommandToHistory(String command) {
        history.add(command);
    }

    @Override
    public String nextCommand() {
        return history.next();
    }

    @Override
    public String previousCommand() {
        return history.previous();
    }
}
