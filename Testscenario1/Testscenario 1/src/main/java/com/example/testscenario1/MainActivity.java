package com.example.testscenario1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.ad_hoc_network_communication.connection.BluetoothSerialConnection;
import com.example.ad_hoc_network_communication.connection.SerialInputListener;


public class MainActivity extends AppCompatActivity implements SerialInputListener {

    private Boolean isConnected = false;
    private BluetoothSerialConnection connection;
    String currentMessage="";
    TextView consoleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button commandButton = findViewById(R.id.sendCommandButton);
        Switch connectSwitch = findViewById(R.id.switch_connection);
        consoleText = findViewById(R.id.console);
        connection = new BluetoothSerialConnection(this,"98:D3:31:FB:3B:40");

        connectSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                connection.disconnect();
                consoleText.setText("Disconnected.");
                isConnected = false;
            } else {
                connection.connect();
            }
        });


        commandButton.setOnClickListener(view -> {
            TextView messageString = findViewById(R.id.messageInputField);

            if (!isConnected) {
                consoleText.append("Please connect first.\r\n");
            }else{
                String message = messageString.getText().toString()+"\r\n";
                connection.write(message.getBytes());
            }
        });
    }


    @Override
    public void onConnect() {
        isConnected = true;
        runOnUiThread(() -> consoleText.setText("Successfully connected. \r\n"));
    }

    @Override
    public void onSerialDataReceived(byte[] bytes) {
        String incomingMessage = new String(bytes);
        currentMessage += incomingMessage;
        System.out.println(currentMessage);
        if (incomingMessage.endsWith("\r\n")) {
            System.out.println("rn");
            String lastMessage = currentMessage;
            runOnUiThread(() -> consoleText.append("[Serial]: " + lastMessage + "\r\n"));
            currentMessage = "";
        }

    }

    @Override
    public void onError(Exception e) {
        runOnUiThread(() -> consoleText.append("[ERROR]: " + e.toString() + "\r\n"));
    }
}
