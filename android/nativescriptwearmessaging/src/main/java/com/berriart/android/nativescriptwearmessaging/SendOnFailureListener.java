package com.berriart.android.nativescriptwearmessaging;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;

class SendOnFailureListener implements OnFailureListener {
    @Override
    public void onFailure(@NonNull Exception e) {
        if (Log.isLoggable(Service.TAG, Log.ERROR)) {
            Log.e(Service.TAG, "Error sending message. Path: " + e.getMessage());
        }
    }
}
