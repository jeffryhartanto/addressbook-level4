package guitests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import seedu.taskscheduler.commons.core.Messages;
import seedu.taskscheduler.logic.commands.Command;
import seedu.taskscheduler.testutil.TestTask;
import seedu.taskscheduler.testutil.TestUtil;
import seedu.taskscheduler.ui.TaskCard;

//@@author A0148145E

public class ReplaceCommandTest extends TaskSchedulerGuiTest {

    @Test
    public void replace() {
        
        TestTask[] currentList = td.getTypicalTasks();
        
        //invalid command
        replace_invalidCommand_messageTaskNotFound();
        
        //replace with a deadline task
        currentList = replace_deadlineTask_success(currentList, 2, td.deadline);

        //replace with a floating task
        currentList = replace_floatingTask_success(currentList, currentList.length, td.floating);
        
        //replace with overdue task
        currentList = replace_overdueTask_success(currentList, 1, td.overdue);

        //replace with an event task
        currentList = replace_eventTask_success(currentList, 1, td.event);
        
        //replace with a duplicate task
        replace_duplicateTask_messageDuplicateError(currentList, 5, td.event);

        //replace in empty list
        replace_emptyList_messageInvalidIndex(5, td.event);

    }

    private void replace_emptyList_messageInvalidIndex(int indexToReplace, TestTask taskToCopy) {
        commandBox.runCommand("clear");
        commandBox.runCommand("replace " + indexToReplace + " " + taskToCopy.getTaskString());
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    private void replace_duplicateTask_messageDuplicateError(TestTask[] currentList, int indexToReplace, TestTask taskToCopy) {
        commandBox.runCommand("replace " + indexToReplace + " " + taskToCopy.getTaskString());
        assertResultMessage(Command.MESSAGE_DUPLICATE_TASK);
        assertTrue(taskListPanel.isListMatching(currentList));
    }

    private TestTask[] replace_eventTask_success(TestTask[] currentList, int indexToReplace, TestTask taskToCopy) {
        taskToCopy.setStartDate(currentList[indexToReplace - 1].getStartDate());
        currentList = replace_floatingTask_success(currentList, indexToReplace, taskToCopy);
        return currentList;
    }

    private TestTask[] replace_overdueTask_success(TestTask[] currentList, int indexToReplace, TestTask taskToCopy) {
        assertReplaceSuccess(indexToReplace, taskToCopy, currentList);        
        //assert that overdue task is red
        assertTrue(taskListPanel.navigateToTask(indexToReplace - 1).getPaintFromShape().equals(TaskCard.OVERDUE_INDICATION));
        assertFalse(taskListPanel.navigateToTask(indexToReplace - 1).getPaintFromShape().equals(TaskCard.COMPLETED_INDICATION));
        
        currentList[indexToReplace - 1] = taskToCopy;
        currentList = TestUtil.addTasksToList(currentList);
        return currentList;
    }

    private TestTask[] replace_floatingTask_success(TestTask[] currentList, int indexToReplace, TestTask taskToCopy) {
        assertReplaceSuccess(indexToReplace, taskToCopy, currentList);
        currentList[indexToReplace - 1] = taskToCopy;
        currentList = TestUtil.addTasksToList(currentList);
        return currentList;
    }

    private TestTask[] replace_deadlineTask_success(TestTask[] currentList, int indexToReplace, TestTask taskToCopy) {
        taskToCopy.setEndDate(currentList[indexToReplace - 1].getEndDate());
        currentList = replace_floatingTask_success(currentList, indexToReplace, taskToCopy);
        return currentList;
    }

    private void replace_invalidCommand_messageTaskNotFound() {
        commandBox.runCommand("replace eee " + td.ida.getTaskString());
        assertResultMessage(Messages.MESSAGE_PREV_TASK_NOT_FOUND);
    }

    private void assertReplaceSuccess(int indexToReplace, TestTask taskToCopy, TestTask... currentList) {
        
        commandBox.runCommand("replace " + indexToReplace + " " + taskToCopy.getTaskString());

        //confirm the replaced card contains the right data
        TaskCardHandle replacedCard = taskListPanel.navigateToTask(indexToReplace - 1);
        
        assertMatching(taskToCopy, replacedCard);

        //confirm the list now contains all previous tasks with the replaced task
        TestTask[] expectedList = TestUtil.addTasksToList(currentList);
        expectedList[indexToReplace - 1] = taskToCopy;
        
        assertTrue(taskListPanel.isListMatching(expectedList));
    }
}
