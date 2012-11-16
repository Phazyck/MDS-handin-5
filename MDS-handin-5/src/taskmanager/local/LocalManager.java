package taskmanager.local;

import concurrent.AutoSave;
import java.io.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.xml.bind.*;
import serialization.*;
import taskmanager.TaskManager;

/**
 * A TaskManager which manages its content locally in an asynchronous manner.
 */
public class LocalManager implements TaskManager {

    private final String path;
    private Cal cal = new Cal();
    private FileInputStream in;
    private Executor exec;
    

    /**
     * TODO
     */
    public LocalManager() {
        this("lib\task-manager-revised.xml");
    }

    /**
     * Uses a xml from a certain path.
     * @param path
     */
    public LocalManager(String p) {
        path = p;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(path);
            out = new FileOutputStream(path);            
            cal = readCal();
        } catch (FileNotFoundException | JAXBException ex) {
            System.out.println(ex);
            cal = new Cal();
        }
        exec = Executors.newSingleThreadExecutor();
        exec.execute(new AutoSave(cal, out, 5));     
    }

    private Cal readCal() throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(Cal.class);
        return (Cal) context.createUnmarshaller().unmarshal(in);
    }


    @Override
    public boolean executeTask(String taskId) {
        Task task = getTask(taskId);

        //If the task's conditions aren't executed or some conditions still are required we don't do anything.
        //If the conditions are executed and no are required, we set the task as executed and not required. 
        for (String c : task.conditionsAsList()) {
            if (!getTask(c).isExecuted() || getTask(c).isRequired()) {
                return false;
            } else {
                synchronized (task) {
                    task.setExecuted(true);
                    task.setRequired(false);
                }
            }
        }

        //We set the response tasks as required
        for (String r : task.responsesAsList()) {
            Task t = getTask(r);
            synchronized (t) {
                t.setRequired(true);
            }
        }

        return true;
    }

    @Override
    public Users getUsers() {
        return cal.users;
    }

    @Override
    public Tasks getAttendantTasks(String attendantId) {
        List<Task> matches = new ArrayList<>();
        for (Task t : cal.tasks) {
            if (t.attendantsAsList().contains(attendantId)) {
                matches.add(t);
            }
        }

        return new Tasks(matches);
    }

    @Override
    public Task getTask(String taskId) {
        for (Task t : cal.tasks) {
            if (t.id.equals(taskId)) {
                return t;
            }
        }
        return null;
    }
}
