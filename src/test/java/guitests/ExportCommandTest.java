package guitests;

import static org.junit.Assert.assertEquals;
import static seedu.taskscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.io.File;

import org.junit.Test;

import seedu.taskscheduler.commons.util.FileUtil;
import seedu.taskscheduler.logic.commands.ExportCommand;

//@@author A0138696L

public class ExportCommandTest extends TaskSchedulerGuiTest {

    @Test
    public void exportFilePath() {
        
        // check for exported file
        String exportingPath = "datatest/testing.xml";
        String exportedFile = null;
        commandBox.runCommand("export " + exportingPath);
        File file = new File(exportingPath);
        System.out.println(file.getAbsolutePath());
        if (FileUtil.isFileExists(file)) {
            System.out.println("testing");
            exportedFile = exportingPath;
        }
        assertEquals(exportingPath, exportedFile);
        
        // check for invalid file path
        exportingPath = "datatest/testing_123.xml";
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }
}
