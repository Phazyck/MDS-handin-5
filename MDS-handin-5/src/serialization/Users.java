package serialization;

import java.io.*;
import java.util.*;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;

/**
 * A java equivalent to '<users>' as it is defined in task-manager-revised.xml.
 * Used for serialization/deSerialization to/from java/xml. Also contains
 * various utility methods.
 */
@XmlRootElement
public class Users implements Serializable, Iterable<User> {

    /**
     * Try out serialization/deSerialization of this class.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws JAXBException {
        User u1 = new User("a", "b");
        User u2 = new User("1", "2");
        List<User> us = new ArrayList<>();
        us.add(u1);
        us.add(u2);
        Users u = new Users(us);
        for (User x : u) {
            System.out.printf("name:%s password:%s\n", x.name, x.password);
        }
        String xml = serialization.util.Serializer.serialize(u);
        System.out.println(xml);
        u = serialization.util.Serializer.deSerialize(xml, Users.class);
        for (User x : u) {
            System.out.printf("name:%s password:%s\n", x.name, x.password);
        }
    }

    /**
     * Constructs an empty list of users.
     */
    public Users() {
        this.user = new ArrayList<>();
    }

    /**
     * Constructs a list of users from a provided list.
     *
     * @param tasks
     */
    public Users(List<User> users) {
        this();
        user.addAll(users);
    }
    /**
     * A list of users.
     */
    @XmlElement
    public List<User> user;

    /**
     * Allow to iterate through the list of users, by providing its iterator.
     *
     * @return A user iterator.
     */
    @Override
    public Iterator<User> iterator() {
        return user.iterator();
    }

    /**
     * @return The amount of users.
     */
    public int size() {
        return user.size();
    }
}
