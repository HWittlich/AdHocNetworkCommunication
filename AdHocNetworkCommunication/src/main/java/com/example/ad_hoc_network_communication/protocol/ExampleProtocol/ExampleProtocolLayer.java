package com.example.ad_hoc_network_communication.protocol.ExampleProtocol;

import com.example.ad_hoc_network_communication.device.AbstractDevice;
import com.example.ad_hoc_network_communication.protocol.AbstractProtocolLayer;

/**
 * This class is an implementation stub of an AbstractProtocolLayer subclass, for demonstration purposes.  You would implement your own version of this.
 */
public class ExampleProtocolLayer extends AbstractProtocolLayer {

    private ExampleProtocolListener protocolListener;


    /**
     * Constructor to use with additional abstraction layers on top.
     *
     * @param device
     */
    public ExampleProtocolLayer(AbstractDevice device) {
        super(device);
    }

    /**
     * Constructor that registers an ExampleProtocolListener to itself and registers itself as DeviceListener to an AbstractDevice.
     * Use this with an Activity Class as ExampleProtocolListener
     *
     * @param exampleProtocolListener the listener
     * @param device                  the network device
     */
    public ExampleProtocolLayer(ExampleProtocolListener exampleProtocolListener, AbstractDevice device) {
        super(device);
        this.protocolListener = exampleProtocolListener;
    }


    // Protocol methods and functionalities

    /**
     * Broadcasts a message to the whole network
     *
     * @param message the message to be send
     */
    public void sendBroadcast(String message) {
        device.send("BROADCAST,6TTL," + message);
    }

    /**
     * Sends a neighbor discovery request to all nodes in range
     */
    public void sendNeighborDiscovery() {
        device.send("DISCOVERY,1TTL,Is anyone out there?");
    }

    /**
     * sends a Reset Message to the network
     */
    public void sendNetworkReset() {
        device.send("RESET");
    }

    // DeviceListener methods

    @Override
    public void onConnect() {
        protocolListener.onConnect();
        System.out.println("Successfully connected.");
    }


    @Override
    public void onDeviceMessageReceived(String message) {
        protocolListener.onDeviceMessageReceived(message);
    }

    @Override
    public void onNetworkMessageReceived(String message) {
        if (message.startsWith("DISCOVERY")) {
            protocolListener.onDiscoveryMessageReceived(message);
            // some code to answer to an incoming Discovery request
        } else if (message.startsWith("RESET")) {
            protocolListener.onResetMessageReceived(message);
            // some code to initiate a reset
        } else {
            protocolListener.onBroadcastMessageReceived(message);
            // some code to decide whether to forward the incoming broadcast to the neighbors or not
            // can be implemented here or in the class implementing ExampleProtocolListener
        }
    }


    @Override
    public void onError(Exception e) {
        protocolListener.onError(e);
        System.out.println("Exception received on ProtocolLayer: " + e.getMessage());
    }

    // AbstractProtocolLayer methods

    @Override
    public void connect() {
        device.connect();
        System.out.println("Called connect() on Protocollayer");
    }

    @Override
    public void disconnect() {
        device.disconnect();
        System.out.println("Called disconnect() on Protocollayer");
    }
}
