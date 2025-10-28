package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CommandHistory}.
 */
public class CommandHistoryTest {

    private CommandHistory commandHistory;
    private int maxSize;

    @BeforeEach
    public void setUp() throws Exception {
        commandHistory = new CommandHistory();
        Field field = CommandHistory.class.getDeclaredField("MAX_SIZE");
        field.setAccessible(true);
        maxSize = field.getInt(null);
    }

    @Test
    public void constructor_empty_createsEmptyHistory() {
        assertTrue(commandHistory.getHistory().isEmpty());
    }

    @Test
    public void constructor_withInitialHistory_trimsToMaxSizeAndSetsIndex() {
        // Create list larger than MAX_SIZE (e.g. maxSize + 5)
        List<String> longList = new ArrayList<>();
        for (int i = 1; i <= maxSize + 5; i++) {
            longList.add("cmd" + i);
        }

        CommandHistory ch = new CommandHistory(longList);

        // Only last MAX_SIZE kept
        assertEquals(maxSize, ch.getHistory().size());
        assertEquals("cmd" + (maxSize + 5), ch.getHistory().get(maxSize - 1));
        // Ensure trimming started from cmd6 (when maxSize=50)
        assertEquals("cmd" + (6 + (maxSize - 50)), ch.getHistory().get(0));
    }

    @Test
    public void add_validCommand_addsSuccessfully() {
        commandHistory.add("list");
        commandHistory.add("add John");
        assertEquals(2, commandHistory.getHistory().size());
        assertEquals("add John", commandHistory.getHistory().get(1));
    }

    @Test
    public void add_exceedsMaxSize_oldestRemoved() {
        for (int i = 1; i <= maxSize + 5; i++) {
            commandHistory.add("cmd" + i);
        }
        assertEquals(maxSize, commandHistory.getHistory().size());
        // Oldest entries dropped, so first should be cmd6 if maxSize=50
        assertEquals("cmd" + (maxSize + 5 - maxSize + 1), commandHistory.getHistory().get(0));
    }

    @Test
    public void previous_emptyHistory_returnsEmptyString() {
        assertEquals("", commandHistory.previous());
    }

    @Test
    public void previous_navigationMovesUpCorrectly() {
        commandHistory.add("first");
        commandHistory.add("second");
        commandHistory.add("third");

        assertEquals("third", commandHistory.previous());
        assertEquals("second", commandHistory.previous());
        assertEquals("first", commandHistory.previous());
        assertEquals("first", commandHistory.previous());
    }

    @Test
    public void next_emptyHistory_returnsEmptyString() {
        assertEquals("", commandHistory.next());
    }

    @Test
    public void next_navigationMovesDownCorrectly() {
        commandHistory.add("a");
        commandHistory.add("b");
        commandHistory.add("c");

        commandHistory.previous(); // c
        commandHistory.previous(); // b

        assertEquals("c", commandHistory.next());
        assertEquals("", commandHistory.next());
        assertEquals("", commandHistory.next());
    }

    @Test
    public void resetHistory_replacesExistingHistoryAndResetsIndex() {
        CommandHistory oldHistory = new CommandHistory(List.of("one", "two", "three"));
        CommandHistory newHistory = new CommandHistory(List.of("new1", "new2"));

        oldHistory.resetHistory(newHistory);

        assertEquals(2, oldHistory.getHistory().size());
        assertEquals("new2", oldHistory.getHistory().get(1));
        assertEquals("new2", oldHistory.previous());
    }
}

