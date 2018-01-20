package com.berriart.android.nativescriptwearmessaging;

import android.content.Context;
import android.util.Log;

public class Sender {
    public static final String TAG = "Sender";

    private Context _context = null;

    public Sender(Context context) {
        _context = context;
    }

    public void send(final String messagePath, final String messageToSend) {
        if (Log.isLoggable(TAG, Log.INFO)) {
            Log.i(TAG, "Send call: " + messagePath + " " + messageToSend);
        }
        Service.send(_context, messagePath, messageToSend);
    }

    public void send(final String messagePath, final String messageToSend, final String capability) {
        if (Log.isLoggable(TAG, Log.INFO)) {
            Log.i(TAG, "Send call with capability");
        }
        Service.send(_context, messagePath, messageToSend, capability);
    }
}
