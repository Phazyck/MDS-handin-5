package taskmanager.secure;

import comm.*;
import java.net.*;
import java.security.*;
import java.util.*;
import securemanager.*;
import static security.Encryption.*;
import security.TokenService;
import security.serialization.*;
import static serialization.Serializer.*;

public class Server implements Runnable {

    // The port which will be used for incoming requests.
    public static final int REQPORT = 1000;
    // Receiver used for incoming request messages.
    private Receiver<String> in;
    // The secret key which is formed between the TokenService and the Server.
    private Key keyTS;
    // The task manager.
    private ISecureManager manager;

    /**
     * Prepare the Server.
     */
    public Server() {
        try {
            in = new comm.udp.StringReceiver(new DatagramSocket(REQPORT));
            keyTS = suggestKey(new comm.udp.StringTransmitter("localhost", TokenService.KEYPORTS));
            manager = new SecureManager();
        } catch (Exception e) {
            System.out.println("An error occured: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        while (true) {
            // Receive a request.
            String request = in.receive();
            // Prepare a transmitter in order to send back a reply to the source.
            Transmitter<String> out = in.getTransmitter();
            // If the request is for the time...
            if (request.equalsIgnoreCase("time")) {
                // ...return the current time.
                out.transmit(new Date().toString());
            } else {
                // Else, prepare a reply.
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
                    out.transmit(reply);
                }
            }
        }
    }

    /**
     * Run the Server.
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        Thread server = new Thread(new Server());
        server.start();
    }
}