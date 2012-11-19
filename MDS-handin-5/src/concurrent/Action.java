package concurrent;

import javax.xml.bind.JAXBException;
import remote.Transmitter;
import serialization.secure.Envelope;
import static serialization.util.Serializer.*;
import taskmanager.TaskManager;

public class Action implements Runnable {

    private Transmitter<String> out;
    private TaskManager manager;
    private String command;
    private String data;

    public Action(Transmitter<String> out, TaskManager manager, Envelope description) {
        this.out = out;
        this.manager = manager;
        this.command = description.command;
        this.data = description.data;
    }

    @Override
    public void run() {
        String result = "";
        try {            
            switch (command.toLowerCase()) {
                case "execute":
                    result = "" + manager.executeTask(data);
                    break;
                case "users":
                    result = serialize(manager.getUsers());
                    break;
                case "tasks":
                    result = serialize(manager.getAttendantTasks(data));
                    break;
                case "task":
                    result = serialize(manager.getTask(data));
                    break;
                default :
                    result = "Command '" + command + "' not supported";
            }
        } catch (JAXBException ex) {
            result = "Exception: " + ex.toString();
        } finally {
            out.transmit(result);
        }
    }
}
