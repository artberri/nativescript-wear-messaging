package com.berriart.android.nativescriptwearmessaging;

public interface MessageListener {
    void receive(String messagePath, String messageReceived);
}
