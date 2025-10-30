package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.model.CommandHistory;

/**
 * Represents a storage interface for persisting {@link CommandHistory} data.
 * <p>
 * Implementations define how command history is read from and written to
 * a storage medium, such as a local file or database.
 * <p>
 * This interface does not specify any specific file format.
 */
public interface CommandHistoryStorage {

    /**
     * Returns the file path of the command history storage.
     *
     * @return the {@link Path} representing where the command history is stored.
     */
    Path getCommandHistoryFilePath();

    /**
     * Reads the {@link CommandHistory} from storage.
     *
     * @return an {@link Optional} containing the stored {@link CommandHistory}, or
     *         {@code Optional.empty()} if no data exists.
     * @throws IOException if there is an error reading the storage.
     */
    Optional<CommandHistory> readCommandHistory() throws IOException;

    /**
     * Saves the provided {@link CommandHistory} to storage.
     *
     * @param history the command history to save; must not be null.
     * @throws IOException if there is an error writing to the storage.
     */
    void saveCommandHistory(CommandHistory history) throws IOException;
}

