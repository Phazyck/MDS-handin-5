package remote;

/**
 * An interface for an object which actively sends objects to corresponding
 * receivers.
 *
 * @param <O> The type of the objects which will be sent.
 */
public interface Transmitter<O> {

    /**
     * Transmit an object to a receiver.
     *
     * @param object The object.
     */
    public void transmit(O object);
    
    /**
     * Get a receiver which receives from the last target to which a message was transmitted.
     * @return A Receiver which can receive from the last message target.
     */
    public Receiver<O> getReceiver();
}
