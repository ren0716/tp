package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.AddressBookStorage;
import seedu.address.storage.CommandHistoryStorage;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.TxtCommandHistoryStorage;

/**
 * Unit tests for {@link MainApp}.
 * <p>
 * These tests do not start the JavaFX runtime, and instead verify that
 * MainApp correctly loads and initializes model data (AddressBook + CommandHistory)
 * when provided with temporary storage files.
 */
public class MainAppTest {

    @TempDir
    public Path tempDir;

    private MainApp mainApp;

    @BeforeEach
    public void setUp() {
        mainApp = new MainApp();
    }

    @Test
    public void initModelManager_withValidStorage_loadsDataSuccessfully() throws IOException {
        // Create temporary storage files
        Path addressBookFile = tempDir.resolve("addressbook.json");
        Path userPrefsFile = tempDir.resolve("userprefs.json");
        Path historyFile = tempDir.resolve("history.txt");

        // Use actual JSON/txt storages
        AddressBookStorage addressBookStorage = new JsonAddressBookStorage(addressBookFile);
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(userPrefsFile);
        CommandHistoryStorage commandHistoryStorage = new TxtCommandHistoryStorage(historyFile);

        // Combine into a StorageManager
        Storage storage = new StorageManager(addressBookStorage, userPrefsStorage, commandHistoryStorage);

        // Create UserPrefs for initialization
        ReadOnlyUserPrefs userPrefs = new UserPrefs();

        // Call the method under test
        Model model = mainApp.initModelManager(storage, userPrefs);

        // Verify
        assertNotNull(model);
        assertEquals(ModelManager.class, model.getClass());

        // Command history should be initialized (even if empty)
        assertNotNull(((ModelManager) model).getHistory());
    }

    @Test
    public void loadCommandHistory_missingFile_returnsEmptyHistory() throws IOException {
        Path missingFile = tempDir.resolve("missing_history.txt");
        CommandHistoryStorage commandHistoryStorage = new TxtCommandHistoryStorage(missingFile);

        // Simulate storage with no history file yet
        Storage storage = new StorageManager(
                new JsonAddressBookStorage(tempDir.resolve("ab.json")),
                new JsonUserPrefsStorage(tempDir.resolve("prefs.json")),
                commandHistoryStorage
        );

        CommandHistory result = invokeLoadCommandHistory(mainApp, storage);

        assertNotNull(result);
        assertEquals(0, result.getHistory().size());
    }

    @Test
    public void loadCommandHistory_withExistingFile_readsHistoryCorrectly() throws IOException {
        Path historyFile = tempDir.resolve("existing_history.txt");
        CommandHistoryStorage commandHistoryStorage = new TxtCommandHistoryStorage(historyFile);

        // Write fake data
        commandHistoryStorage.saveCommandHistory(new CommandHistory(java.util.List.of("add Alice", "list")));

        Storage storage = new StorageManager(
                new JsonAddressBookStorage(tempDir.resolve("ab.json")),
                new JsonUserPrefsStorage(tempDir.resolve("prefs.json")),
                commandHistoryStorage
        );

        CommandHistory result = invokeLoadCommandHistory(mainApp, storage);

        assertEquals(2, result.getHistory().size());
        assertEquals("add Alice", result.getHistory().get(0));
    }

    /**
     * Utility to access protected/private loadCommandHistory() via reflection
     * since it's a non-public helper in MainApp.
     */
    private CommandHistory invokeLoadCommandHistory(MainApp app, Storage storage) {
        try {
            var method = MainApp.class.getDeclaredMethod("loadCommandHistory", Storage.class);
            method.setAccessible(true);
            return (CommandHistory) method.invoke(app, storage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}



