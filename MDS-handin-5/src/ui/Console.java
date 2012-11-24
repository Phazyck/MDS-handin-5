package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import security.serialization.Credentials;
import taskmanager.ITaskManager;
import taskmanager.secure.Client;

public class Console implements Runnable {

    private final ITaskManager manager;
    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public Console() {
        System.out.println("Welcome to the Task Manager client.");
        System.out.println("You can type [exit] at any point to exit the application.\n");        
        System.out.println("Please enter your ITU username:");
        String username = in();
        System.out.println("Please enter your ITU password:");
        String password = in();
        ITaskManager tmp = null;
        try {
            tmp = new Client(new Credentials(username, password));
        } catch (Exception e) {
            System.out.println("An error occurred when preparing the client: " + e.getMessage());
            System.exit(1);
        } finally {
            manager = tmp;
        }
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("What is the id of the task you want to execute?");
            if (manager.executeTask(in())) {
                System.out.println("Task executed.");
            } else {
                System.out.println("Task couldn't be executed.");
            }
        }
    }
    
    /**
     * Get input from the user.
     *
     * @return The input.
     */
    private String in() {
        System.out.print("> ");
        System.out.flush();
        try {
            String line = input.readLine();
            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Bye-bye!");
                System.exit(0);
            }
            return line;
        } catch (IOException ex) {
            return ex.toString();
        }
    }    
}