package com.example.ad_hoc_network_communication.protocol;

import com.example.ad_hoc_network_communication.device.AbstractDevice;
import com.example.ad_hoc_network_communication.device.DeviceListener;

/**
 * Base class to inherit from when developing new protocols
 */
public abstract class AbstractProtocolLayer implements DeviceListener {

    protected AbstractDevice device;

    /**
     * Constructor that registers this instance as Devicelistener to the device.
     *
     * @param device the network device that this network node listens to
     */
    public AbstractProtocolLayer(AbstractDevice device) {
        device.setDeviceListener(this);
        this.device = device;
    }

    /**
     * Method to connect to the device implementing this interface. Usually, this should call the connect() method of an AbstractSerialConnection
     * and optionally call code that is necessary to configure the connected device
     */
    public abstract void connect();

    /**
     * Method to connect to the device implementing this interface. Usually, this should call the disconnect() method of the AbstractSerialConnection
     */
    public abstract void disconnect();

    // Getter and Setter

    /**
     * Gets the device for this ProtocolLayer
     *
     * @return the device
     */
    public AbstractDevice getDevice() {
        return device;
    }

    /**
     * Sets the device for this ProtocolLayer
     *
     * @param device
     */
    public void setDevice(AbstractDevice device) {
        this.device = device;
    }


}
