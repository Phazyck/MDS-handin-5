package serialization.envelope;

import java.io.Serializable;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Envelope implements Serializable {

    /**
     * Try out serialization/deSerialization of this class.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws JAXBException {
        Envelope e = new Envelope("a", "b");
        System.out.printf("command:%s data:%s\n", e.command, e.data);
        String xml = serialization.util.Serializer.serialize(e);
        System.out.println(xml);
        e = serialization.util.Serializer.deSerialize(xml, Envelope.class);
        System.out.printf("command:%s data:%s\n", e.command, e.data);
    }

    /**
     * Constructs an Envelope with blank data.
     */
    public Envelope() {
        this("", "");
    }

    /**
     * Constructs an Envelope by defining all its contents.
     *
     * @param command The command which at some point will be invoked.
     * @param data The data needed to invoke the command.
     */
    public Envelope(String command, String data) {
        this.command = command;
        this.data = data;
    }
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
