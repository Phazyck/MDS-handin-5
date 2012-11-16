package serialization;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;

/**
 * A java equivalent to '<cal>' as it is defined in task-manager-revised.xml.
 * Used for serialization/deSerialization to/from java/xml.
 */
@XmlRootElement
public class Cal implements Serializable {

    /**
     * Try out serialization/deSerialization of this class.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws JAXBException {
        User u1 = new User("a", "b");
        User u2 = new User("1", "2");
        Task t1 = new Task("a", "b", "c", "d", "e", "f", "g", "h", "i");
        Task t2 = new Task("1", "2", "3", "4", "5", "6", "7", "8", "9");
        List<User> us = new ArrayList<>();
        List<Task> ts = new ArrayList<>();
        us.add(u1);
        us.add(u2);
        ts.add(t1);
        ts.add(t2);
        Users u = new Users(us);
        Tasks t = new Tasks(ts);
        Cal c = new Cal(u, t);

        for (User x : c.users) {
            System.out.printf("name:%s password:%s\n", x.name, x.password);
        }
        for (Task x : c.tasks) {
            System.out.printf("id:%s name:%s date:%s status:%s required:%s description:%s attendants:%s conditions:%s responses:%s\n", x.id, x.name, x.date, x.status, x.required, x.description, x.attendants, x.conditions, x.responses);
        }

        String xml = serialization.util.Serializer.serialize(c);
        System.out.println(xml);
        c = serialization.util.Serializer.deSerialize(xml, Cal.class);
        for (User x : c.users) {
            System.out.printf("name:%s password:%s\n", x.name, x.password);
        }
        for (Task x : c.tasks) {
            System.out.printf("id:%s name:%s date:%s status:%s required:%s description:%s attendants:%s conditions:%s responses:%s\n", x.id, x.name, x.date, x.status, x.required, x.description, x.attendants, x.conditions, x.responses);
        }
    }

    /**
     * Constructs an empty Cal.
     */
    public Cal() {
        this(new Users(), new Tasks());
    }

    /**
     * Constructs a Cal from Users and Tasks.
     *
     * @param users A list of 'User's.
     * @param tasks A list of 'Task's.
     */
    public Cal(Users users, Tasks tasks) {
        this.users = users;
        this.tasks = tasks;
    }
    /**
     * A list of 'User's.
     */
    @XmlElement
    public Users users;
    /**
     * A list of 'Task's.
     */
    @XmlElement
    public Tasks tasks;
}