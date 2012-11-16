package serialization;

import java.io.*;
import java.util.*;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;

/**
 * A java equivalent to '<tasks>' as it is defined in task-manager-revised.xml.
 * Used for serialization/deSerialization to/from java/xml. Also contains
 * various utility methods.
 */
@XmlRootElement
public class Tasks implements Serializable, Iterable<Task> {

    /**
     * Try out serialization/deSerialization of this class.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws JAXBException {
        Task t1 = new Task("a", "b", "c", "d", "e", "f", "g", "h", "i");
        Task t2 = new Task("1", "2", "3", "4", "5", "6", "7", "8", "9");
        List<Task> ts = new ArrayList<>();
        ts.add(t1);
        ts.add(t2);
        Tasks t = new Tasks(ts);
        for (Task x : t) {
            System.out.printf("id:%s name:%s date:%s status:%s required:%s description:%s attendants:%s conditions:%s responses:%s\n", x.id, x.name, x.date, x.status, x.required, x.description, x.attendants, x.conditions, x.responses);
        }

        String xml = serialization.util.Serializer.serialize(t);
        System.out.println(xml);
        t = serialization.util.Serializer.deSerialize(xml, Tasks.class);
        for (Task x : t) {
            System.out.printf("id:%s name:%s date:%s status:%s required:%s description:%s attendants:%s conditions:%s responses:%s\n", x.id, x.name, x.date, x.status, x.required, x.description, x.attendants, x.conditions, x.responses);
        }
    }

    /**
     * Constructs an empty list of tasks.
     */
    public Tasks() {
        this.task = new ArrayList<>();
    }

    /**
     * Constructs a list of tasks from a provided list.
     *
     * @param tasks
     */
    public Tasks(List<Task> tasks) {
        this();
        task.addAll(tasks);
    }
    /**
     * A list of tasks.
     */
    @XmlElement
    public List<Task> task;

    /**
     * Allow to iterate through the list of tasks, by providing its iterator.
     *
     * @return A task iterator.
     */
    @Override
    public Iterator<Task> iterator() {
        return task.iterator();
    }

    /**
     * @return The amount of tasks.
     */
    public int size() {
        return task.size();
    }
}