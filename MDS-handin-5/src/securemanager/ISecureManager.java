package securemanager;

import security.serialization.Token;
/**
 * An interface which defines what features a SecureManager must provide.
 */
public interface ISecureManager {    
    
    /**
     * Execute a given task.
     *
     * @param taskId The id of the task to be executed.
     * @param token The token used to authorize this action.
     * @return the result of the execution, e.g. success, failure, not-permitted or token-expired.
     */
    public String executeTask(String taskId, Token token);
}
