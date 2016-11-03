package seedu.taskscheduler.logic.commands;

import seedu.taskscheduler.commons.core.EventsCenter;
import seedu.taskscheduler.commons.core.Messages;
import seedu.taskscheduler.commons.core.UnmodifiableObservableList;
import seedu.taskscheduler.commons.events.ui.CommandBoxTextChangeRequestEvent;
import seedu.taskscheduler.commons.events.ui.JumpToListRequestEvent;
import seedu.taskscheduler.commons.util.TaskUtil;
import seedu.taskscheduler.model.task.ReadOnlyTask;
import seedu.taskscheduler.ui.CommandBox;

//@@author A0140007B
/**
 * Selects a task display the index's command.
 */
public class SelectCommand extends Command {

    public final int targetIndex;

    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the task display the index's command.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_TASK_SUCCESS = "Selected Task: %1$s";

    public SelectCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    //@@author A0140007B
    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        EventsCenter.getInstance().post(new CommandBoxTextChangeRequestEvent(
        		TaskUtil.convertToTaskString(lastShownList.get(targetIndex - 1))));
        CommandHistory.setModifiedTask(lastShownList.get(targetIndex - 1));
        return new CommandResult(String.format(MESSAGE_SELECT_TASK_SUCCESS, targetIndex));

    }
    //@@author

    @Override
    public CommandResult revert() {
        // This command not available for revert
        assert false : Messages.MESSAGE_PROGRAM_ERROR;
        return null;
    }

}
