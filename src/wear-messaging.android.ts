import * as app from "tns-core-modules/application";

export class WearMessaging {
    public static send(messagePath: string, messageToSend: string, capability: string = null) {
        let sender = new com.berriart.android.nativescriptwearmessaging.Sender(app.android.context);

        if (capability) {
            sender.send(messagePath, messageToSend, capability);
        } else {
            sender.send(messagePath, messageToSend);
        }
    }

    public static registerListener(receiveCallback: (messagePath: string, messageReceived: string) => void) {
        let messageListener = new com.berriart.android.nativescriptwearmessaging.MessageListener({
            receive: function (messagePath: string, messageReceived: string) {
                receiveCallback(messagePath, messageReceived);
            }
        });
        let receiver = new com.berriart.android.nativescriptwearmessaging.Receiver();
        receiver.registerListener(messageListener);
    }
}
