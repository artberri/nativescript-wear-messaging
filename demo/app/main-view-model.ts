import { Observable } from 'tns-core-modules/data/observable';
import { WearMessaging } from 'nativescript-wear-messaging';
import * as app from "tns-core-modules/application";

export class HelloWorldModel extends Observable {
  public message: string;
  private wearMessaging: WearMessaging;

  constructor() {
    super();

    let messagingClient: WearMessaging = new WearMessaging();
    messagingClient.registerListener((messagePath: string, messageReceived: string) => {
      console.log(messagePath);
      console.log(messageReceived);
    });
    messagingClient.startListener();
    messagingClient.send("/demo", "Hola");
    messagingClient.send("/demo", "Hola", "cap-hola");
    this.message = "Hola";

    console.log(this.message);
  }
}
