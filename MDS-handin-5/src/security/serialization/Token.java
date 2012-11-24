package security.serialization;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.*;

@XmlRootElement
/**
 * A class containing data used to grant access to certain privileges on the
 * server.
 */
public class Token implements Serializable {
    
        /**
     * Try out serialization/deSerialization of this class.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws javax.xml.bind.JAXBException {
        Token t = new Token("a");
        System.out.printf("role:%S timestamp:%s\n", t.role, t.timestamp);        
        String xml = serialization.Serializer.serialize(t);
        System.out.println(xml);
        t = serialization.Serializer.deSerialize(xml, Token.class);
        System.out.printf("role:%S timestamp:%s\n", t.role, t.timestamp);
    }

    /**
     * Initialize an empty Token.
     */
    public Token() {
        this("none");
    }

    /**
     * Initialize a Token with the given data.
     *
     * @param role The role, which defines the class of privileges.
     * privileges.
     */
    public Token(String role) {
        this.role = role;
        this.timestamp = new Date();
    }
    /**
     * The role, which defines the class of privileges.
     */
    @XmlElement
    public String role;
    /**
     * The timestamp, which defines the time limit of the given privileges.
     */
    @XmlElement
    public Date timestamp;
    
    @Override
    public String toString() {
        return role + ", " + timestamp;
    }
}
