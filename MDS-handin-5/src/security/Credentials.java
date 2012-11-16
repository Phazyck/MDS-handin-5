package security;

import com.jcraft.jsch.*;
import java.io.*;

/**
 * A class for storing and checking ITU credentials through SSH.
 */
public class Credentials {

    /**
     * Use to test initialization and authenticity of Credentials.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws IOException {
        // Set up console I/O.
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintStream out = System.out;

        // Get the user name from console.
        out.println("Please enter your user name:");
        out.print("> ");
        out.flush();
        String user = in.readLine();

        // Get the password from console.
        out.println("Please enter your password:");
        out.print("> ");
        out.flush();
        String password = in.readLine();

        // Initialize Credentials and test their authenticity.
        if ((new Credentials(user, password)).areAuthentic()) {
            out.println("ITU credentials are authentic!");
        } else {
            out.println("ITU credentials are not authentic!");
        }
    }
    private final String user;
    private final String host = "ssh.itu.dk";
    private final String password;

    /**
     * Initialize a new pair of Credentials.
     *
     * @param user The ITU user name.
     * @param password The password for the corresponding user.
     */
    public Credentials(String user, String password) {
        this.user = user;
        this.password = password;
    }

    /**
     * Test whether the Credentials are authentic, by using them to log into
     * ssh.itu.dk.
     *
     * @return true if login succeeds, false if not.
     */
    public boolean areAuthentic() {
        try {
            Session session = (new JSch()).getSession(user, host);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.disconnect();
            return true;
        } catch (JSchException e) {
            return false;
        }
    }
}