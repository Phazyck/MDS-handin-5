package taskmanager.remote;

import concurrent.Action;
import java.net.*;
import java.util.concurrent.*;
import javax.xml.bind.JAXBException;
import remote.*;
import remote.udp.string.UdpStringReceiver;
import serialization.envelope.Envelope;
import static serialization.util.Serializer.*;
import taskmanager.TaskManager;
import taskmanager.local.LocalManager;

/**
 * A host to RemoteManagers content.
 */
public class RemoteManagerHost implements Runnable {

    private TaskManager manager;
    private Receiver<String> in;
    private Executor exec;

    /**
     * Constructs a RemoteManagerHost which hosts a LocalManager through UDP.
     */
    public RemoteManagerHost() throws SocketException {
        this(null, null);
    }

    /**
     * Constructs a RemoteManagerHost with a specific TaskManager and string
     * Receiver.
     *
     * @param manager The TaskManager which should be hosted. Will use new
     * LocalManager() if given 'null'.
     * @param in The string Receiver which should be used to receive messages.
     * Will use new UdpStringReceiver() if given 'null'.
     */
    public RemoteManagerHost(TaskManager manager, Receiver<String> in) throws SocketException {
        this.manager = manager == null ? new LocalManager() : manager;
        this.in = in == null ? new UdpStringReceiver() : in;
        this.exec = Executors.newCachedThreadPool();
    }

    /**
     * Receives incoming requests and spawns a new Action thread. The Action
     * thread will then handle the request and send back a reply.
     */
    @Override
    public void run() {
        while (true) {
            try {
                Envelope envelope = deSerialize(in.receive(), Envelope.class);
                Transmitter<String> out = in.getTransmitter();
                exec.execute(new Action(out, manager, envelope));
            } catch (JAXBException ex) {
                System.out.println(ex);
                continue;
            }
        }
    }
}
