package taskmanager.secure;

import comm.*;
import java.io.*;
import java.security.*;
import security.*;
import static security.Encryption.*;
import security.serialization.*;
import static serialization.Serializer.*;

public class Client implements Runnable {

    // The ITU credentials required to request tokens.
    private String credentials;
    // The token which will be used to provide authorization in certain situations.
    private String tokenTS;
    // The input reader.
    private BufferedReader input;
    // Transmitter used for outgoing messages to the TokenService.
    private Transmitter<String> outT;
    // The secret key which is formed between the Client and the TokenService.
    private Key keyCT;
    // Transmitter used for outgoing messages to the Server.
    private Transmitter<String> outS;

    public Client() {
        try {
            // Prepare the input reader.
            input = new BufferedReader(new InputStreamReader(System.in));
            // Prepare the TokenService Transmitter.
            outT = new comm.udp.StringTransmitter("localhost", TokenService.KEYPORTC);
            // Form a secret key with the TokenService.
            keyCT = suggestKey(outT);
            // Prepare the Server Transmitter.
            outS = new comm.udp.StringTransmitter("localhost", Server.REQPORT);
            // Introduction.
            System.out.println("Welcome to the Task Manager client.");
            System.out.println("You can type [exit] at any point to exit the application.\n");
            // Ask for user name.
            System.out.println("Please enter your ITU username:");
            // Get user name.
            String username = in();
            // Ask for password.
            System.out.println("Please enter your ITU password:");
            // Get password.
            String password = in();
            // Fill out credentials.
            this.credentials = serialize(new Credentials(username, password));
            // Get a fresh token.
            renewToken();
        } catch (Exception e) {
            System.out.println("An error occurred when preparing the client: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Replace the Token with a new one with a more recent time stamp.
     */
    private void renewToken() {
        // Try to...
        try {
            // ...encrypt the credentials...
            String credentialsCT = encrypt(credentials, keyCT);
            // ...transmit them to the TokenService...
            outT.transmit(credentialsCT);
            // ...prepare a Receiver for the reply...
            Receiver<String> inT = outT.getReceiver();
            // ...and receive a (double-encrypted) token from the TokenService...
            String tokenTSCT = inT.receive();
            // ...which will be stored after removing the first layer of encryption.
            tokenTS = decrypt(tokenTSCT, keyCT);
        } catch (Exception ex) {
            // In case an error occurs, it should be 'logged'.
            System.out.println("An error occured when trying to renew the token: " + ex.getMessage());
        }
    }

    /**
     * Execute a Task.
     * @param taskId The ID of the Task.
     * @return The result of the execution.
     */
    private String executeTask(String taskId) {
        // Prepare a request object.
        Request requestObject = new Request(tokenTS, taskId);

        // Prepare a request message.
        String request;
        try {
            // Attempt to serialize the request object into a string.
            request = serialize(requestObject);
        } catch (Exception e) {
            // If an error occurs, return it as a message.
            return "An error occured during serialization: " + e.getMessage();

        }

        // Try at most 3 times to perform the execution.
        for (int i = 1; i <= 2; i++) {
            // Transmit the request.
            outS.transmit(request);
            // Prepare a Receiver for the reply.
            Receiver<String> inS = outS.getReceiver();
            // Get a reply.
            String reply = inS.receive();
            // Do different actions depending of the contents of the reply.
            switch (reply.toLowerCase()) {
                case "success":
                    return "The task has been successfully executed.";
                case "failure":
                    return "The task can't be executed.";
                case "role-mismatch":
                    return "You don't have the permission to execute this task.";
                case "token-expired":
                    // The token has expired and must be renewed.
                    renewToken();
                    // Then try again.
                    continue;
                default:
                    // Reply not recognized, return as error.
                    return "Errort: " + reply;
            }
        }
        
        return "Error: Unable to execute task.";
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("What is the id of the task you want to execute?");
            System.out.println(executeTask(in()));            
        }
    }

    /**
     * Get input from the user.
     *
     * @return The input.
     */
    private String in() {
        System.out.print("> ");
        System.out.flush();
        try {
            String line = input.readLine();
            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Bye-bye!");
                System.exit(0);
            }
            return line;
        } catch (IOException ex) {
            return ex.toString();
        }
    }

    /**
     * Run the Client.
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        Thread client = new Thread(new Client());
        client.start();
    }
}