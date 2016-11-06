package guitests;

import static seedu.taskscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.Test;

import seedu.taskscheduler.logic.commands.ImportCommand;

//@@author A0138696L

public class ImportCommandTest extends TaskSchedulerGuiTest {

    @Test
    public void importFilePath() {
        
        // Checking for invalid file data
        String importingPath = "datatest/testing.xml";
        commandBox.runCommand("import " + importingPath);
        assertResultMessage(ImportCommand.MESSAGE_UNSUCCESS + importingPath + "\n" + ImportCommand.MESSAGE_USAGE_INVALID);
        
        // Checking for invalid importing file format
        importingPath = "datatest/testing.txt";
        commandBox.runCommand("import " + importingPath);
        assertResultMessage(ImportCommand.MESSAGE_UNSUCCESS + importingPath + "\n" + ImportCommand.MESSAGE_USAGE_INVALID);
        
        importingPath = "datatest/testing.asdasd";
 
        commandBox.runCommand("import " + importingPath);
        assertResultMessage(ImportCommand.MESSAGE_UNSUCCESS + importingPath + "\n" + ImportCommand.MESSAGE_USAGE_INVALID);
        
        importingPath = "datatest/-testing-.xml";
        commandBox.runCommand("import " + importingPath);
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        
        importingPath = "datatest/@testing.xml";
        commandBox.runCommand("import " + importingPath);
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
    }
}
