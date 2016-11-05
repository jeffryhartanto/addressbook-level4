package guitests;

import org.junit.Test;

import seedu.taskscheduler.commons.core.Messages;
import seedu.taskscheduler.logic.commands.Command;
import seedu.taskscheduler.logic.commands.TagCommand;
import seedu.taskscheduler.model.task.ReadOnlyTask;
import seedu.taskscheduler.testutil.TestTask;

import static org.junit.Assert.assertTrue;
import static seedu.taskscheduler.logic.commands.TagCommand.MESSAGE_SUCCESS;

//@@author A0148145E

public class TagCommandTest extends TaskSchedulerGuiTest {

    @Test
    public void tag() {

        String singleTagArg = "Priority";
        String multiTagsArg = "School Urgent";
        TestTask[] currentList = td.getTypicalTasks();
        int targetIndex = 1;
        
        //put a single tag
        assertTagSuccess(targetIndex, singleTagArg, currentList);

        //replace with multiple tags
        assertTagSuccess(targetIndex, multiTagsArg, currentList);

        //invalid index
        tag_indexOutOfBound_messageInvalidIndex(currentList);

        undo_tagCommand_success(currentList, targetIndex, singleTagArg);
    }

    private void tag_indexOutOfBound_messageInvalidIndex(TestTask[] currentList) {
        commandBox.runCommand("tag " + currentList.length + 1);
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    private void undo_tagCommand_success(TestTask[] currentList, int targetIndexOneIndexed, String tagArgs) {
        
        ReadOnlyTask task = taskListPanel.getTask(targetIndexOneIndexed - 1);
        
        commandBox.runCommand("undo");
        
        assertTrue(taskListPanel.navigateToTask(targetIndexOneIndexed - 1).getTags()
                .equals(convertArgsToTagString(tagArgs)));

        assertResultMessage(String.format(Command.MESSAGE_REVERT_COMMAND, TagCommand.COMMAND_WORD, "\n" + task));
    }

    /**
     * Runs the tag command to rag the task at specified index and confirms the result is correct.
     * @param targetIndexOneIndexed e.g. to tag the first task in the list, 1 should be given as the target index.
     * @param currentList A copy of the current list of tasks.
     */
    private void assertTagSuccess(int targetIndexOneIndexed, String tagArgs, final TestTask[] currentList) {
        TestTask taskToTag = currentList[targetIndexOneIndexed-1]; //-1 because array uses zero indexing

        commandBox.runCommand("tag " + targetIndexOneIndexed + " " + tagArgs);
        
        
        assertTrue(taskListPanel.navigateToTask(targetIndexOneIndexed - 1).getTags()
                .equals(convertArgsToTagString(tagArgs)));

        //confirm the result message is correct
        assertResultMessage(String.format(MESSAGE_SUCCESS, taskToTag));
    }
    
    /**
     * Convert tags param string to task card display string
     * @param tagArgs tags string that use in tag command
     * @return tags string in task card display
     */
    private String convertArgsToTagString(String tagArgs) {
        final StringBuffer buffer = new StringBuffer();
        final String separator = ", ";
        for (String tag : tagArgs.split("\\s+")) {
            buffer.append("[" + tag + "]").append(separator);
        }
        if (buffer.length() == 0) {
            return "";
        } else {
            return buffer.substring(0, buffer.length() - separator.length());
        }
    }
}

