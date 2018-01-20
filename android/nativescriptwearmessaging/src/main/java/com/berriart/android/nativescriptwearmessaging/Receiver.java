package com.berriart.android.nativescriptwearmessaging;

import android.util.Log;

public class Receiver
{
    private static final String TAG = "Receiver";

    public void registerListener(MessageListener listener) {
        if (Log.isLoggable(TAG, Log.INFO)) {
            Log.i(TAG, "registerListener");
        }
        Service.registerListener(listener);
    }
}
