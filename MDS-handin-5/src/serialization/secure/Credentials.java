package serialization.secure;

import java.io.Serializable;
import javax.xml.bind.annotation.*;

/**
 * A class for storing ITU credentials.
 */
@XmlRootElement
public class Credentials implements Serializable {

    /**
     * Try out serialization/deSerialization of this class.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws javax.xml.bind.JAXBException {
        Credentials c = new Credentials("user", "password");
        System.out.printf("user:%s password:%s\n", c.user, c.password);
        String xml = serialization.util.Serializer.serialize(c);
        System.out.println(xml);
        c = serialization.util.Serializer.deSerialize(xml, Credentials.class);
        System.out.printf("user:%s password:%s\n", c.user, c.password);
    }

    /**
     * Initialize an empty pair of Credentials.
     */
    public Credentials() {
        this("", "");
    }

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
     * The ITU user name.
     */
    @XmlElement
    public String user;
    /**
     * The password for the corresponding user.
     */
    @XmlElement
    public String password;
}