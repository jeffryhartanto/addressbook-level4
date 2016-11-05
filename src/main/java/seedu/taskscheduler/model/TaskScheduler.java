package seedu.taskscheduler.model;

import javafx.collections.ObservableList;
import seedu.taskscheduler.commons.exceptions.IllegalValueException;
import seedu.taskscheduler.model.tag.Tag;
import seedu.taskscheduler.model.tag.UniqueTagList;
import seedu.taskscheduler.model.tag.UniqueTagList.DuplicateTagException;
import seedu.taskscheduler.model.task.ReadOnlyTask;
import seedu.taskscheduler.model.task.Task;
import seedu.taskscheduler.model.task.UniqueTaskList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Wraps all data at the Task Scheduler level
 * Duplicates are not allowed (by .equals comparison)
 */
public class TaskScheduler implements ReadOnlyTaskScheduler {

    private static final int EMPTY_VALUE = 0;
    private static final int INITIAL_VALUE = 1;
    
    private final UniqueTaskList tasks = new UniqueTaskList();
    private final UniqueTagList tags = new UniqueTagList();
    private final Map<Tag, Integer> tagsCounter = new HashMap<>();
    private final UniqueTagList tagsMasterList = new UniqueTagList();

    public TaskScheduler() {}

    /**
     * Tasks and Tags are copied into this task scheduler
     */
    public TaskScheduler(ReadOnlyTaskScheduler toBeCopied) {
        this(toBeCopied.getUniqueTaskList(), toBeCopied.getUniqueTagList(), toBeCopied.getTagsCounter());
    }

    /**
     * Tasks and Tags are copied into this task scheduler
     */
    public TaskScheduler(UniqueTaskList tasks, UniqueTagList tags, Map<Tag, Integer> tagsCounter) {
        resetData(tasks.getInternalList(), tags.getInternalList(), tagsCounter);
    }

    public static ReadOnlyTaskScheduler getEmptyTaskScheduler() {
        return new TaskScheduler();
    }

//// list overwrite operations

    public ObservableList<Tag> getTags() {
        return tags.getInternalList();
    }
    
    public ObservableList<Task> getTasks() {
        return tasks.getInternalList();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks.getInternalList().setAll(tasks);
    }

    public void setTags(Collection<Tag> tags) {
        this.tags.getInternalList().setAll(tags);
    }
   
    public void setTagsCounter(Map<Tag, Integer> tagsCounter) {
        this.tagsCounter.putAll(tagsCounter);
    }

    public void resetData(Collection<? extends ReadOnlyTask> newTasks, Collection<Tag> newTags, Map<Tag, Integer> tagsCounter) {
        setTasks(newTasks.stream().map(Task::new).collect(Collectors.toList()));
        setTags(newTags);
        setTagsCounter(tagsCounter);
    }

    public void resetData(ReadOnlyTaskScheduler newData) {
        resetData(newData.getTaskList(), newData.getTagList(), newData.getTagsCounter());
    }

//// task-level operations

    /**
     * Adds a task to the task scheduler
     * Also checks the new task's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException if an equivalent task already exists.
     */
    public void addTask(Task p) throws UniqueTaskList.DuplicateTaskException {
        addTagsFromTask(p);
        tasks.add(p);
    }
    
    //@@author A0148145E
    /**
     * Replaces a task in the task scheduler
     * Also checks the new task's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException if an equivalent task already exists.
     */
    public void replaceTask(Task oldTask, Task newTask) 
            throws UniqueTaskList.DuplicateTaskException, UniqueTaskList.TaskNotFoundException {
        tasks.replace(oldTask, newTask);   
        addTagsFromTask(newTask);
        removeTagsFromTask(oldTask);
    }

    //@@author A0148145E
    /**
     * Marks a task in the task scheduler as completed
     * Also checks the new task's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException if an equivalent task already exists.
     * @throws DuplicateTagException 
     */
    public void markTask(Task task) 
            throws UniqueTaskList.TaskNotFoundException, IllegalValueException {
        tasks.mark(task);
    }

    //@@author A0148145E
    /**
     * Unmarks a task in the task scheduler as completed
     * Also checks the new task's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException if an equivalent task already exists.
     * @throws DuplicateTagException 
     */
    public void unmarkTask(Task task) 
            throws UniqueTaskList.TaskNotFoundException, IllegalValueException {
        tasks.unmark(task);
    }
    

    //@@author A0148145E
    /**
     * Replaces the tag list of a task in the task scheduler.
     * Also checks the new task's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws TaskNotFoundException
     */
    public void tagTask(Task task, UniqueTagList tagList) 
            throws UniqueTaskList.TaskNotFoundException {
        removeTagsFromTask(task);
        tasks.tagTask(task, tagList);   
        addTagsFromTask(task);
    }
    
    //@@author A0140007B
    /**
     * Inserts a task into another task's position in the task scheduler
     * Also checks the new task's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException if an equivalent task already exists.
     */
    public void insertTask(int index, Task newTask) 
            throws UniqueTaskList.TaskNotFoundException {
        syncTagsWithMasterList(newTask);
        tasks.insert(index, newTask);
    }
    //@@author
    
    //@@author A0138696L
    /**
     * Ensures that every tag in this task:
     *  - exists in the master list 
     *  - tag list only contains tags above empty value
     */
    private void syncTagsWithMasterList(Task task) {
        final UniqueTagList taskTags = task.getTags();
        tagsMasterList.mergeFrom(taskTags);

        for (Tag tag : tagsMasterList) {
            if (tagsCounter.get(tag) <= EMPTY_VALUE) {
                removeTagIfContains(tag);
            } else {
                addTagIfNotContains(tag);
            }
        }
    }

    //@@author A0138696L
    private void removeTagIfContains(Tag tag) {
        if (tags.contains(tag)) {
            tags.remove(tag);
        }
    }

    //@@author A0138696L
    private void addTagIfNotContains(Tag tag) {
        if (!tags.contains(tag)) {
            try {
                tags.add(tag);
            } catch (DuplicateTagException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //@@author A0138696L
    private void addTagsFromTask(Task task) {
        final UniqueTagList taskTags = task.getTags();
        for (Tag tag : taskTags) {
            tagsCounter.put(tag, tagsCounter.get(tag) == null ? INITIAL_VALUE : tagsCounter.get(tag) + 1);
        }
        syncTagsWithMasterList(task);
    }

    //@@author A0138696L
    private void removeTagsFromTask(Task task) {
        final UniqueTagList taskTags = task.getTags();
        for (Tag tag : taskTags) {
            tagsCounter.put(tag, tagsCounter.get(tag) == null ? EMPTY_VALUE : tagsCounter.get(tag) - 1);
        }
        syncTagsWithMasterList(task);
    }
    //@@author

    public boolean removeTask(ReadOnlyTask key) throws UniqueTaskList.TaskNotFoundException {
        if (tasks.remove(key)) {
            removeTagsFromTask((Task) key);
            return true;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }

//// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

//// util methods

    @Override
    public String toString() {
        return tasks.getInternalList().size() + " tasks, " + tags.getInternalList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public List<ReadOnlyTask> getTaskList() {
        return Collections.unmodifiableList(tasks.getInternalList());
    }

    @Override
    public List<Tag> getTagList() {
        return Collections.unmodifiableList(tags.getInternalList());
    }

    @Override
    public UniqueTaskList getUniqueTaskList() {
        return this.tasks;
    }

    @Override
    public UniqueTagList getUniqueTagList() {
        return this.tags;
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskScheduler // instanceof handles nulls
                && this.tasks.equals(((TaskScheduler) other).tasks)
                && this.tags.equals(((TaskScheduler) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(tasks, tags);
    }

    @Override
    public Map<Tag, Integer> getTagsCounter() {
        return tagsCounter;
    }
}
