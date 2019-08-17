package com.example.ad_hoc_network_communication.connection;

/**
 * Base class to inherit from when developing new Serial Connection classes.
 */

public abstract class AbstractSerialConnection {

    protected SerialInputListener serialInputListener;

    /**
     * Default constructor where SerialInputListener needs to be set manually by calling setSerialInputListener()
     */
    public AbstractSerialConnection() {
    }

    /**
     * Constructor that registers a SerialInputListener
     *
     * @param serialInputListener - the SerialinputListener to be registered
     */
    public AbstractSerialConnection(SerialInputListener serialInputListener) {
        this.serialInputListener = serialInputListener;
    }

    /**
     * Establishes the serial connection
     */
    public abstract void connect();

    /**
     * Disconnects the serial connection and does necessary clean-up work
     */
    public abstract void disconnect();

    /**
     * Writes data to the serial connection.
     *
     * @param data - data to be written
     */
    public abstract void write(byte[] data);

    // getter and setter

    /**
     * Gets the serialInputListener
     *
     * @return the serialInputListener
     */
    public SerialInputListener getSerialInputListener() {
        return serialInputListener;
    }

    /**
     * Sets the serialInputListener
     *
     * @param serialInputListener
     */
    public void setSerialInputListener(SerialInputListener serialInputListener) {
        this.serialInputListener = serialInputListener;
    }
}
