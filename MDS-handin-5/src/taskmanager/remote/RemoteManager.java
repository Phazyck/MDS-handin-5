package taskmanager.remote;

import serialization.envelope.Envelope;
import java.net.*;
import javax.xml.bind.JAXBException;
import remote.*;
import remote.udp.string.UdpStringTransmitter;
import serialization.*;
import static serialization.util.Serializer.*;
import taskmanager.TaskManager;

/**
 * A TaskManager which manages its content remotely.
 */
public class RemoteManager implements TaskManager {

    private Transmitter<String> out;

    /**
     * Construct a RemoteManager which will communicate remotely through a
     * UdpStringTransceiver.
     */
    public RemoteManager() throws UnknownHostException, SocketException {
        this(null);
    }

    /**
     * Constructs a RemoteManager which will communicate remotely through a
     * given transceiver.
     *
     * @param out The string Transmitter which will enable the RemoteManager to
     * communicate with the RemoteManagerHost. Will use new
     * UdpStringTransmitter() if given 'null'.
     */
    public RemoteManager(Transmitter<String> out) throws UnknownHostException, SocketException {
        this.out = out == null ? new UdpStringTransmitter() : out;
    }

    @Override
    public boolean executeTask(String taskId) {
        String reply = getReply(new Envelope("execute", taskId));
        return Boolean.getBoolean(reply);
    }

    @Override
    public Users getUsers() {
        try {
            String reply = getReply(new Envelope("users", ""));
            return deSerialize(reply, Users.class);
        } catch (JAXBException ex) {
            System.out.println(ex);
            return null;
        }
    }

    @Override
    public Tasks getAttendantTasks(String attendantId) {
        try {
            String reply = getReply(new Envelope("tasks", attendantId));
            return deSerialize(reply, Tasks.class);
        } catch (JAXBException ex) {
            System.out.println(ex);
            return null;
        }
    }

    @Override
    public Task getTask(String taskId) {
        try {
            String reply = getReply(new Envelope("task", taskId));
            return deSerialize(reply, Task.class);
        } catch (JAXBException ex) {
            System.out.println(ex);
            return null;
        }
    }

    private String getReply(Envelope request) {
        try {
            out.transmit(serialize(request));
            Receiver<String> in = out.getReceiver();
            return in.receive();
        } catch (JAXBException ex) {
            return ex.toString();
        }
    }
}
