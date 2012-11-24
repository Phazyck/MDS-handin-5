package taskmanager;

/**
 * An interface which defines what features a TaskManager must provide.
 */
public interface ITaskManager {    
    
    /**
     * Execute a given task.
     *
     * @param taskId The id of the task to be executed.
     * @return true if the task was executed, false if not.
     */
    public boolean executeTask(String taskId);
}
