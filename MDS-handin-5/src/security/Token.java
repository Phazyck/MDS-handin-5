package security;

/**
 * The token which will be received from the TokenService, 
 * after providing the correct credentials.
 */
public class Token {
    private final String stamp;
    private final String role;
    
    public Token(String stamp, String role) {
        this.stamp = stamp;
        this.role = role;
    }
}
