package com.berriart.android.nativescriptwearmessaging;

import android.support.annotation.NonNull;

import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;

import java.util.Set;

class OnCapabilityChangedListener implements CapabilityClient.OnCapabilityChangedListener {
    String transcriptionNodeId = null;

    @Override
    public void onCapabilityChanged(@NonNull CapabilityInfo capabilityInfo) {
        updateTranscriptionCapability(capabilityInfo);
    }

    void updateTranscriptionCapability(CapabilityInfo capabilityInfo) {
        Set<Node> connectedNodes = capabilityInfo.getNodes();

        transcriptionNodeId = pickBestNodeId(connectedNodes);
    }

    private String pickBestNodeId(Set<Node> nodes) {
        String bestNodeId = null;
        // Find a nearby node or pick one arbitrarily
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }
}
