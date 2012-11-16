package serialization.util;

import java.io.*;
import javax.xml.bind.*;

/**
 * Provides methods for serializing/deSerializing the classes contained in
 * serialization.common.
 */
public class Serializer {

    /**
     * Serializes an object (of type T extends Serializable) into its XML
     * representation.
     *
     * @param <T> The type of the serializable.
     * @param serializable The serializable object.
     * @return The XML representation of the serializable.
     * @throws JAXBException if an error occurred during serialization, e.g. if
     * the object is not serializable.
     */
    public static <T extends Serializable> String serialize(T serializable) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(serializable.getClass());
        StringWriter writer = new StringWriter();
        context.createMarshaller().marshal(serializable, writer);
        return writer.toString();
    }

    /**
     * Creates a serializable object from XML.
     *
     * @param <T> The type of the serializable.
     * @param xml The XML representation of the serializable.
     * @param target The target class which the XML should be serialized into.
     * @return The deSerialized serializable.
     * @throws JAXBException if an error occurred during deSerialization, e.g. if
     * the XML is faulty, or the target is unknown.
     */
    public static <T extends Serializable> T deSerialize(String xml, Class<T> target) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(target);
        return (T) context.createUnmarshaller().unmarshal(new StringReader(xml));
    }
}
