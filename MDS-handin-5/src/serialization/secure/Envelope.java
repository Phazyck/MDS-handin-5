package serialization.secure;

import java.io.Serializable;
import javax.xml.bind.annotation.*;

@XmlRootElement
public class Envelope implements Serializable {

    /**
     * Try out serialization/deSerialization of this class.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws javax.xml.bind.JAXBException {
        Envelope e = new Envelope("a", "b", "c");
        System.out.printf("token:%s command:%s data:%s\n", e.token, e.command, e.data);
        String xml = serialization.util.Serializer.serialize(e);
        System.out.println(xml);
        e = serialization.util.Serializer.deSerialize(xml, Envelope.class);
        System.out.printf("token:%s command:%s data:%s\n", e.token, e.command, e.data);
    }

    /**
     * Constructs an Envelope with blank data.
     */
    public Envelope() {
        this("", "", "");
    }
    
    /**
     * Constructs an Envelope with no token.
     *
     * @param command The command which at some point will be invoked.
     * @param data The data needed to invoke the command.
     */
    public Envelope(String command, String data) {
        this("", command, data);
    }

    /**
     * Constructs an Envelope by defining all its contents.
     *
     * @param token The token which grants the required privileges.
     * @param command The command which at some point will be invoked.
     * @param data The data needed to invoke the command.
     */
    public Envelope(String token, String command, String data) {
        this.token = token;
        this.command = command;
        this.data = data;
    }
    
    /**
     * The token which grants the required privileges.
     */
    @XmlElement
    public String token;
    
    /**
     * The command which at some point will be invoked.
     */
    @XmlElement
    public String command;
    /**
     * The data needed to invoke the command.
     */
    @XmlElement
    public String data;
}
