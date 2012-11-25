package security;

import com.jcraft.jsch.*;
import comm.*;
import java.io.*;
import java.net.DatagramSocket;
import java.security.Key;
import java.util.*;
import java.util.logging.Level;
import static security.Encryption.*;
import security.serialization.*;
import static serialization.Serializer.*;
import taskmanager.secure.Server;

/**
 * A service which will provide Tokens in exchange for valid credentials.
 */
public class TokenService implements Runnable {
    // The port which will be used for incoming requests.

    public static final int REQPORT = 3000;
    // The port which will be used for establishing a key with the Server.
    public static final int KEYPORTS = 3001;
    // The port which will be used for establishing a key with the Client.
    public static final int KEYPORTC = 3002;
    // The secret key which is formed between the TokenService and the Server.
    private Key keyTS;
    // The secret key which is formed between the Client and the TokenService.
    private Key keyCT;
    // A HashMap, mapping users to roles.
    private HashMap<String, String> userRoles;
    // Transmitter used for outgoing messages to the Server.
    private Transmitter<String> outS;
    // Receiver used for incoming request messages.
    private Receiver<String> in;

    /**
     * Initialize the TokenService.
     */
    public TokenService() {
        try {
            // Accept a key suggested by a Server on the "KEYPORTS".
            keyTS = acceptKey(new comm.udp.StringReceiver(new DatagramSocket(KEYPORTS)));
            // Accept a key suggested by a Client on the "KEYPORTC".
            keyCT = acceptKey(new comm.udp.StringReceiver(new DatagramSocket(KEYPORTC)));
            // Prepare the Server Transmitter.
            outS = new comm.udp.StringTransmitter("localhost", Server.REQPORT);
            // Prepare the request receiver.
            in = new comm.udp.StringReceiver(new DatagramSocket(REQPORT));
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TokenService.class.getName()).log(Level.SEVERE, null, ex);
        }

        userRoles = new HashMap<>();
        userRoles.put("olpr", "student");
        userRoles.put("bhas", "student");
        userRoles.put("thomas", "teacher");
        userRoles.put("rao", "ta");

    }

    private String getToken(Credentials c) {
        Token token;
        if (areVerifiable(c)) {
            token = new Token(getRole(c.user), getTime());
            
        } else {
            token = new Token();            
        }
        
        String result = "";
        try {
            result = encrypt(serialize(token), keyTS);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();            
        }
        
        return result;
    }

    private String getRole(String user) {
        String role = userRoles.get(user);
        if (role == null) {
            userRoles.put(user, "none");
        }
        return userRoles.get(user);
    }

    private String getTime() {
        // Send a request for the time to the Server.
        outS.transmit("time");
        // Return the reply.
        return outS.getReceiver().receive();
    }

    private boolean areVerifiable(Credentials c) {
        try {
            Session session = (new JSch()).getSession(c.user, "ssh.itu.dk");
            session.setPassword(c.password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.disconnect();
            return true;
        } catch (JSchException e) {
            return false;
        }
    }

    @Override
    public void run() {
        // Run the TokenService until it is forcefully shut down.
        while (true) {
            // Check for incoming requests from the Client.
            String request = in.receive();
            // Prepare a reply transmitter.
            Transmitter<String> out = in.getTransmitter();
            // Prepare the reply token.
            String reply = "";
            
            try {
                Credentials credentials = deSerialize(decrypt(request, keyCT), Credentials.class);
                reply = encrypt(serialize(getToken(credentials)), keyCT);
            } catch (Exception e) {
                System.out.println("An error occured: " + e.getMessage());
                e.printStackTrace();
            } finally {
                out.transmit(reply);
            }
        }
    }

    /**
     * Run the TokenService.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws IOException, Exception {
        Thread tokenService = new Thread(new TokenService());
        tokenService.start();
    }
}
