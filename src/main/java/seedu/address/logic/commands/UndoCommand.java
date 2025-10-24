package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.versionedaddressbook.NoPreviousCommitException;

public class UndoCommand extends Command{

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Change reverted!";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        try {
            model.undo();
        } catch (NoPreviousCommitException e) {
            throw new CommandException(e.getMessage());
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
