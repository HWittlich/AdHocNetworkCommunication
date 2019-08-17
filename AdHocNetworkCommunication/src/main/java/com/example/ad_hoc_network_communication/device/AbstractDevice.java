package com.example.ad_hoc_network_communication.device;

import com.example.ad_hoc_network_communication.connection.AbstractSerialConnection;
import com.example.ad_hoc_network_communication.connection.SerialInputListener;

/**
 * Base class to inherit from when adding new devices to the library
 */
public abstract class AbstractDevice implements SerialInputListener {

    protected DeviceListener deviceListener;
    protected AbstractSerialConnection serialConnection;

    /**
     * Constructor that registers this class as SerialInputListener to an AbstractSerialConnection
     *
     * @param serialConnection the AbstractSerialConnection that should be listened to
     */
    public AbstractDevice(AbstractSerialConnection serialConnection) {
        serialConnection.setSerialInputListener(this);
        this.serialConnection = serialConnection;
    }

    /**
     * Constructor that registers a DeviceListener to itself and registers itself as SerialInputListener to an AbstractSerialConnection
     *
     * @param deviceListener   the Devicelistener that listens to this device
     * @param serialConnection the subclass of AbstractSerialConnection that this device listens to
     */
    public AbstractDevice(DeviceListener deviceListener, AbstractSerialConnection serialConnection) {
        serialConnection.setSerialInputListener(this);
        this.serialConnection = serialConnection;
        this.deviceListener = deviceListener;
    }


    /**
     * Method to connect to the device implementing this interface. Usually, this should call the connect() method of an AbstractSerialConnection
     * and optionally call code that is necessary to configure the connected device
     */
    public abstract void connect();

    /**
     * Method to send a message to the network using the devices functionality to send. Should call the write(byte[] data) method of the AbstractSerialConnection
     * This method should encompass everything needed to send a message through the device to the network.
     *
     * @param message the message to be send to the network
     */
    public abstract void send(String message);

    /**
     * Method to send a control command to the device. This method gives developers the possibility of interacting with the device if need be
     *
     * @param command the command that should be sent to the device
     */
    public abstract void sendDeviceCommand(String command);

    /**
     * Method to connect to the device implementing this interface. Usually, this should call the disconnect() method of the AbstractSerialConnection
     */
    public abstract void disconnect();


    // Setter and Getter

    /**
     * Method to get the current DeviceListener.
     *
     * @return the DeviceListener object
     */
    public DeviceListener getDeviceListener() {
        return deviceListener;
    }

    /**
     * Method to set the DeviceListener.
     *
     * @param deviceListener the DeviceListener object
     */
    public void setDeviceListener(DeviceListener deviceListener) {
        this.deviceListener = deviceListener;
    }

    /**
     * Method to get the current AbstractSerialConnection.
     *
     * @return the AbstractSerialConnection object
     */
    public AbstractSerialConnection getSerialConnection() {
        return serialConnection;
    }

    /**
     * Method to set the DeviceListener.
     *
     * @param serialConnection the AbstractSerialConnection object
     */
    public void setSerialConnection(AbstractSerialConnection serialConnection) {
        this.serialConnection = serialConnection;
    }
}
