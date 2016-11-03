package guitests;

import guitests.guihandles.TaskCardHandle;
import org.junit.Test;

import seedu.taskscheduler.commons.core.Messages;
import seedu.taskscheduler.logic.commands.AddCommand;
import seedu.taskscheduler.testutil.TestTask;
import seedu.taskscheduler.testutil.TestUtil;

import static org.junit.Assert.assertTrue;

//@@author A0140007B

public class AddCommandTest extends TaskSchedulerGuiTest {

    @Test
    public void add() {
        //add event task
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = td.event;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add deadline task
        taskToAdd = td.deadline;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add floating task
        taskToAdd = td.floating;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add duplicate tasks
        add_duplicateTasks_messageDuplicateTask(currentList, td.floating, td.deadline, td.event);

        //add to empty list
        commandBox.runCommand("clear");
        assertAddSuccess(td.alice);

        //invalid command
        add_invalidCommand_messageUnknownCommand();
    }

    private void add_invalidCommand_messageUnknownCommand() {
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void add_duplicateTasks_messageDuplicateTask(TestTask[] currentList, TestTask...tasks) {
        for (TestTask task : tasks) {
            commandBox.runCommand(task.getAddCommand());
            assertResultMessage(AddCommand.MESSAGE_DUPLICATE_TASK);
            assertTrue(taskListPanel.listContainsAll(currentList));
        }
    }

    private void assertAddSuccess(TestTask taskToAdd, TestTask... currentList) {
        commandBox.runCommand(taskToAdd.getAddCommand());

        //confirm the new card contains the right data
        TaskCardHandle addedCard = taskListPanel.navigateToTask(taskToAdd.getName().fullName);
        assertMatching(taskToAdd, addedCard);

        //confirm the list now contains all previous tasks plus the new task
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, taskToAdd);
        assertTrue(taskListPanel.listContainsAll(expectedList));
    }

}
