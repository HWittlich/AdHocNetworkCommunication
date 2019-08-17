package com.example.ad_hoc_network_communication.connection;

/**
 * Interface that specifies the necessary methods for classes that want to listen to an implementation of AbstractSerialConnection
 */
public interface SerialInputListener {

    /**
     * Method to handle successful connection
     */
    void onConnect();

    /**
     * Method to handle incoming serial data. Note that the incoming data packets may be fragments of the original message.
     * This method would handle putting those fragments back together into a message
     *
     * @param data incoming data
     */
    void onSerialDataReceived(byte[] data);

    /**
     * Method to handle errors encountered by a subclass of AbstractSerialConnection
     *
     * @param e error
     */
    void onError(Exception e);


}