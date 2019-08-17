package com.example.testscenario3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.ad_hoc_network_communication.protocol.ExampleProtocol.ExampleProtocolListener;
import com.example.ad_hoc_network_communication.connection.BluetoothSerialConnection;
import com.example.ad_hoc_network_communication.device.Ting_01M.Ting_01M;
import com.example.ad_hoc_network_communication.protocol.ExampleProtocol.ExampleProtocolLayer;

/**
 * Activity Class for Test scenario 3. Tests the protocol component and the example classes ExampleProtocolListener, AbstractProtocolLayer/ExampleProtocolLayer
 */
public class MainActivity extends AppCompatActivity implements ExampleProtocolListener {

    private Boolean isConnected = false;
    private ExampleProtocolLayer protocolNode;
    private TextView consoleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Initialise variables */

        Button sendBroadcastButton = findViewById(R.id.buttonSendBroadcast);
        Button sendResetButton = findViewById(R.id.buttonSendReset);
        Button sendDiscoveryButton = findViewById(R.id.buttonSendDiscovery);
        Button sendCommandButton = findViewById(R.id.buttonSendCommand);
        Switch connectSwitch = findViewById(R.id.switch_connection);
        consoleText = findViewById(R.id.console);
        protocolNode = new ExampleProtocolLayer(this, new Ting_01M(new BluetoothSerialConnection("98:D3:31:FB:3B:40")));

        /* set UI Listeners */

        connectSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                protocolNode.disconnect();
                isConnected = false;
            } else {
                protocolNode.connect();
            }
        });

        sendBroadcastButton.setOnClickListener(view -> {
            if (!isConnected) {
                consoleText.append("Please connect first.\r\n");
            }else{
                TextView messageString = findViewById(R.id.messageInputField);
                String message = messageString.getText().toString();
                protocolNode.sendBroadcast(message);
            }
        });

        sendCommandButton.setOnClickListener(view -> {
            if (!isConnected) {
                consoleText.append("Please connect first.\r\n");
            }else{
                TextView messageString = findViewById(R.id.messageInputField);
                String message = messageString.getText().toString();
                protocolNode.getDevice().sendDeviceCommand(message);
            }
        });

        sendResetButton.setOnClickListener(view -> {
            if (!isConnected) {
                consoleText.append("Please connect first.\r\n");
            }else{
                protocolNode.sendNetworkReset();
            }
        });

        sendDiscoveryButton.setOnClickListener(view -> {
           if (!isConnected) {
                consoleText.append("Please connect first.\r\n");
            }else{
                protocolNode.sendNeighborDiscovery();
            }
        });
    }

    @Override
    public void onConnect() {
        isConnected = true;
        runOnUiThread(() -> consoleText.setText("Successfully connected. \r\n"));
    }

    @Override
    public void onDeviceMessageReceived(String message) {
        runOnUiThread(() -> consoleText.append("[Device] " + message + "\r\n"));
    }

    @Override
    public void onBroadcastMessageReceived(String message) {
        runOnUiThread(() -> consoleText.append("[Network Broadcast]: " + message + "\r\n"));
    }

    @Override
    public void onDiscoveryMessageReceived(String message) {
        // in this example, there is no need to process the parameter "message"
        runOnUiThread(() -> consoleText.append("[Network]: Received a Neighbor Discovery request." + "\r\n"));
    }

    @Override
    public void onResetMessageReceived(String message) {
        // in this example, there is no need to process the parameter "message"
        runOnUiThread(() -> consoleText.append("[Network]: Received a reset message." + "\r\n"));
    }

    @Override
    public void onError(Exception e) {
        runOnUiThread(() -> consoleText.append("[ERROR]: " + e.toString() + "\r\n"));
    }
}
