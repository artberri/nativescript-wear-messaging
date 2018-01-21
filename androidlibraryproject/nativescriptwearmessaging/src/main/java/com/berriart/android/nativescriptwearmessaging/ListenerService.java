package com.berriart.android.nativescriptwearmessaging;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class ListenerService extends WearableListenerService {
    private static final String TAG = "ListenerService";

    private static MessageListener onMessageReceivedCallback = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.debug(TAG, getString(R.string.npaw_listener_created));
    }

    @Override
    public void onMessageReceived (MessageEvent messageEvent) {
        Message message = new Message(messageEvent.getPath(), new String(messageEvent.getData()));
        Logger.debug(TAG, String.format(getString(R.string.npaw_message_received), message.path));
        receive(message);
    }

    static void registerListener(MessageListener listener) {
        onMessageReceivedCallback = listener;
    }

    private void receive(Message message) {
        if (onMessageReceivedCallback == null) {
            Logger.error(TAG, String.format(getString(R.string.npaw_message_callback_ko), message.path));
            return;
        }

        onMessageReceivedCallback.receive(message.path, message.content);
        Logger.info(TAG, String.format(getString(R.string.npaw_message_callback_ok), message.path));
    }
}
