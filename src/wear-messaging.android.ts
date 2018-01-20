import * as app from "tns-core-modules/application";

export class WearMessaging {
    private sender: com.berriart.android.nativescriptwearmessaging.Sender;
    private receiver: com.berriart.android.nativescriptwearmessaging.Receiver;

    constructor() {
        this.sender = new com.berriart.android.nativescriptwearmessaging.Sender(app.android.context);
        this.receiver = new com.berriart.android.nativescriptwearmessaging.Receiver(app.android.context);
    }

    public send(messagePath: string, messageToSend: string, capability: string = null) {
        if (capability) {
            this.sender.send(messagePath, messageToSend, capability);
        } else {
            this.sender.send(messagePath, messageToSend);
        }
    }

    public registerListener(receiveCallback: (messagePath: string, messageReceived: string) => void) {
        let messageListener = new com.berriart.android.nativescriptwearmessaging.MessageListener({
            receive: function (messagePath: string, messageReceived: string) {
                receiveCallback(messagePath, messageReceived);
            }
        });

        this.receiver.registerListener(messageListener);
    }
}
