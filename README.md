# AdHocNetworkCommunication
Library to facilitate the development of Ad hoc networking apps in Android

This project was created as part of my Bachelor's thesis at the HTW Berlin in 2019.

It provides a basic library for developing ad-hoc networking apps. 
It's aim is to allow developers to easily integrate the ability to communicate with network hardware using different methods of connections (Bluetooth, USB, ...).



In general, 

##Examples
The library has been tested in three scenarios, each using a different of the three components to build upon.
Testscenario3 contains the most comprehensive

##Step-by-Step:
1. To get started, import the library into your project as documented [here](https://developer.android.com/studio/projects/android-library#AddDependency)
2. Depending on your use case decide which component you want to build upon
3. Have a look at the examples in the Testscenario directories and the corresponding classes from the library

If you want to implement your own protocol, the ExampleProtocolLayer.java and the ExampleProtocolListener.java files give you an idea on how to achieve this.
Testscenario3 shows you how a protocol is then implemented in an app.
