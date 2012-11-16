package serialization;

import java.io.Serializable;
import java.util.*;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;

/**
 * A java equivalent to a '<task>' as it is defined in task-manager-revised.xml.
 * Used for serialization/deSerialization to/from java/xml. Also contains
 * various methods, providing alternative representations of the data as it is
 * defined in task-manager-revised.xml.
 */
@XmlRootElement
public class Task implements Serializable {

    /**
     * Try out serialization/deSerialization of this class.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws JAXBException {
        Task t = new Task("a", "b", "c", "d", "e", "f", "g", "h", "i");
        System.out.printf("id:%s name:%s date:%s status:%s required:%s description:%s attendants:%s conditions:%s responses:%s\n", t.id, t.name, t.date, t.status, t.required, t.description, t.attendants, t.conditions, t.responses);
        String xml = serialization.util.Serializer.serialize(t);
        System.out.println(xml);
        t = serialization.util.Serializer.deSerialize(xml, Task.class);
        System.out.printf("id:%s name:%s date:%s status:%s required:%s description:%s attendants:%s conditions:%s responses:%s\n", t.id, t.name, t.date, t.status, t.required, t.description, t.attendants, t.conditions, t.responses);
    }

    /**
     * Constructs a Task with blank data.
     */
    public Task() {
        this("", "", "", "", "", "", "", "", "");
    }

    /**
     * Constructs a Task by defining all its data.
     *
     * @param id The ID of the task. (e.g. handin-01).
     * @param name The name of the task, (e.g. Submit assignment-01).
     * @param date The date of the task, (format: DD-MM-YYYY).
     * @param status The task status, (executed|not-excecuted).
     * @param required Describes whether the task is required. (true|false).
     * @param description A description of the task, (e.g. Work on mandatory
     * assignment and send hand-in to Rao.).
     * @param attendants Names of all users attending the task, (format: user1,
     * user2, ..., usern).
     * @param conditions IDs of all condition tasks, (format: id1, id2, ...,
     * idn).
     * @param responses IDs of all repsonse tasks, (format: id1, id2, ..., idn).
     */
    public Task(String id, String name, String date, String status, String required, String description, String attendants, String conditions, String responses) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
        this.required = required;
        this.description = description;
        this.attendants = attendants;
        this.conditions = conditions;
        this.responses = responses;
    }
    /**
     * The ID of the task. (e.g. handin-01).
     */
    @XmlAttribute
    /**
     * The name of the task, (e.g. Submit assignment-01).
     */
    public String id;
    /**
     * The date of the task, (format: DD-MM-YYYY).
     */
    @XmlAttribute
    public String name;
    /**
     * The date of the task, (format: DD-MM-YYYY).
     */
    @XmlAttribute
    public String date;

    /**
     * A Task status is either "executed" or "not-executed". If a Task is
     * executed, it has been executed at least once. If not, is has yet to be
     * executed for the first time.
     *
     * @return True if the task status is "executed", otherwise false.
     */
    public boolean isExecuted() {
        return status.equalsIgnoreCase("executed") ? true : false;
    }
    
    public void setExecuted(boolean executed) {
        status = executed ? "executed" : "not-executed";
    }
    /**
     * The task status, (executed|not-excecuted).
     */
    @XmlAttribute
    public String status;

    /**
     * If a Task is required, other Tasks having that task in their conditions,
     * will be unable to execute, before it is no longer required.
     *
     * @return True if it is true that the task is required, otherwise false.
     */
    public boolean isRequired() {
        return required.equalsIgnoreCase("true") ? true : false;
    }
    
    public void setRequired(boolean required) {
        this.required = "" + required;
    }
    /**
     * Describes whether the task is required. (true|false).
     */
    @XmlAttribute
    public String required;
    /**
     * A description of the task, (e.g. Work on mandatory assignment and send
     * hand-in to Rao.).
     */
    @XmlElement
    public String description;

    /**
     * Splits up the attendants string into an attendant string list.
     *
     * @return A string list containing attendant IDs.
     */
    public List<String> attendantsAsList() {
        return Arrays.asList(attendants.split(", "));

    }
    /**
     * Names of all users attending the task, (format: user1, user2, ...,
     * usern).
     */
    @XmlElement
    public String attendants;

    /**
     * Split up the conditions string into a condition string list.
     *
     * @return A string list containing condition (task) IDs.
     */
    public List<String> conditionsAsList() {
        return Arrays.asList(conditions.split(", "));
    }
    /**
     * IDs of all condition tasks, (format: id1, id2, ..., idn).
     */
    @XmlElement
    public String conditions;

    /**
     * Split up the responses string into a response string list.
     *
     * @return A string list containing response (task) IDs.
     */
    public List<String> responsesAsList() {
        return Arrays.asList(responses.split(", "));
    }
    /**
     * IDs of all repsonse tasks, (format: id1, id2, ..., idn).
     */
    @XmlElement
    public String responses;
}