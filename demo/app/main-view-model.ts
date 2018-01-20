import { Observable } from 'tns-core-modules/data/observable';
import { WearMessaging } from 'nativescript-wear-messaging';
import * as app from "tns-core-modules/application";

export class HelloWorldModel extends Observable {
  public message: string;
  private wearMessaging: WearMessaging;

  constructor() {
    super();

    WearMessaging.send("/demo", "Hola");
    WearMessaging.registerListener((messagePath: string, messageReceived: string) => {
      console.log(messagePath);
      console.log(messageReceived);
    });
    this.message = "Hola";

    console.log(this.message);
  }
}
