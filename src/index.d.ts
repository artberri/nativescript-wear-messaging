export declare class WearMessaging {
  static send(messagePath: string, messageToSend: string, capability?: string): void;
  static registerListener(receiveCallback: (messagePath: string, messageReceived: string) => void): void;
}
