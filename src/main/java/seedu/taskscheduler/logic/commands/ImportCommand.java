package seedu.taskscheduler.logic.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import seedu.taskscheduler.commons.core.EventsCenter;
import seedu.taskscheduler.commons.core.Messages;
import seedu.taskscheduler.commons.events.storage.ImportFilePathEvent;
import seedu.taskscheduler.commons.util.FileUtil;

//@@author A0138696L

/**
 * Importing data into taskScheduler.
 */
public class ImportCommand extends Command {
    public static final String COMMAND_WORD = "import";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Import file to Task Scheduler. "
            + "Parameters: <filename>\n"
            + "Example: " + COMMAND_WORD
            + " TaskSchedulerData\n";
    public static final String MESSAGE_USAGE_INVALID = COMMAND_WORD + ": valid file to Task Scheduler. "
            + "Parameters: <filename>\n"
            + "Example: " + COMMAND_WORD
            + " TaskSchedulerData\n";

    public static final String MESSAGE_SUCCESS = "File path changed: %s";
    public static final String MESSAGE_UNSUCCESS = "File not found: ";

    private String filePath;
    
    public ImportCommand(String arguments) {
        this.filePath = arguments;
    }

    @Override
    public CommandResult execute() {
        if (FileUtil.isFileExists(new File(filePath)) && filePath.endsWith(".xml")) {
            restartapp();
            EventsCenter.getInstance().post(new ImportFilePathEvent(filePath));
            return new CommandResult(String.format(MESSAGE_SUCCESS, filePath));
            
        }
        return new CommandResult(MESSAGE_UNSUCCESS + filePath + "\n" + MESSAGE_USAGE_INVALID); 
    }

    @Override
    public CommandResult revert() {
        // This command not available for revert
        assert false : Messages.MESSAGE_PROGRAM_ERROR;
        return null;
    } 
    
    private void restartapp() {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File("MustDoList.jar");

        if(!currentJar.getName().endsWith(".jar"))
          return;

        final ArrayList<String> command = new ArrayList<String>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
