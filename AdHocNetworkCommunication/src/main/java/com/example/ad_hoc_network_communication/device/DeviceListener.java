package com.example.ad_hoc_network_communication.device;

/**
 * Interface that specifies the necessary methods for classes that want to listen to an implementation of AbstractDevice
 */
public interface DeviceListener {

    /**
     * Method handling additional functionality when the device successfully connected. Is called from the implementation of an AbstractDevice class
     */
    void onConnect();

    /**
     * Method handling messages from the device itself (anything that is not a message from the network). Is called from the implementation of an AbstractDevice class
     *
     * @param message message from the device
     */
    void onDeviceMessageReceived(String message);

    /**
     * Method handling incoming messages from the network. Is called from the implementation of an AbstractDevice class
     *
     * @param message message from the device
     */
    void onNetworkMessageReceived(String message);

    /**
     * Method handling an error from the device or serial connection layer. Is called from the implementation of an AbstractDevice class
     *
     * @param e the exception that occured
     */
    void onError(Exception e);
}
