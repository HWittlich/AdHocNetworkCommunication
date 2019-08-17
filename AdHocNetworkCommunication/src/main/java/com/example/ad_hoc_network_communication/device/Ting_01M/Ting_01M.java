package com.example.ad_hoc_network_communication.device.Ting_01M;

import com.example.ad_hoc_network_communication.connection.AbstractSerialConnection;
import com.example.ad_hoc_network_communication.device.AbstractDevice;
import com.example.ad_hoc_network_communication.device.DeviceListener;

/**
 * Exemplary implementation of the Ting-01M Device.
 * Documentation of AT commands can be found online at https://www.widora.io/ting_at (in Chinese, use e.g. Google Chrome to translate)
 */
public class Ting_01M extends AbstractDevice {

    private final String lineEnding = "\r\n"; // helper string for the lineEnding
    private WaitingFor waitingFor;  // enum to deal with different waiting states of the device that are all answered with "AT,OK"
    private String currentMessage = ""; // message that is received from the serial connection
    private String messageToBeSend = ""; // message that is to be send to the network or the device

    /**
     * Constructor that sets the underlying serial connection, but does not register a DeviceListener for this device.
     *
     * @param serialConnection - the underlying serial connection
     */
    public Ting_01M(AbstractSerialConnection serialConnection) {
        super(serialConnection);
    }

    /**
     * Constructor that sets the underlying serial connection and registers a DeviceListener for this device.
     * Use this constructor if you want to register an Activity as your
     * Device Listener
     *
     * @param deviceListener   a DeviceListener object
     * @param serialConnection the underlying serial connection
     */
    public Ting_01M(DeviceListener deviceListener, AbstractSerialConnection serialConnection) {
        super(deviceListener, serialConnection);
    }

    /**
     * Sends a message to the network using the AT command AT+SEND
     *
     * @param message the message to be send to the network
     */
    @Override
    public void send(String message) {

        if (waitingFor != null) {
            deviceListener.onError(new Exception("Error: The device is still busy."));
        } else {
            deviceListener.onDeviceMessageReceived("Sending to network: " + message);
            String commandForSending = "AT+SEND=" + message.length() + lineEnding;
            serialConnection.write(commandForSending.getBytes());
            waitingFor = WaitingFor.SENDING_CONFIRMATION;
            messageToBeSend = message;
        }
    }

    /**
     * Calls connect() method of the underlying serial connection
     */
    @Override
    public void connect() {
        serialConnection.connect();
    }

    /**
     * Method to handle incoming complete messages.
     *
     * @param message the message
     */
    private void onMessageReceived(String message) {
        //cut the line ending
        message = message.substring(0, message.length() - 2);

        if (message.startsWith("AT,")) {
            deviceListener.onDeviceMessageReceived(message);
        }

        if (message.contains("AT,OK")) {
            // Determining what this AT,OK is meant for
            if (waitingFor != null) {
                switch (waitingFor) {
                    case INITIAL_RESET:
                        notifyDeviceListener("Initial reset started.");
                        break;
                    case INITIAL_CONFIGURE:
                        notifyDeviceListener("Initial configuration done.");
                        waitingFor = null;
                        break;
                    case RESET:
                        notifyDeviceListener("Reset started.");
                        waitingFor = null;
                        break;
                    case SENDING_CONFIRMATION:
                        notifyDeviceListener("Writing message to serial connection");
                        serialConnection.write(messageToBeSend.getBytes());
                        messageToBeSend = null;
                        waitingFor = null;
                        break;
                    case CONFIGURE:
                        notifyDeviceListener("Configuration done.");
                        waitingFor = null;
                        break;
                }

            }
        }

        // error messages from device
        if (message.contains("AT,ERR")) {
            deviceListener.onError(new Exception(message));
        }

        if (message.contains("AT,SENDED")) {
            deviceListener.onDeviceMessageReceived("Message successfully sent");
        }

        // incoming message from network start with LR
        if (message.startsWith("LR,")) {
            String incomingStringMessage = message.substring(11);
            deviceListener.onNetworkMessageReceived(incomingStringMessage);
        }

        // other messages should still be given to the deviceListener
        if (!message.startsWith("LR") && !message.startsWith("AT")) {
            deviceListener.onDeviceMessageReceived(message);
            if (message.startsWith("MODULE")) {
                deviceListener.onDeviceMessageReceived("Device was reset.");
            }
            if (waitingFor == WaitingFor.INITIAL_RESET) {
                notifyDeviceListener("Initial reset done. Starting default configuration.");
                waitingFor = WaitingFor.INITIAL_CONFIGURE;
                configure();
            }
        }

    }

    /**
     * Allows sending AT commands to the Ting-01M.
     *
     * @param command the command that should be sent to the device
     */
    @Override
    public void sendDeviceCommand(String command) {
        deviceListener.onDeviceMessageReceived("Sending: " + command);
        serialConnection.write((command + lineEnding).getBytes());
    }

    /**
     * Configures the device with a standard configuration, using the command "AT+CFG=433000000,20,6,10,1,1,0,0,0,0,3000,8,4"
     */
    public void configure() {
        waitingFor = WaitingFor.CONFIGURE;
        sendDeviceCommand("AT+CFG=433000000,20,6,10,1,1,0,0,0,0,3000,8,4");
    }

    /**
     * Additional method to configure the device via a String. uses "AT+CFG=" command
     *
     * @param configuration - String formatted for configuration of the Ting-01M(e.g. "433000000,20,6,10,1,1,0,0,0,0,3000,8,4")
     */
    public void configure(String configuration) {
        waitingFor = WaitingFor.CONFIGURE;
        sendDeviceCommand("AT+CFG=" + configuration);
    }

    /**
     * Method to send a reset to the device. uses AT+RST command
     */
    public void reset() {
        waitingFor = WaitingFor.RESET;
        sendDeviceCommand("AT+RST");
    }

    /**
     * Resets Ting-01M module after the serial connection has been established and notifies
     * the deviceListener through its onConnect method
     */
    @Override
    public void onConnect() {
        deviceListener.onConnect();
        waitingFor = WaitingFor.INITIAL_RESET;
        sendDeviceCommand("AT+RST");
    }

    /**
     * Calls the underlying serial connection's disconnect()
     */
    @Override
    public void disconnect() {
        serialConnection.disconnect();
        waitingFor = null;
        deviceListener.onDeviceMessageReceived("Disconnected.");
    }

    /**
     * Method to help connect data chunks read from the Serial connection. Calls onMessageReceived() with complete messages.
     * Makes sure the serial data is read until encountering "\r\n", which is the end of individual Ting01_M messages
     *
     * @param incomingData the data chunks received by the serial connection.
     */
    //
    @Override
    public void onSerialDataReceived(byte[] incomingData) {

        String incomingMessage = new String(incomingData);
        currentMessage += incomingMessage;
        if (incomingMessage.endsWith("\r\n")) {
            onMessageReceived(currentMessage);
            currentMessage = "";
        }
    }

    /**
     * forwards error to the deviceListener
     *
     * @param e error
     */
    @Override
    public void onError(Exception e) {
        deviceListener.onError(e);
    }

    private void notifyDeviceListener(String message) {
        if (deviceListener != null) {
            deviceListener.onDeviceMessageReceived(message);
        }
    }

    // Enum to discern the different states when waiting for AT,OK
    private enum WaitingFor {
        INITIAL_RESET, INITIAL_CONFIGURE, RESET, SENDING_CONFIRMATION, CONFIGURE
    }
}
