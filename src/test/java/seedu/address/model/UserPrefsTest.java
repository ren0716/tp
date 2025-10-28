package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;

/**
 * Unit tests for {@link UserPrefs}.
 */
public class UserPrefsTest {

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        UserPrefs userPref = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPref.setGuiSettings(null));
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPrefs.setAddressBookFilePath(null));
    }

    @Test
    public void setCommandHistoryFilePath_nullPath_throwsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPrefs.setCommandHistoryFilePath(null));
    }

    @Test
    public void getAndSetGuiSettings_success() {
        UserPrefs userPrefs = new UserPrefs();
        GuiSettings newGui = new GuiSettings(800, 600, 200, 100);
        userPrefs.setGuiSettings(newGui);
        assertEquals(newGui, userPrefs.getGuiSettings());
    }

    @Test
    public void getAndSetAddressBookFilePath_success() {
        UserPrefs userPrefs = new UserPrefs();
        Path newPath = Paths.get("data", "newAddressBook.json");
        userPrefs.setAddressBookFilePath(newPath);
        assertEquals(newPath, userPrefs.getAddressBookFilePath());
    }

    @Test
    public void getAndSetCommandHistoryFilePath_success() {
        UserPrefs userPrefs = new UserPrefs();
        Path newPath = Paths.get("data", "history2.txt");
        userPrefs.setCommandHistoryFilePath(newPath);
        assertEquals(newPath, userPrefs.getCommandHistoryFilePath());
    }

    @Test
    public void resetData_validUserPrefs_success() {
        UserPrefs userPrefs = new UserPrefs();
        UserPrefs newPrefs = new UserPrefs();
        GuiSettings newGui = new GuiSettings(1200, 900, 100, 50);
        Path newAddressPath = Paths.get("data", "altAddressBook.json");
        Path newHistoryPath = Paths.get("data", "altHistory.txt");

        newPrefs.setGuiSettings(newGui);
        newPrefs.setAddressBookFilePath(newAddressPath);
        newPrefs.setCommandHistoryFilePath(newHistoryPath);

        // resetData only copies GUI + addressBook path (not history)
        userPrefs.resetData(newPrefs);

        assertEquals(newGui, userPrefs.getGuiSettings());
        assertEquals(newAddressPath, userPrefs.getAddressBookFilePath());
        // commandHistoryFilePath remains default
        assertNotEquals(newHistoryPath, userPrefs.getCommandHistoryFilePath());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        UserPrefs prefs = new UserPrefs();
        assertEquals(prefs, prefs);
    }

    @Test
    public void equals_differentType_returnsFalse() {
        UserPrefs prefs = new UserPrefs();
        assertNotEquals(prefs, "string");
    }

    @Test
    public void equals_differentValues_returnsFalse() {
        UserPrefs prefs1 = new UserPrefs();
        UserPrefs prefs2 = new UserPrefs();
        prefs2.setAddressBookFilePath(Paths.get("different", "file.json"));
        assertNotEquals(prefs1, prefs2);
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        UserPrefs prefs1 = new UserPrefs();
        UserPrefs prefs2 = new UserPrefs();
        assertEquals(prefs1, prefs2);
    }

    @Test
    public void hashCode_sameValues_returnsSameHash() {
        UserPrefs prefs1 = new UserPrefs();
        UserPrefs prefs2 = new UserPrefs();
        assertEquals(prefs1.hashCode(), prefs2.hashCode());
    }

    @Test
    public void toString_containsKeyInformation() {
        UserPrefs prefs = new UserPrefs();
        String output = prefs.toString();
        assertTrue(output.contains("Gui Settings"));
        assertTrue(output.contains("Local data file location"));
        assertTrue(output.contains("command history"));
    }
}

