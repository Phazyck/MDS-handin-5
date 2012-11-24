package taskmanager.secure;

import com.*;
import java.net.*;
import java.security.Key;
import securemanager.*;
import static security.Encryption.*;
import security.serialization.Request;
import security.serialization.Token;
import static serialization.Serializer.*;

public class Server implements Runnable {

    /**
     * Run the Server.
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        // TODO - run the Server.
    }
    // The port which will be used for incoming messages from a Client.   
    public final static int PORTC = 1002;
    // Receiver used for incoming messages from a Client.
    private final Receiver<String> iC = new com.udp.StringReceiver(new DatagramSocket(PORTC));
    // The port which will be used for incoming messages from a TokenService.
    public final static int PORTT = 1003;
    // Receiver used for incoming messages from a TokenService.
    private final Receiver<String> iT = new com.udp.StringReceiver(new DatagramSocket(PORTT));
    // The secret key which has been formed between the TokenService and the Server.
    private Key keyTS = acceptKey(iT);
    // The actual task manager.
    private ISecureManager manager = new SecureManager();

    /**
     * Prepare the Server.
     */
    public Server() throws Exception {
    }

    @Override
    public void run() {
        while (true) {
            // Receive a requset.
            String request = iC.receive();
            // Prepare a transmitter in order to send back a reply to the source.
            Transmitter<String> oC = iC.getTransmitter();
            // Prepare a reply.
            String reply = "null";
            // Try to...
            try {
                // ...deserialize the request.
                Request requestObject = deSerialize(request, Request.class);
                // Extract the token from the request through decryption and deserialization.
                Token token = deSerialize(decrypt(requestObject.tokenTS, keyTS), Token.class);
                // Extract the taskId.
                String taskId = requestObject.taskId;
                // Perform the execution and store the result in the reply.
                reply = manager.executeTask(taskId, token);
            } catch (Exception ex) {
                // If an error occurred, 'log' it...
                System.out.println("Received faulty request: " + request);
                // ...store the error in the reply...
                reply = "Error: " + ex.getMessage();
                // ... and log the error.
                System.out.println(reply);
            } finally {
                // Finally, transmit the stored reply.
                oC.transmit(reply);
            }
        }
    }
}