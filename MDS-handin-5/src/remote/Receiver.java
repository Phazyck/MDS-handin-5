package remote;

/**
 * An interface for an object which actively receives objects from
 * corresponding transmitters.
 *
 * @param <I> The type of the objects which will be received.
 */
public interface Receiver<I> {

    /**
     * Receive an object from a transmitter.
     *
     * @return The object.
     */
    public I receive();
    
    /**
     * Get a transmitter which transmits to the last source from which a message was received.
     * @return A Transmitter which can transmit back to the last message source.
     */
    public Transmitter<I> getTransmitter();
}