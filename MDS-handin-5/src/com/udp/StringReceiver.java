package com.udp;

import java.io.*;
import java.net.*;
import com.Receiver;
import com.Transmitter;

/**
 * A receiver which receives Strings through UDP.
 */
public class StringReceiver implements Receiver<String> {

    private final DatagramSocket socket;
    private final byte[] buffer = new byte[65508];
    private DatagramPacket latest;
    private boolean verbose = false;

    /**
     * Constructs a UdpReceiver with port = 4444 and bufferLength = 256.
     *
     * @throws SocketException if the socket could not be opened, or the socket
     * could not bind to the specified local port.
     */
    public StringReceiver() throws SocketException {
        this(new DatagramSocket(4445));
    }

    /**
     * Constructs a UdpReceiver.
     *
     * @param socket The socket on which the receiver should listen for
     * messages.
     * @throws SocketException if the socket could not be opened, or the socket
     * could not bind to the specified local port.
     */
    public StringReceiver(DatagramSocket socket) throws SocketException {
        this.socket = socket;
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
        String message = new String(latest.getData(), 0, latest.getLength());
        if (verbose) {
            System.out.println("[<-IN--] " + message);
        }
        return message;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    @Override
    public Transmitter<String> getTransmitter() {
        try {
            return new StringTransmitter(latest);
        } catch (SocketException ex) {
            System.out.println(ex);
            return null;
        }
    }
}