package remote.udp.string;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import remote.Receiver;
import remote.Transmitter;

/**
 * A receiver which receives Strings through UDP.
 */
public class UdpStringReceiver implements Receiver<String> {

    private DatagramSocket socket;
    private byte[] buffer;
    private DatagramPacket latest;

    /**
     * Constructs a UdpReceiver with port = 4445 and bufferLength = 256.
     *
     * @throws SocketException if the socket could not be opened, or the socket
     * could not bind to the specified local port.
     */
    public UdpStringReceiver() throws SocketException {
        this(4445, 256);
    }

    /**
     * Constructs a UdpReceiver.
     *
     * @param port The port on which the receiver should listen for messages.
     * @param bufferLength The length of the buffer.
     * @throws SocketException if the socket could not be opened, or the socket
     * could not bind to the specified local port.
     */
    public UdpStringReceiver(int port, int bufferLength) throws SocketException {
        socket = new DatagramSocket(port);
        buffer = new byte[bufferLength];
    }

    /**
     * Receive a string from a transmitter.
     *
     * @return The string.
     */
    @Override
    public String receive() {
        latest = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(latest);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return new String(latest.getData(), 0, latest.getLength());
    }  

    @Override
    public Transmitter<String> getTransmitter() {
        try {
            return new UdpStringTransmitter(latest);
        } catch (SocketException ex) {
            System.out.println(ex);
            return null;
        }
    }
}