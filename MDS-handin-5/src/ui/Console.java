package ui;

import java.io.*;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.*;
import serialization.*;
import taskmanager.*;
import taskmanager.local.LocalManager;
import taskmanager.remote.RemoteManager;

/**
 * Provides interaction with a TaskManager through the terminal.
 */
public class Console implements Runnable {

    BufferedReader input;
    TaskManager manager;

    /**
     * Initializes the console with a little help from the user.
     */
    public Console() throws UnknownHostException, SocketException {
        input = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            out("Which kind of TaskManager would you like to test?");
            out(" - [local]Manager");
            out(" - [remote]Manager");
            switch (in().toLowerCase()) {
                case "local":
                    manager = new LocalManager();
                    return;
                case "remote":
                    manager = new RemoteManager();
                    return;
                default:
                    wrongInput();
            }
        }
    }

    /**
     * Runs the Client.
     */
    @Override
    public void run() {
        while (true) {
            out("Would you like to:");
            out(" - [execute] a task?");
            out(" - list all [users]?");
            out(" - list all [tasks] attended by a given user?");
            out(" - display all details about a given [task]?");
            out(" - [exit]?");
            switch (in().toLowerCase()) {
                case "execute":
                    executeTask();
                    break;
                case "users":
                    listUsers();
                    break;
                case "tasks":
                    listTasks();
                    break;
                case "task":
                    displayTask();
                    break;
                default:
                    wrongInput();
            }
            System.out.println("\n");
        }
    }

    /**
     * Starts the [execute] dialog.
     */
    private void executeTask() {
        out("What is the id of the task you want to execute?");
        if(manager.executeTask(in())) {
            out("Task executed.");
        } else {
            out("Task couldn't be executed.");
            out("Some conditions might not have been fulfilled.");
        }
    }

    /**
     * Starts the [users] dialog.
     */
    private void listUsers() {
        out("Listing all users:");
        for (User u : manager.getUsers()) {
            out("  " + u.name);
        }
    }

    /**
     * Starts the [tasks] dialog.
     */
    private void listTasks() {
        out("What is the id of the attendant whose tasks you want to list?");
        String attendantId = in();
        Tasks tasks = manager.getAttendantTasks(attendantId);

        if (tasks.size() == 0) {
            out(attendantId + " isn't attending any tasks.");
        } else {
            for (Task t : tasks) {
                out("  " + t.id);
            }
        }
    }

    /**
     * Starts the [task] dialog.
     */
    private void displayTask() {
        out("What is the id of the task you want to display?");

        Task task = manager.getTask(in());

        if (task != null) {
            out("Displaying " + task.id + ":");
            out("name:");
            out("  " + task.name);
            out("date:");
            out("  " + task.date);
            out("status:");
            out("  " + task.status);
            out("required:");
            out("  " + task.required);
            out("description:");
            out("  " + task.description);
            out("attendants:");
            out("  " + task.attendants);
            out("conditions:");
            out("  " + task.conditions);
            out("responses:");
            out("  " + task.responses);
        } else {
            out("No such task.");
        }
    }

    /**
     * Gets input from the user.
     *
     * @return The input.
     */
    private String in() {
        System.out.print("> ");
        System.out.flush();
        try {
            String line = input.readLine();
            if (line.equalsIgnoreCase("exit")) {
                out("Bye-bye!");
                System.exit(0);
            }
            return line;
        } catch (IOException ex) {
            return ex.toString();
        }
    }

    /**
     * Prints out a string, using System.out.println(String x);
     *
     * @param x The string.
     */
    private void out(String x) {
        System.out.println(x);
    }

    /**
     * A dialog which is started when unknown input is entered.
     */
    private void wrongInput() {
        out("Please type one of the [options] displayed in square brackets.");
        out("You can also [exit] when you're done.\n");
    }

    /**
     * Starts the TaskManager testing console.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws UnknownHostException, SocketException {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(new Console());
        exec.shutdown();
    }
}
