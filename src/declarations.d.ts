declare namespace com {
    export namespace berriart {
        export namespace android {
            export namespace nativescriptwearmessaging {
                export class Sender {
                    public constructor(context: any);
                    public send(messagePath: string, messageToSend: string, capability: string): void;
                    public send(messagePath: string, messageToSend: string): void;
                }
            }
        }
    }
}

declare namespace com {
    export namespace berriart {
        export namespace android {
            export namespace nativescriptwearmessaging {
                export class MessageListener {
                    public constructor(implementation: {
                        receive(messagePath: string, messageToSend: string): void;
                    });
                    public receive(messagePath: string, messageToSend: string): void;
                }
            }
        }
    }
}

declare namespace com {
    export namespace berriart {
        export namespace android {
            export namespace nativescriptwearmessaging {
                export class Receiver {
                    public constructor(context: any);
                    public registerListener(listener: MessageListener): void;
                    public startListener(): void;
                    public stopListener(): void;
                }
            }
        }
    }
}

declare namespace com {
    export namespace berriart {
        export namespace android {
            export namespace nativescriptwearmessaging {
                export class MessageListenerService {
                    public onCreate(): void;
                    public onMessageReceived(messageEvent: any): void;
                    public constructor();
                }
            }
        }
    }
}
