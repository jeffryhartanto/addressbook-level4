package guitests;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import seedu.taskscheduler.commons.core.Messages;
import seedu.taskscheduler.testutil.TestTask;
import seedu.taskscheduler.testutil.TestUtil;

//@@author A0148145E

public class RecurCommandTest extends TaskSchedulerGuiTest {

    @Test
    public void add() {
        
        //invalid recur task
        commandBox.runCommand("recur every 3 days until next week");
        assertResultMessage(Messages.MESSAGE_PREV_TASK_NOT_FOUND);
        
        //add one task
        TestTask[] currentList = td.getTypicalTasks();
        commandBox.runCommand(td.event.getAddCommand());
        
        //recur without index
        recur_withoutIndex_success(currentList, "recur every 3 days until next week", td.event);        
        
        //assert undo works for recur command
        currentList = undo_recurCommand_success();        

        //recur with index
        recur_withIndex_success(currentList);        
    }

    private void recur_withIndex_success(TestTask[] currentList) {
        long dateInterval;
        long dateLimit;
        dateInterval = 3 * 24 * 3600 * 1000; // 3 days
        commandBox.runCommand("recur 1 every 3 days until next week");
        Date dateNow = new Date();
        Date taskDate = td.alice.getEndDate().getDate();
        dateLimit = dateNow.getTime() - taskDate.getTime() + 7 * 24 * 3600 * 1000l; // 1 week later
        
        currentList = generateExpectedList(td.alice, currentList, dateInterval, dateLimit);
        assertTrue(taskListPanel.listContainsAll(currentList));
    }

    private TestTask[] undo_recurCommand_success() {
        TestTask[] currentList;
        commandBox.runCommand("undo");
        currentList = td.getTypicalTasks();
        currentList = TestUtil.addTasksToList(currentList, td.event);
        assertTrue(taskListPanel.listContainsAll(currentList));
        return currentList;
    }

    private void recur_withoutIndex_success(TestTask[] currentList, String string, TestTask task) {
        commandBox.runCommand(string);
        long dateInterval = 3 * 24 * 3600 * 1000; // 3 days
        long dateLimit = 7 * 24 * 3600 * 1000; // 1 week

        currentList = TestUtil.addTasksToList(currentList, task);
        currentList = generateExpectedList(task, currentList, dateInterval, dateLimit);
        assertTrue(taskListPanel.listContainsAll(currentList));
    }

    private TestTask[] generateExpectedList(TestTask task, TestTask[] currentList, long dateInterval, long dateLimit) {
        for (long i = dateInterval; i <= dateLimit; i += dateInterval) {
            TestTask taskToAdd = task.copy();
            taskToAdd.addDuration(i);
            currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        }
        return currentList;
    }
}
