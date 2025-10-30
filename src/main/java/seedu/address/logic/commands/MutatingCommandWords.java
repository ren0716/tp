package seedu.address.logic.commands;

/**
 * Enum representing all commands that mutate the state of the address book.
 * <p>
 * Each enum constant stores the corresponding command word as a {@code String}.
 * This is used to check whether a command modifies the model and therefore
 * requires committing to the versioned history for undo/redo functionality.
 * </p>
 */
public enum MutatingCommandWords {
    ADD("add"),
    DELETE("delete"),
    EDIT("edit"),
    ADDCLASS("addclass"),
    DELETECLASS("deleteclass"),
    MARK("mark"),
    UNMARK("unmark"),
    ASSIGN("assign"),
    ASSIGNALL("assignall"),
    UNASSIGN("unassign"),
    UNASSIGNALL("unassignall");


    private final String commandWord;

    MutatingCommandWords(String commandWord) {
        this.commandWord = commandWord;
    }

    @Override
    public String toString() {
        return commandWord;
    }

    public String getCommandWord() {
        return commandWord;
    }

    /**
     * Checks whether the given string corresponds to any mutating command.
     *
     * @param commandWord The command word to check.
     * @return {@code true} if the command word is a mutating command, {@code false} otherwise.
     */
    public static boolean contains(String commandWord) {
        for (MutatingCommandWords cmd : values()) {
            if (cmd.commandWord.equalsIgnoreCase(commandWord)) {
                return true;
            }
        }
        return false;
    }
}

