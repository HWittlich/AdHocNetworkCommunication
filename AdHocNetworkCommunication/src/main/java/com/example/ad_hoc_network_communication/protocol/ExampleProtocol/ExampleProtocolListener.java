package com.example.ad_hoc_network_communication.protocol.ExampleProtocol;

import com.example.ad_hoc_network_communication.protocol.ProtocolListener;

public interface ExampleProtocolListener extends ProtocolListener {

    /**
     * Method handling messages from the underlying device itself (anything that is not a message from the network).
     *
     * @param message message from the device
     */
    void onDeviceMessageReceived(String message);

    /**
     * Method handling incoming messages from the network.
     *
     * @param message message from the network
     */
    void onBroadcastMessageReceived(String message);

    /**
     * Method handling incoming messages from the network.
     *
     * @param message message from the network
     */
    void onDiscoveryMessageReceived(String message);

    /**
     * Method handling incoming messages from the network.
     *
     * @param message message from the network
     */
    void onResetMessageReceived(String message);

}
