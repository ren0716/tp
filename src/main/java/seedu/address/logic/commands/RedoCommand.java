package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.versionmanager.exceptions.NoPreviousUndoException;

/**
 * restores the effect of the latest undone command
 * Note: Redo is only available immediately after an undo operation. If any new command
 * is executed after an undo, the redo history is cleared and redo becomes unavailable.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Change restored!";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        try {
            model.redo();
        } catch (NoPreviousUndoException e) {
            throw new CommandException(e.getMessage());
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }
}
