package com.berriart.android.nativescriptwearmessaging;

import android.content.Context;

public class Receiver
{
    private static final String TAG = "Receiver";
    private Context _context = null;

    public Receiver(Context context) {
        _context = context;
    }

    public void registerListener(MessageListener listener) {
        Logger.info(TAG, _context.getString(R.string.npaw_action_register_listener));
        ListenerService.registerListener(listener);
    }
}
