package taskmanager;

import serialization.*;

/**
 * An interface which defines what features a TaskManager must provide.
 */
public interface TaskManager {

    /**
     * Execute a given task.
     *
     * @param taskId The id of the task to be executed.
     * @return true if the task was executed, false if not.
     */
    public boolean executeTask(String taskId);

    /**
     * @return All the users.
     */
    public Users getUsers();

    /**
     * Gets all tasks attended by a certain user.
     *
     * @param attendantId The id of the user.
     * @return The attendants tasks.
     */
    public Tasks getAttendantTasks(String attendantId);

    /**
     * Gets a specific task.
     *
     * @param taskId The id of the task.
     * @return The task.
     */
    public Task getTask(String taskId);
}
