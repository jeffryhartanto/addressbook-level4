package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.taskscheduler.commons.core.Messages;
import seedu.taskscheduler.logic.commands.ClearCommand;
import seedu.taskscheduler.logic.commands.Command;
import seedu.taskscheduler.logic.commands.UndoCommand;
import seedu.taskscheduler.model.task.ReadOnlyTask;
import seedu.taskscheduler.testutil.TestTask;
import seedu.taskscheduler.testutil.TestUtil;

//@@author A0140007B

public class UndoCommandTest extends TaskSchedulerGuiTest {
	
	@Test
    public void undo() {

		//clear mutate command history
        TestTask[] currentList = td.getTypicalTasks();
        ReadOnlyTask task = td.event;
        String commandKey;
        
        //undo add command
        undo_add_success(currentList, td.event);
        
        //undo delete command
        undo_delete_success(currentList);

        //undo replace command
        undo_replace_success(currentList);
        
        //undo edit command
        undo_edit_success(currentList);

        //undo mark command
        undo_mark_success(currentList);
        
        //undo unmark command
        undo_unmark_success(currentList);
        
        //undo multiple mixed commands
        undo_multipleCommand_success(currentList);
        
        //undo without any remaining previous command
		undo_noPreviousCommand_fail();
		
        undo_multipleDelete_success(currentList);

        undo_clear_success(currentList);
    }

    private void undo_unmark_success(TestTask[] currentList) {
        ReadOnlyTask task;
        String commandKey;
        commandBox.runCommand("mark 5");
        commandKey = "unmark";
        task = taskListPanel.getTask(4);
        commandBox.runCommand(commandKey + " 5");
        assertUndoSuccess(commandKey,currentList,task);
    }

    private void undo_mark_success(TestTask[] currentList) {
        ReadOnlyTask task;
        String commandKey;
        commandKey = "mark";
        task = taskListPanel.getTask(4);
        commandBox.runCommand(commandKey + " 5");
        assertUndoSuccess(commandKey,currentList,task);
    }

    private void undo_edit_success(TestTask[] currentList) {
        ReadOnlyTask task;
        String commandKey;
        commandKey = "edit";
        task = taskListPanel.getTask(1);
        commandBox.runCommand(commandKey + " 2 " + td.event.getTaskString());
        assertUndoSuccess(commandKey,currentList,td.event);
    }

    private void undo_replace_success(TestTask[] currentList) {
        ReadOnlyTask task;
        String commandKey;
        commandKey = "replace";
        task = taskListPanel.getTask(1);
        commandBox.runCommand(commandKey + " 2 " + td.ida.getTaskString());
        assertUndoSuccess(commandKey,currentList,td.ida);
    }

    private void undo_delete_success(TestTask[] currentList) {
        ReadOnlyTask task;
        String commandKey;
        commandKey = "delete";
        task = taskListPanel.getTask(0);
        commandBox.runCommand(commandKey + " 1");
        assertUndoSuccess(commandKey,currentList,task);
    }

    private void undo_add_success(TestTask[] currentList, TestTask task) {
        String commandKey;
        commandKey = "add";
        commandBox.runCommand(task.getAddCommand());
        assertUndoSuccess(commandKey,currentList,task);
    }

    private void undo_multipleCommand_success(TestTask[] currentList) {
        commandBox.runCommand("replace " + 2 + " " + td.ida.getTaskString());
        commandBox.runCommand("mark 5");
        commandBox.runCommand("mark 3");
        commandBox.runCommand(td.event.getAddCommand());
        commandBox.runCommand("delete 3");
        for (int i = 0; i < 5; i++) {
        	commandBox.runCommand(UndoCommand.COMMAND_WORD);
        }
        assertTrue(taskListPanel.isListMatching(currentList));
    }

    private void undo_noPreviousCommand_fail() {
        commandBox.runCommand(UndoCommand.COMMAND_WORD);
		assertResultMessage(UndoCommand.MESSAGE_FAILURE);
    }

    private void undo_multipleDelete_success(TestTask[] currentList) {
        for (int i = 0; i < 4; i++) {
            commandBox.runCommand("delete 5");
        }
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        for (int i = 0; i < 3; i++) {
            commandBox.runCommand(UndoCommand.COMMAND_WORD);
        }
        assertTrue(taskListPanel.isListMatching(currentList));
    }

    private void undo_clear_success(TestTask[] currentList) {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertResultMessage(ClearCommand.MESSAGE_SUCCESS);
        commandBox.runCommand(UndoCommand.COMMAND_WORD);
        assertTrue(taskListPanel.isListMatching(currentList));
    }

    private void assertUndoSuccess(String commandKey, TestTask[] currentList, ReadOnlyTask... taskList) {
       
    	commandBox.runCommand(UndoCommand.COMMAND_WORD);

        //confirm the list now contains all previous tasks with the undo task
        TestTask[] expectedList = TestUtil.addTasksToList(currentList);
        assertTrue(taskListPanel.isListMatching(expectedList));
        StringBuilder sb = new StringBuilder();
        for (ReadOnlyTask testTask : taskList) {
            sb.append("\n");
            sb.append(testTask.getAsText());
        }
        assertResultMessage(String.format(Command.MESSAGE_REVERT_COMMAND, commandKey, sb));
    }
}
