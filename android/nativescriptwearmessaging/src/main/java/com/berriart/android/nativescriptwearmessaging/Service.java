package com.berriart.android.nativescriptwearmessaging;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Service extends WearableListenerService {
    public static final String TAG = "WearableListenerService";

    private static MessageListener onMessageReceivedCallback = null;
    private static Map<String, OnCapabilityChangedListener> capabilityChangedListeners = new HashMap<String, OnCapabilityChangedListener>();

    @Override
    public void onMessageReceived (MessageEvent messageEvent) {
        if (Log.isLoggable(TAG, Log.INFO)) {
            Log.i(TAG, "onMessageReceived: " + messageEvent);
        }
        receive(messageEvent.getPath(), new String(messageEvent.getData()));
    }

    public static void registerListener(MessageListener listener) {
        if (Log.isLoggable(TAG, Log.INFO)) {
            Log.i(TAG, "registerListener");
        }
        onMessageReceivedCallback = listener;
    }

    public static void send(final Context context, final String messagePath, final String messageToSend) {
        if (Log.isLoggable(TAG, Log.INFO)) {
            Log.i(TAG, "send: " + messagePath);
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (Log.isLoggable(TAG, Log.INFO)) {
                    Log.i(TAG, "run: ");
                }

                String nodeId = getNodeId(context);

                if (Log.isLoggable(TAG, Log.INFO)) {
                    Log.i(TAG, "Node: " + nodeId);
                }

                if (nodeId != null) {
                    sendToNode(context, messagePath, messageToSend, nodeId);
                    return;
                }

                if (Log.isLoggable(TAG, Log.INFO)) {
                    Log.i(TAG, "No nodes to send. Path: " + messagePath);
                }
            }
        });

        thread.start();
    }

    public static void send(final Context context, final String messagePath, final String messageToSend, final String capability) {
        if (Log.isLoggable(TAG, Log.INFO)) {
            Log.i(TAG, "ssssssssssend3: ");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (setupCapability(context, capability)) {
                    sendToNode(context, messagePath, messageToSend, capabilityChangedListeners.get(capability).transcriptionNodeId);
                    return;
                }

                if (Log.isLoggable(TAG, Log.INFO)) {
                    Log.i(TAG, "No nodes to send. Path: " + messagePath);
                }
            }
        });
    }

    private static void sendToNode(final Context context, final String messagePath, final String messageToSend, String nodeId) {
        Task<Integer> sendTask =
                Wearable.getMessageClient(context).sendMessage(
                        nodeId, messagePath, messageToSend.getBytes());

        sendTask.addOnSuccessListener(new SendOnSuccessListener());
        sendTask.addOnFailureListener(new SendOnFailureListener());

        if (Log.isLoggable(TAG, Log.INFO)) {
            Log.i(TAG, "No nodes to send. Path: " + messagePath);
        }
    }

    private void receive(String messagePath, String messageReceived) {
        if (onMessageReceivedCallback != null) {
            onMessageReceivedCallback.receive(messagePath, messageReceived);

            if (Log.isLoggable(TAG, Log.INFO)) {
                Log.i(TAG, "Successful callback run. Path: " + messagePath);
            }

            return;
        }
        if (Log.isLoggable(TAG, Log.INFO)) {
            Log.i(TAG, "Message received without callback being set. Path: " + messagePath);
        }
    }

    private static boolean setupCapability(final Context context, final String capability) {
        if (!capabilityChangedListeners.containsKey(capability)) {
            capabilityChangedListeners.put(capability, new OnCapabilityChangedListener());
        }

        OnCapabilityChangedListener onCapabilityChangedListener = capabilityChangedListeners.get(capability);

        if (onCapabilityChangedListener.transcriptionNodeId == null) {
            CapabilityInfo capabilityInfo;
            try {
                capabilityInfo = Tasks.await(
                        Wearable.getCapabilityClient(context).getCapability(
                                capability, CapabilityClient.FILTER_REACHABLE));
            } catch (ExecutionException e) {
                if (Log.isLoggable(TAG, Log.ERROR)) {
                    Log.e(TAG, "ExecutionException checking capability: " + e.getMessage());
                }
                return false;
            } catch (InterruptedException e) {
                if (Log.isLoggable(TAG, Log.ERROR)) {
                    Log.e(TAG, "InterruptedException checking capability: " + e.getMessage());
                }
                return false;
            }

            onCapabilityChangedListener.updateTranscriptionCapability(capabilityInfo);

            Wearable.getCapabilityClient(context).addListener(
                    onCapabilityChangedListener, capability);
        }

        return true;
    }

    private static String getNodeId(final Context context) {
        String nodeId = null;

        try {
            List<Node> nodes = Tasks.await(Wearable.getNodeClient(context).getConnectedNodes());
            nodeId = pickBestNodeId(nodes);
        } catch (ExecutionException e) {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "ExecutionException checking capability: " + e.getMessage());
            }
        } catch (InterruptedException e) {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "InterruptedException checking capability: " + e.getMessage());
            }
        }

        return nodeId;
    }

    private static String pickBestNodeId(List<Node> nodes) {
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
