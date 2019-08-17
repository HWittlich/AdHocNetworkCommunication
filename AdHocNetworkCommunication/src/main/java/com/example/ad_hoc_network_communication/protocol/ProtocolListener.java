package com.example.ad_hoc_network_communication.protocol;

/**
 * Base interface for more specific ProtocolListener Interfaces. Implement your own interface that fits your protocol
 */
public interface ProtocolListener {

    void onConnect();

    void onError(Exception e);
}


