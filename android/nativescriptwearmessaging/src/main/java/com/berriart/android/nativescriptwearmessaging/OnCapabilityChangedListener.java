package com.berriart.android.nativescriptwearmessaging;

import android.support.annotation.NonNull;

import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;

import java.util.Set;

class OnCapabilityChangedListener implements CapabilityClient.OnCapabilityChangedListener {
    Set<Node> connectedNodes = null;

    @Override
    public void onCapabilityChanged(@NonNull CapabilityInfo capabilityInfo) {
        updateTranscriptionCapability(capabilityInfo);
    }

    void updateTranscriptionCapability(CapabilityInfo capabilityInfo) {
        connectedNodes = capabilityInfo.getNodes();
    }
}
