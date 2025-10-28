package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the history of commands entered by the user.
 * Supports navigating with up/down like a command line.
 */
public class CommandHistory {

    private static final int MAX_SIZE = 50;
    private final List<String> history = new ArrayList<>();
    private int currentIndex;

    /** Initializes empty command history. */
    public CommandHistory() {
        this.currentIndex = 0;
    }

    /** Initializes command history with a list of commands. */
    public CommandHistory(List<String> initialHistory) {
        int start = Math.max(0, initialHistory.size() - MAX_SIZE);
        this.history.addAll(initialHistory.subList(start, initialHistory.size()));
        this.currentIndex = history.size(); // start after last command
    }

    /** Adds a command to the history. */
    public void add(String command) {
        history.add(command);
        if (history.size() > MAX_SIZE) {
            history.remove(0);
        }
        currentIndex = history.size(); // reset index
    }

    /** Returns the previous command (up arrow). */
    public String previous() {
        if (history.isEmpty()) {
            return "";
        }
        if (currentIndex > 0) {
            currentIndex--;
        }
        return history.get(currentIndex);
    }

    /** Returns the next command (down arrow). */
    public String next() {
        if (history.isEmpty()) {
            return "";
        }
        if (currentIndex < history.size() - 1) {
            currentIndex++;
            return history.get(currentIndex);
        } else {
            currentIndex = history.size();
            return ""; // empty when past last command
        }
    }

    /** Returns all commands in history (copy to prevent external modification). */
    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

    /** Resets history with new data, resets index. */
    public void resetHistory(CommandHistory newData) {
        requireNonNull(newData);
        history.clear();
        history.addAll(newData.getHistory());
        currentIndex = history.size(); // reset index
    }
}



