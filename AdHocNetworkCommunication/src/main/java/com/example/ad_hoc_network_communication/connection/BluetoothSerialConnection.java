package com.example.ad_hoc_network_communication.connection;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Executors;

/**
 * Subclass of AbstractSerialConnection, that creates a SerialConnection via Bluetooth and listens to incoming messages in a separate thread.
 */
public class BluetoothSerialConnection extends AbstractSerialConnection implements Runnable {

    private static final UUID SERIAL_PORT_PROFILE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Well-known address for Bluetooth Serial Boards, according to android documentation
    private String macAddress;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private boolean isConnected;
    private boolean disconnectInitiated = false;

    /**
     * Default constructor. SerialInputListener and macAddress need to be set manually calling their respective getters
     */
    public BluetoothSerialConnection() {
        super();
    }

    /**
     * Constructor with the MAC address of the target bluetooth device. A SerialInputListener needs to be set manually using the setter
     *
     * @param macAddress MAC address of the bluetooth device that will be connected when calling connect()
     */
    public BluetoothSerialConnection(String macAddress) {
        super();
        this.macAddress = macAddress;
    }

    /**
     * Full constructor for this class.
     *
     * @param serialInputListener class implementing the SerialInputListener interface
     * @param MacAddress          MAC address of the bluetooth device that will be connected when calling connect()
     */
    public BluetoothSerialConnection(SerialInputListener serialInputListener, String MacAddress) {
        super();
        this.serialInputListener = serialInputListener;
        this.macAddress = MacAddress;

    }


    /**
     * Method to open a serial connection to the target Bluetooth device. Is usually called through the IDevice connect() method.
     * Starts a new thread to listen to incoming messages.
     */
    @Override
    public void connect() {

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            serialInputListener.onError(new IOException("Your device doesn't support Bluetooth"));
        } else {
            // cancelling discovery to prevent potential connection problems
            bluetoothAdapter.cancelDiscovery();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(macAddress);
            String deviceName = device.getName() != null ? device.getName() : device.getAddress();
            this.bluetoothDevice = device;

            // check if the connection already exists
            if (isConnected || bluetoothSocket != null) {
                serialInputListener.onError(new IOException("Connection already established"));
            }
            Executors.newSingleThreadExecutor().submit(this); // run this as new Thread
        }
    }

    /**
     * Method to disconnect and close the BluetoothSocket
     */
    @Override
    public void disconnect() {

        disconnectInitiated = true;
        try {
            bluetoothSocket.getInputStream().close();
            bluetoothSocket.getOutputStream().close();
            bluetoothSocket.close();
        } catch (IOException e) {
            serialInputListener.onError(e);
        }
        bluetoothSocket = null;
        disconnectInitiated = false;
    }

    /**
     * Method to write data to the bluetooth serial socket
     *
     * @param data the data to be sent
     */
    @Override
    public void write(byte[] data) {
        if (!isConnected) {
            serialInputListener.onError(new IOException("Writing Data failed, not connected."));
        } else {
            try {
                bluetoothSocket.getOutputStream().write(data);
            } catch (Exception e) {
                serialInputListener.onError(new IOException("Writing Data failed. " + e.toString()));
            }
        }
    }

    /**
     * Method to run this connection in a thread. Reads buffer for incoming messages continuously.
     */
    @Override
    public void run() {
        // try to open a connection
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(SERIAL_PORT_PROFILE_UUID);
            bluetoothSocket.connect();
        } catch (Exception e) {
            serialInputListener.onError(e);
            try {
                bluetoothSocket.close();
            } catch (Exception ex) {
                serialInputListener.onError(ex);
            }
            bluetoothSocket = null;
            return;
        }

        isConnected = true;
        serialInputListener.onConnect();

        // Starting infinite loop to listen for input received on the bluetooth serial port
        try {

            byte[] buffer = new byte[1024];
            int incomingDataLength;

            while (true) {
                // determine the length of the incoming data and store data in buffer
                incomingDataLength = bluetoothSocket.getInputStream().read(buffer);
                // pass data on to serialInputListener
                byte[] data = Arrays.copyOf(buffer, incomingDataLength);
                serialInputListener.onSerialDataReceived(data);
            }

            // on encountering an exception, notify serialInputListener and try to close resources
        } catch (Exception e) {
            isConnected = false;
            if (!disconnectInitiated) {
                serialInputListener.onError(e);
                try {
                    bluetoothSocket.close();
                } catch (Exception ex) {
                    serialInputListener.onError(ex);
                } finally {
                    bluetoothSocket = null;
                }
            }
        }
    }

    // Getter and Setter for MAC address

    /**
     * Gets the MAC address of the BluetoothDevice to connect to
     *
     * @return MAC address
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * Gets the MAC address of the BluetoothDevice to connect to
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

}
