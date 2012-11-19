package security;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.*;
import serialization.secure.*;

/**
 * A service which will provide Tokens in exchange for valid credentials.
 */
public class TokenService {

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

        Credentials c = new Credentials(user, password);

        TokenService ts = new TokenService();       
        
        System.out.println(ts.getToken(c));                              
    }
    
    private HashMap<String, String> userRoles;
    
    public TokenService() {
        userRoles = new HashMap<>();
        userRoles.put("olpr", "student");
        userRoles.put("bhas", "student");
        userRoles.put("thomas", "teacher");
        userRoles.put("rao", "ta");
    }
    
    public Token getToken(Credentials c) {
        if(areVerifiable(c)) {
            return new Token(getRole(c.user));
        } else {
            return new Token();
        }
    }
    
    private String getRole(String user) {
        String role = userRoles.get(user);
        if(role == null) {
            userRoles.put(user, "none");
        }        
        return userRoles.get(user);
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
}
