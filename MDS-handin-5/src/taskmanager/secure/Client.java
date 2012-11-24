package taskmanager.secure;

import com.*;
import java.security.Key;
import javax.xml.bind.JAXBException;
import security.*;
import static security.Encryption.*;
import security.serialization.*;
import static serialization.Serializer.*;
import taskmanager.ITaskManager;

public class Client implements ITaskManager {

    // The ITU credentials required to request tokens.
    private String credentials;
    // The token which will be used to provide authorization in certain situations.
    private String tokenTS = "";

    public Client(Credentials credentials) throws Exception {
        this.credentials = serialize(credentials);
        renewToken();
    }
    // Transmitter used for outgoing messages to the TokenService.
    private final Transmitter<String> oT = new com.udp.StringTransmitter("localhost", TokenService.PORTC);
    // Receiver used for incoming messages from the TokenService.
    private final Receiver<String> iT = oT.getReceiver();
    // The secret key which has been formed between the Client and the TokenService.
    private final Key keyCT = suggestKey(oT);

    private void renewToken() {
        // Try to...
        try {
            // ...encrypt the credentials...
            String credentialsCT = encrypt(credentials, keyCT);
            // ...transmit them to the TokenService...
            oT.transmit(credentialsCT);
            // ...and in turn receive a (double-encrypted) token from the TokenService...
            String tokenTSCT = iT.receive();
            // ...which will be stored after removing the first layer of encryption.
            tokenTS = decrypt(tokenTSCT, keyCT);
        } catch (Exception ex) {
            // In case an error occurs, it should be 'logged'.
            System.out.println("An error occured when trying to renew the token: " + ex.getMessage());
        }
    }
    // Transmitter used for outgoing messages to the Server.
    private final Transmitter<String> oS = new com.udp.StringTransmitter("localhost", Server.PORTC);
    // Receiver used for incoming messages from the Server.
    private final Receiver<String> iS = oS.getReceiver();
    private final int attempts = 3;

    @Override
    public boolean executeTask(String taskId) {
        // Prepare a request object.
        Request requestObject = new Request(tokenTS, taskId);

        // Prepare a request message.
        String request;
        try {
            // Attempt to serialize the request object into a string.
            request = serialize(requestObject);
        } catch (JAXBException e) {
            // If an error occurs, print it and abort the execution.
            System.out.println("An error occured during serialization: " + e.getMessage());
            return false;
        }
        
        // Make some 'attempts' at performing the execution.
        for (int i = 0; i < attempts; i++) {
            // Transmit the request.
            oS.transmit(request);
            // Get a reply.
            String reply = iS.receive();
            // Do different actions depending of the contents of the reply.
            switch (reply.toLowerCase()) {
                // If 'true', the execution went well...
                case "true":
                    // ...and thus, true should be returned.
                    return true;
                // If 'false', the execution failed in a proper manner...
                case "false":                    
                    // ...and thus, false should be returned.
                    return false;
                // If 'not-permitted', the given token does have the necessary permissions...
                case "not-permitted":
                    // ...and thus, false should be returned.
                    return false;
                // If 'expired', the token's timestamp has gotten too old...
                case "expired":
                    // ...thus, the token should be renewed...
                    renewToken();
                    // ...and we should try again...
                    continue;
                // If not any of the above, the reply is not recognized...
                default:
                    // ...thus it should be 'logged'...
                    System.out.println("Unrecognized reply: " + reply);
                    // ...and false should be returned.
                    return false;
            }
        }
        
        // If this line is reached, the limits of attempts has been used...
        // ...and thus, the execution has failed.
        return false;
    }
}