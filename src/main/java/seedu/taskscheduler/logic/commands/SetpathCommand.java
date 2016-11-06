package seedu.taskscheduler.logic.commands;

import java.io.File;

import seedu.taskscheduler.commons.core.EventsCenter;
import seedu.taskscheduler.commons.events.storage.FilePathChangedEvent;

//@@author A0138696L

/**
 * Set the working path of the Task Scheduler.
 */
public class SetpathCommand extends Command {

    public static final String COMMAND_WORD = "setpath";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Set custom save path for Task Scheduler. "
            + "Parameters: <filename>\n"
            + "Example: " + COMMAND_WORD
            + " TaskSchedulerData.xml\n";

    public static final String MESSAGE_SUCCESS = "File path changed: %s";
    
    private String savedPathLink;
    
    public SetpathCommand(String arguments) {
        this.savedPathLink = arguments;
    }

    @Override
    public CommandResult execute() {
        removePrvFile();
        CommandHistory.setPreviousStorageFilePath(savedPathLink);
        EventsCenter.getInstance().post(new FilePathChangedEvent(savedPathLink));
        CommandHistory.addExecutedCommand(this);
        return new CommandResult(String.format(MESSAGE_SUCCESS, savedPathLink));
    }

    @Override
    public CommandResult revert() {
        removePrvFile();
        if (savedPathLink == CommandHistory.readPreviousStorageFilePath()) {
            CommandHistory.getPreviousStorageFilePath();
        }
        savedPathLink = CommandHistory.getPreviousStorageFilePath();
        EventsCenter.getInstance().post(new FilePathChangedEvent(savedPathLink));
        CommandHistory.addRevertedCommand(this);
        return new CommandResult(String.format(MESSAGE_SUCCESS, savedPathLink));
    } 
    
    private void removePrvFile() {
        File file = new File(CommandHistory.readPreviousStorageFilePath());
        file.delete();
    }
}