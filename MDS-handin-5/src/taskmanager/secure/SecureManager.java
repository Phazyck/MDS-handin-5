package taskmanager.secure;

import securemanager.ISecureManager;
import security.serialization.Token;

/**
 * The SecureManager acts as a proxy to another TaskManager,
 * requiring authorization through the use of tokens in order to 
 */
public class SecureManager implements ISecureManager {
    
    
    public SecureManager() {
        
    }
    
    

    @Override
    public String executeTask(String taskId, Token token) {
        // TODO - Return "success" if executed properly. 
        //return "success";
        // TODO - Return "failure" if cannot be executed.
        // return "failure";
        // TODO - Return "role-mismatch" if the role doesn't match.
        //return "role-mismatch";
        // TODO - Return "token-expired" if the token has expired.
        //return "token-expired";
        
        return "";
    }

}
