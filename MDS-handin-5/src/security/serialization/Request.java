package security.serialization;

import java.io.Serializable;
import javax.xml.bind.annotation.*;

@XmlRootElement
public class Request implements Serializable {

    /**
     * Constructs an ExecutionRequest with blank data.
     */
    public Request() {
        this("", "");
    }

    /**
     * Constructs an ExecutionRequest by defining all its contents.
     *
     * @param token The encrypted token which grants the required privileges.
     * @param taskId The id of the task that should be executed by the receiver.
     */
    public Request(String token, String taskId) {
        this.tokenTS = token;
        this.taskId = taskId;
    }
    /**
     * The encrypted token which grants the required privileges. The token will
     * be encrypted with a key only known to the Server (S) and the
     * TokenService(T), hence the nake, "tokenTS".
     */
    @XmlElement
    public String tokenTS;
    /**
     * The id of the task that should be executed by the receiver.
     */
    @XmlElement
    public String taskId;
}
