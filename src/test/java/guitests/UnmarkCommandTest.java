package guitests;

import org.junit.Test;

import seedu.taskscheduler.commons.core.Messages;
import seedu.taskscheduler.logic.commands.MarkCommand;
import seedu.taskscheduler.logic.commands.UnmarkCommand;
import seedu.taskscheduler.testutil.TestTask;
import seedu.taskscheduler.ui.TaskCard;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//@@author A0138696L

/**
 * Undo a unmark command in the task scheduler.
 */

public class UnmarkCommandTest extends TaskSchedulerGuiTest {
    
    @Test
    public void unmark() {
        
        //unmark without index given
        unmark_noIndex_messageNoTaskFound();
        
        TestTask[] currentList = td.getTypicalTasks();
        
        //initialise with some marked tasks
        runMarkCommand(1, currentList.length, currentList.length/2);
        currentList = td.getTypicalTasks();
        
        //unmark the first in the list
        int targetIndex = 1;
        assertUnmarkSuccess(targetIndex, currentList);
        
        //unmark the last in the list
        targetIndex = currentList.length;
        assertUnmarkSuccess(targetIndex, currentList);
        
        //unmark the middle in the list
        targetIndex = currentList.length/2;
        assertUnmarkSuccess(targetIndex, currentList);
        
        //invalid index - unmark
        commandBox.runCommand("unmark " + currentList.length + 1);
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        
        //unmark the first in the list again
        targetIndex = 1;
        commandBox.runCommand("unmark " + targetIndex);
        assertResultMessage(UnmarkCommand.MESSAGE_UNMARK_TASK_FAIL);
        
        //mark empty list
        commandBox.runCommand("clear");
        commandBox.runCommand("unmark " + currentList.length + 1);
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);

    }

    private void unmark_noIndex_messageNoTaskFound() {
        commandBox.runCommand("unmark");
        assertResultMessage(Messages.MESSAGE_PREV_TASK_NOT_FOUND);
    }
    
    public void runMarkCommand(int... indices) {
        for (int index : indices) {
            commandBox.runCommand("mark " + index);
        }
    }

    /**
     * Runs the unmark command to unmark the task at specified index as uncompleted and confirms the result is correct.
     * @param targetIndexOneIndexed e.g. to unmark the first task in the list, 1 should be given as the target index.
     * @param currentList A copy of the current list of tasks (before deletion).S
     */
    public void assertUnmarkSuccess(int targetIndexOneIndexed, final TestTask[] currentList) {
        
        TestTask taskToUnMark = currentList[targetIndexOneIndexed-1]; //-1 because array uses zero indexing
        
        commandBox.runCommand("unmark " + targetIndexOneIndexed);
        
        //confirm the task card is now marked uncompleted.
        assertFalse(taskListPanel.navigateToTask(targetIndexOneIndexed - 1).getPaintFromShape().equals(TaskCard.COMPLETED_INDICATION));
        //confirm the result message is correct
        assertResultMessage(String.format(UnmarkCommand.MESSAGE_UNMARK_TASK_SUCCESS, taskToUnMark));
    }
}