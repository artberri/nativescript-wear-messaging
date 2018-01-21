package com.berriart.android.nativescriptwearmessaging;

import android.content.Context;
import android.content.Intent;

public class Receiver
{
    private static final String TAG = "Receiver";
    private Context _context = null;
    private Intent _serviceIntent;

    public Receiver(Context context) {
        _context = context;
    }

    public void startListener() {
        _serviceIntent = new Intent(
                _context, MessageListenerService.class
        );
        _context.startService(_serviceIntent);
        Logger.info(TAG, _context.getString(R.string.npaw_action_start_listener));
    }

    public void stopListener() {
        _context.stopService(_serviceIntent);
        Logger.info(TAG, _context.getString(R.string.npaw_action_stop_listener));
    }

    public void registerListener(MessageListener listener) {
        Logger.info(TAG, _context.getString(R.string.npaw_action_register_listener));
        MessageListenerService.registerListener(listener);
    }
}
