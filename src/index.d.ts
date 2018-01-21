export declare class WearMessaging {
    constructor();
    send(messagePath: string, messageToSend: string, capability?: string): void;
    startListener(): void;
    stopListener(): void;
    registerListener(receiveCallback: (messagePath: string, messageReceived: string) => void): void;
}
