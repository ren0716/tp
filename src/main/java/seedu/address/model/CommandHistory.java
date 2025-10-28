package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the history of commands entered by the user.
 * Provides functionality to add commands and navigate through
 * previous and next commands like a typical command line history.
 */
public class CommandHistory {

    private static final int MAX_SIZE = 50;
    private List<String> history = new ArrayList<>();
    private int currentIndex;

    /**
     * Creates a {@code CommandHistory} initialized with a list of commands.
     * Keeps only the most recent {@code MAX_SIZE} commands.
     *
     * @param initialHistory List of initial commands.
     */
    public CommandHistory(List<String> initialHistory) {
        int start = Math.max(0, initialHistory.size() - MAX_SIZE);
        this.history.addAll(initialHistory.subList(start, initialHistory.size()));
        this.currentIndex = this.history.size(); // points just after the last command
    }

    /**
     * Creates an empty {@code CommandHistory}.
     */
    public CommandHistory() {
        this.currentIndex = 0;
    }

    /**
     * Adds a new command to the history.
     * Ignores null or blank commands. Maintains a maximum size.
     *
     * @param command The command string to add.
     */
    public void add(String command) {
        if (command == null || command.trim().isEmpty()) {
            return;
        }
        history.add(command);
        if (history.size() > MAX_SIZE) {
            history.remove(0);
        }
        currentIndex = history.size(); // Reset index to end
    }

    /**
     * Returns the previous command in history (up arrow behavior).
     *
     * @return The previous command, or an empty string if at the start.
     */
    public String previous() {
        if (history.isEmpty()) {
            return "";
        }
        if (currentIndex > 0) {
            currentIndex--;
        }
        return history.get(currentIndex);
    }

    /**
     * Returns the next command in history (down arrow behavior).
     *
     * @return The next command, or an empty string if at the end.
     */
    public String next() {
        if (history.isEmpty()) {
            return "";
        }
        if (currentIndex < history.size() - 1) {
            currentIndex++;
            return history.get(currentIndex);
        } else {
            currentIndex = history.size();
            return "";
        }
    }

    /**
     * Returns a list of all commands currently in the history.
     *
     * @return The list of commands.
     */
    public List<String> getHistory() {
        return this.history;
    }

    /**
     * Resets the existing data of this {@code CommandHistory} with {@code newData}.
     */
    public void resetHistory(CommandHistory newData) {
        requireNonNull(newData);

        this.history = newData.getHistory();
    }
}


