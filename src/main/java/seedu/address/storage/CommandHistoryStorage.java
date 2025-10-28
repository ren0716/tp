package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.model.CommandHistory;

/**
 * Represents a storage for command history.
 */
public interface CommandHistoryStorage {

    Path getCommandHistoryFilePath();

    Optional<CommandHistory> readCommandHistory() throws IOException;

    void saveCommandHistory(CommandHistory history) throws IOException;
}
