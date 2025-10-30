package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.CommandHistory;

/**
 * Unit tests for {@link TxtCommandHistoryStorage}.
 */
public class TxtCommandHistoryStorageTest {

    @TempDir
    public Path tempDir;

    private Path historyFile;
    private TxtCommandHistoryStorage storage;

    @BeforeEach
    public void setUp() {
        historyFile = tempDir.resolve("history.txt");
        storage = new TxtCommandHistoryStorage(historyFile);
    }

    @Test
    public void getCommandHistoryFilePath_returnsCorrectPath() {
        assertEquals(historyFile, storage.getCommandHistoryFilePath());
    }

    @Test
    public void readCommandHistory_fileDoesNotExist_returnsEmptyOptional() throws IOException {
        assertFalse(Files.exists(historyFile));
        Optional<CommandHistory> result = storage.readCommandHistory();
        assertTrue(result.isEmpty());
    }

    @Test
    public void saveAndReadCommandHistory_success() throws IOException {
        CommandHistory original = new CommandHistory();
        original.add("list");
        original.add("add John Doe");
        original.add("delete 2");

        // Save and read back
        storage.saveCommandHistory(original);
        Optional<CommandHistory> readBack = storage.readCommandHistory();

        assertTrue(readBack.isPresent());
        assertEquals(original.getHistory(), readBack.get().getHistory());
    }

    @Test
    public void saveCommandHistory_createsFileIfMissing() throws IOException {
        assertFalse(Files.exists(historyFile));
        CommandHistory history = new CommandHistory(List.of("help", "exit"));

        storage.saveCommandHistory(history);

        assertTrue(Files.exists(historyFile));
        List<String> lines = Files.readAllLines(historyFile);
        assertEquals(List.of("help", "exit"), lines);
    }

    @Test
    public void readCommandHistory_withExistingFile_success() throws IOException {
        List<String> commands = List.of("a", "b", "c");
        Files.write(historyFile, commands);

        Optional<CommandHistory> result = storage.readCommandHistory();

        assertTrue(result.isPresent());
        assertEquals(commands, result.get().getHistory());
    }

    @Test
    public void saveCommandHistory_nullHistory_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> storage.saveCommandHistory(null));
    }

    @Test
    public void readCommandHistory_invalidFile_throwsIoException() throws IOException {
        // Create a directory where a file is expected
        Path dirPath = tempDir.resolve("invalidDir");
        Files.createDirectory(dirPath);
        TxtCommandHistoryStorage badStorage = new TxtCommandHistoryStorage(dirPath);

        assertThrows(IOException.class, badStorage::readCommandHistory);
    }
}
