package seedu.taskscheduler.commons.util;

import seedu.taskscheduler.model.task.ReadOnlyTask;
import seedu.taskscheduler.model.task.ReadOnlyTask.TaskType;

//@@author A0140007B

/**
 * Utility methods for Task
 */

public final class TaskUtil {

	public static String convertToTaskString(ReadOnlyTask task) {
		if (task.getType() == TaskType.FLOATING) {
			return getFloatingTaskString(task);
		} else if (task.getType() == TaskType.DEADLINE) {
			return getDeadlineTaskString(task);
		} else {
			return getEventTaskString(task);
		}
		
	}
	
	private static String getFloatingTaskString(ReadOnlyTask task) {
		return "add " + task.getName();
	}
	
	private static String getDeadlineTaskString(ReadOnlyTask task) {
		return "add " + task.getName() + " by " + task.getEndDate();
	}
	
	private static String getEventTaskString(ReadOnlyTask task) {
		return "add " + task.getName() + " from " + task.getStartDate()
			+ " to " + task.getEndDate() + " at " + task.getLocation();
	}
}
