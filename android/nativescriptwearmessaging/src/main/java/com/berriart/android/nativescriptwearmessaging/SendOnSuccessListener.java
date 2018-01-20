package com.berriart.android.nativescriptwearmessaging;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;

import static com.google.android.gms.common.api.CommonStatusCodes.SUCCESS;

class SendOnSuccessListener implements OnSuccessListener<Integer> {

    @Override
    public void onSuccess(Integer resultCode) {
        if (resultCode != SUCCESS) {
            if (Log.isLoggable(Service.TAG, Log.ERROR)) {
                Log.e(Service.TAG, "Error sending message. Error code: " + resultCode);
            }
        }
        else {
            if (Log.isLoggable(Service.TAG, Log.INFO)) {
                Log.i(Service.TAG, "Message sended!");
            }
        }
    }
}
