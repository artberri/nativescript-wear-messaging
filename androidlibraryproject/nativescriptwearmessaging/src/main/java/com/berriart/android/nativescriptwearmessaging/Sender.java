package com.berriart.android.nativescriptwearmessaging;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Sender {
    private static final String TAG = "Sender";

    private Context _context = null;
    private Map<String, OnCapabilityChangedListener> capabilityChangedListeners = new HashMap<String, OnCapabilityChangedListener>();
    private Queue<Message> messageQueue = new LinkedList<>();

    public Sender(Context context) {
        _context = context;
    }

    public void send(final String messagePath, final String messageToSend) {
        final Message message = new Message(messagePath, messageToSend);
        Logger.info(TAG, String.format(_context.getString(R.string.npaw_action_send), message.path));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.debug(TAG, _context.getString(R.string.npaw_thread_started));

                deliverMessage(message);
            }
        });

        thread.start();
    }

    public void send(final String messagePath, final String messageToSend, final String capability) {
        final Message message = new Message(messagePath, messageToSend);
        Logger.info(TAG, String.format(_context.getString(R.string.npaw_action_send_capability), messagePath, capability));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.debug(TAG, _context.getString(R.string.npaw_thread_started));

                deliverMessageWithCapability(message, capability);
            }
        });

        thread.start();
    }

    private void deliverMessageWithCapability(Message message, final String capability) {
        boolean isFirstTime = false;
        if (!capabilityChangedListeners.containsKey(capability)) {
            isFirstTime = true;
            capabilityChangedListeners.put(capability, new OnCapabilityChangedListener());
        }

        final OnCapabilityChangedListener onCapabilityChangedListener = capabilityChangedListeners.get(capability);

        if (onCapabilityChangedListener.connectedNodes == null) {
            messageQueue.add(message);

            if (isFirstTime) {
                Task<CapabilityInfo> getCapabilityTask = Wearable.getCapabilityClient(_context).getCapability(
                        capability, CapabilityClient.FILTER_REACHABLE);

                getCapabilityTask.addOnSuccessListener(new OnSuccessListener<CapabilityInfo>() {
                    @Override
                    public void onSuccess(CapabilityInfo capabilityInfo) {
                        onCapabilityChangedListener.updateTranscriptionCapability(capabilityInfo);
                        Wearable.getCapabilityClient(_context).addListener(
                                onCapabilityChangedListener, capability);

                        Logger.debug(TAG, _context.getString(R.string.npaw_capability_listener));

                        String nodeId = pickBestNodeId(onCapabilityChangedListener.connectedNodes);

                        while (messageQueue.size() > 0) {
                            Message messageInQueue = messageQueue.remove();
                            sendToNode(messageInQueue, nodeId);
                        }
                    }
                });

                getCapabilityTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Logger.error(TAG, String.format(_context.getString(R.string.npaw_capability_exception), e.getMessage()));
                    }
                });
            }

            return;
        }

        sendToNode(message, pickBestNodeId(onCapabilityChangedListener.connectedNodes));
    }

    private void sendToNode(final Message message, String nodeId) {
        if (nodeId != null) {
            Logger.info(TAG, String.format(_context.getString(R.string.npaw_action_send_node), nodeId));

            Task<Integer> sendTask =
                    Wearable.getMessageClient(_context).sendMessage(
                            nodeId, message.path, message.content.getBytes());

            sendTask.addOnSuccessListener(new OnSuccessListener<Integer>() {
                @Override
                public void onSuccess(Integer resultCode) {
                    if (resultCode > 0) {
                        Logger.debug(TAG, String.format(_context.getString(R.string.npaw_message_sent), message.path, resultCode));
                        return;
                    }

                    Logger.error(TAG, String.format(_context.getString(R.string.npaw_message_sent_wrong), resultCode));
                }
            });
            sendTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Logger.error(TAG, String.format(_context.getString(R.string.npaw_sending_message_exception), e.getMessage()));
                }
            });

            return;
        }

        Logger.warn(TAG, _context.getString(R.string.npaw_no_nodes));
    }

    private void deliverMessage(final Message message) {
        String nodeId = null;

        Task<List<Node>> getNodesTask = Wearable.getNodeClient(_context).getConnectedNodes();

        getNodesTask.addOnSuccessListener(new OnSuccessListener<List<Node>>(){
            @Override
            public void onSuccess(List<Node> nodes) {
                final String nodeId = pickBestNodeId(nodes);

                if (nodeId != null) {
                    sendToNode(message, nodeId);
                    return;
                }

                Logger.warn(TAG, _context.getString(R.string.npaw_no_nodes));
            }
        });

        getNodesTask.addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e) {
                Logger.error(TAG, String.format(_context.getString(R.string.npaw_getnodes_exception), e.getMessage()));
            }
        });
    }

    private String pickBestNodeId(Collection<Node> nodes) {
        String bestNodeId = null;
        // Find a nearby node or pick one arbitrarily
        for (Node node : nodes) {
            Logger.debug(TAG, String.format(_context.getString(R.string.npaw_found_node), node.getDisplayName()));
            if (node.isNearby()) {
                Logger.debug(TAG, String.format(_context.getString(R.string.npaw_selected_node), node.getDisplayName()));
                return node.getId();
            }
            bestNodeId = node.getId();
        }

        return bestNodeId;
    }
}
