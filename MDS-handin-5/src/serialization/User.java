package serialization;

import java.io.Serializable;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;

/**
 * A java equivalent to a '<user>' as it is defined in task-manager-revised.xml.
 * Used for serialization/deSerialization to/from java/xml.
 */
@XmlRootElement
public class User implements Serializable {

    /**
     * Try out serialization/deSerialization of this class.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws JAXBException {
        User u = new User("a", "b");
        System.out.printf("name:%s password:%s\n", u.name, u.password);
        String xml = serialization.util.Serializer.serialize(u);
        System.out.println(xml);
        u = serialization.util.Serializer.deSerialize(xml, User.class);
        System.out.printf("name:%s password:%s\n", u.name, u.password);
    }

    /**
     * Constructs a User with blank data.
     */
    public User() {
        this("", "");
    }

    /**
     * Constructs a User by defining all its data.
     *
     * @param name The users name, (e.g. student-01).
     * @param password The users password, (e.g. study).
     */
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
    /**
     * The users name, (e.g. student-01).
     */
    @XmlElement
    public String name;
    /**
     * The users password, (e.g. study).
     */
    @XmlElement
    public String password;
}
