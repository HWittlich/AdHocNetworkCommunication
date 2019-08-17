package com.example.testscenario2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.ad_hoc_network_communication.connection.BluetoothSerialConnection;
import com.example.ad_hoc_network_communication.device.DeviceListener;
import com.example.ad_hoc_network_communication.device.Ting_01M.Ting_01M;


/**
 * Activity Class for Test scenario 2. Tests the device component and its classes DeviceListener, AbstractDevice/Ting_01M
 */
public class MainActivity extends AppCompatActivity implements DeviceListener {

    private Boolean isConnected = false;
    private Ting_01M device;
    private TextView consoleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Initialise variables */

        Button sendMessageButton = findViewById(R.id.button_send);
        Button sendCommandButton = findViewById(R.id.sendCommandButton);
        Switch connectSwitch = findViewById(R.id.switch_connection);
        consoleText = findViewById(R.id.console);
        device = new Ting_01M(this, new BluetoothSerialConnection("98:D3:31:FB:3B:40"));

        /* set UI Listeners */

        connectSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                device.disconnect();
                isConnected = false;
            } else {
                device.connect();
            }
        });

        sendMessageButton.setOnClickListener(view -> {
            TextView consoleText = findViewById(R.id.console);
            TextView messageString = findViewById(R.id.messageInputField);

            if (!isConnected) {
                consoleText.append("Please connect first.\r\n");
            }else{
                String message = messageString.getText().toString();
                device.send(message);
            }
        });

        sendCommandButton.setOnClickListener(view -> {
            TextView consoleText = findViewById(R.id.console);
            TextView messageString = findViewById(R.id.messageInputField);

            if (!isConnected) {
                consoleText.append("Please connect first.\r\n");
            }else{
                String message = messageString.getText().toString();
                device.sendDeviceCommand(message);
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
    public void onNetworkMessageReceived(String message) {
        runOnUiThread(() -> consoleText.append("[Network]: " + message + "\r\n"));
    }

    @Override
    public void onError(Exception e) {
        runOnUiThread(() -> consoleText.append("[ERROR]: " + e.toString() + "\r\n"));
    }
}
