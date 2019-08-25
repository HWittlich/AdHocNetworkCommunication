# AdHocNetworkCommunication
Library to facilitate the development of Ad hoc networking apps in Android

This project was created as part of my Bachelor's thesis at the HTW Berlin in 2019.

It provides a basic library for developing ad-hoc networking apps. 
It's aim is to allow developers to easily integrate the ability to communicate with network hardware using different methods of connections (Bluetooth, USB, ...) and different networking devices.

Currently, it supports only connecting via Bluetooth to a network device Ting-01M, which has to be connected via a UART Adapter to a Bluetooth module.

The thesis will be included as additional documentation (in German) in this repository once it has been reviewed by the University.


## Examples
The library has been tested in three scenarios, each using a different of the three components to build upon:
- TestScenario1 contains an App that uses the BluetoothSerialConnection to create a serial connection to a bluetooth device. 
- TestScenario2 contains an App that uses the Ting_01M class to use functionalities of a Ting-01M networking device to send and receive messages to a network. It uses the BluetoothSerialConnection class to create a connection.
- Testscenario3 contains the most comprehensive example, using the library's ExampleProtocolLayer class to send and receive messages that are contained in this protocol. It uses the Ting_01M class to control a networking device accordingly, as well as the BluetoothSerialConnection class for connecting to the Ting_01M module.


## Step-by-Step: Using the library
1. To get started, import the library into your project as documented [here](https://developer.android.com/studio/projects/android-library#AddDependency). You can use the AdHocNetworkCommunication.aar file for this.
2. Have a look at the examples in the Testscenario directories and the corresponding classes from the library to see how you can interact with the different components of the library.
3. In your App, let an Activity class implement one of the Listener Interfaces (SerialInputListenr, DeviceListener, your own ProtocolListener). 
4. You can use the constructors to create a new networking connection:
```
public class MainActivity extends AppCompatActivity implements ExampleProtocolListener {
private ExampleProtocolLayer protocolNode;
...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    ...
    protocolNode = new ExampleProtocolLayer(this, new Ting_01M(new BluetoothSerialConnection("98:D3:31:FB:3B:40")));
    }
    
// using the connect() method invokes the connect() method from Ting_01M, which then invokes the connect() method of BletoothSerialConnection.
protocolNode.connect();

}

```
6. As the BluetoothSerialConnection starts a new thread to listen to incoming messages, any UI changes that occur in the methods of the Listener Interfaces (SerialInputListener, DeviceListener,...) need to be embedded in [runOnUiThread()](https://developer.android.com/reference/android/app/Activity.html#runOnUiThread(java.lang.Runnable)) methods. See the examples on how it is used.

## Extending the library
The library uses abstract classes and interfaces to offer easy extensibility. to add a new kind of serial connection, simply extend the AbstractSerialConnection class. To add a new networking device, extend the AbstractDevice class. Your classes should make use of the Listener Interface methods to communicate to their respective Listener.

## Creating your own protocol
If you want to implement your own protocol, the ExampleProtocolLayer.java and the ExampleProtocolListener.java files give you an idea on how to achieve this. Testscenario3 shows you how a protocol is then implemented in an app.
You can easily extend the AbstractProtocolLayer class and write a ProtocolListener Interface that fit your protocol's needs. Then simply build upon the existing BluetoothSerialConnection class

## Additional Information
The BluetoothSerialConnection class uses the well-known UUID 00001101-0000-1000-8000-00805F9B34FB to create a connection. 
