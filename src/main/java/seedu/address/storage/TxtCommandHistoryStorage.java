package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import seedu.address.model.CommandHistory;

/**
 * Stores command history in a text file, one command per line.
 */
public class TxtCommandHistoryStorage implements CommandHistoryStorage {

    private final Path filePath;

    public TxtCommandHistoryStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getCommandHistoryFilePath() {
        return filePath;
    }

    @Override
    public Optional<CommandHistory> readCommandHistory() throws IOException {
        if (!Files.exists(filePath)) {
            return Optional.empty();
        }

        // Read all lines from the file
        List<String> lines = Files.readAllLines(filePath);
        CommandHistory commandHistory = new CommandHistory(lines);

        return Optional.of(commandHistory);
    }


    @Override
    public void saveCommandHistory(CommandHistory history) throws IOException {
        // Ensure the parent directory exists
        Files.createDirectories(filePath.getParent());

        // Write the commands to the file, one per line
        Files.write(filePath, history.getHistory());
    }

}
