package comm.udp;

import java.io.*;
import java.net.*;
import comm.Receiver;
import comm.Transmitter;

/**
 * A transmitter which sends Strings through UDP.
 */
public class StringTransmitter implements Transmitter<String> {

    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private boolean verbose = false;

    /**
     * Constructs a UdpTransmitter with host = "localhost" and port = 4445.
     *
     * @throws UnknownHostException if no IP address for the host could be
     * found, or if a scope_id was specified for a global IPv6 address.
     * @throws SocketException if the socket could not be opened, or the socket
     * could not bind to the specified local port.
     */
    public StringTransmitter() throws UnknownHostException, SocketException {
        this("localhost", 4445);
    }

    /**
     * Constructs a UdpTransmitter.
     *
     * @param host The name of the host.
     * @param port The target port for transmission.
     * @throws UnknownHostException if no IP address for the host could be
     * found, or if a scope_id was specified for a global IPv6 address.
     * @throws SocketException if the socket could not be opened, or the socket
     * could not bind to the specified local port.
     */
    public StringTransmitter(String host, int port) throws UnknownHostException, SocketException {
        this(new DatagramPacket(new byte[256], 256, InetAddress.getByName(host), port));
    }

    /**
     * Construct a UdpTransmitter by getting destination details from a packet.
     *
     * @param packet The packet containing the destination details.
     * @throws SocketException if the socket could not be opened, or the socket
     * could not bind to the specified local port.
     */
    public StringTransmitter(DatagramPacket packet) throws SocketException {
        this.socket = new DatagramSocket();
        this.address = packet.getAddress();
        this.port = packet.getPort();
    }

    /**
     * Transmit an string to a receiver.
     *
     * @param message The message.
     */
    @Override
    public void transmit(String message) {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        if(verbose) {
            System.out.println("[-OUT->] " + message);
        } 
    }
    
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    @Override
    public Receiver<String> getReceiver() {
        try {
            return new StringReceiver(socket);
        } catch (SocketException ex) {
            System.out.println(ex);
            return null;            
        }
    }
}