# NativeScript Wear Messaging Plugin

[![Build Status][build-status]][build-url]
[![NPM version][npm-image]][npm-url]
[![Downloads][downloads-image]][npm-url]
[![Twitter Follow][twitter-image]][twitter-url]

[build-status]:https://travis-ci.org/artberri/nativescript-wear-messaging.svg?branch=master
[build-url]:https://travis-ci.org/artberri/nativescript-wear-messaging
[npm-image]:http://img.shields.io/npm/v/nativescript-wear-messaging.svg
[npm-url]:https://npmjs.org/package/nativescript-wear-messaging
[downloads-image]:http://img.shields.io/npm/dm/nativescript-wear-messaging.svg
[twitter-image]:https://img.shields.io/twitter/follow/artberri.svg?style=social&label=Follow%20me
[twitter-url]:https://twitter.com/artberri

Adding support for Wear Messaging using the [MessageClient API](https://developer.android.com/training/wearables/data-layer/messages.html).
This plugin is intended to be used to communicate between a handled app and an Android Wear app.

**Only Android Supported**

## Installation

Install the plugin:

```bash
tns plugin add nativescript-wear-messaging
```

## Usage

The idea of this plugin is to communicate between a wear device and a handled device. Both can act as receiver or sender, in fact, that's the
most common usage way and the one explained here. You need two apps that will communicate to each other, here is described how you can configure this plugin
in both:

### In the Wear app

Add the following to your `AndroidManifest.xml` inside your `<application>` tag. It will create the service listener that will be waiting
for the messages sent by the handled app.

```xml
<service android:name="com.berriart.android.nativescriptwearmessaging.MessageListenerService">
    <intent-filter>
        <action android:name="com.google.android.gms.wearable.MESSAGE_RECEIVED" />
        <data android:scheme="wear" android:host="*" />
    </intent-filter>
</service>
```

Since multiple wearables can be connected to the handheld device, the wearable app needs to determine that a connected node
is capable of launching the activity. In your wearable app, advertise that the node it runs on provides specific capabilities.
We will use this later when sending messages from the handled device.

Create a `wear.xml` file inside `app/App_Resources/Android/values` to advertise the capabilities

```xml
<resources>
    <string-array name="android_wear_capabilities">
        <item>name_of_your_capabilty_wear</item>
    </string-array>
</resources>
```

Sending messages to the handled app:

```typescript
import { WearMessaging } from 'nativescript-wear-messaging';

let client = new WearMessaging();
client.send("/some/path", "some content", "name_of_your_capabilty_handled"); // Last parameter is the capablity name of then handled device
```

Receiving messages to from the handled app:

```typescript
import { WearMessaging } from 'nativescript-wear-messaging';

let client = new WearMessaging();
client.registerListener((path: string, content: string) => {
    if (path === "/some/path") {
        console.log(path + " " + content);
    }
});
client.startListener();
```

*Include the following to your `references.d.ts` file if you are getting this error: `TS2304: Cannot find name 'com'.`

```ts
/// <reference path="./node_modules/nativescript-wear-messaging/declarations.d.ts" /> Needed for wear-messaging
```

### In the handled app

Add the following to your `AndroidManifest.xml` inside your `<application>` tag. It will create the service listener that will be waiting
for the messages sent by the wear app.

```xml
<service android:name="com.berriart.android.nativescriptwearmessaging.MessageListenerService">
    <intent-filter>
        <action android:name="com.google.android.gms.wearable.MESSAGE_RECEIVED" />
        <data android:scheme="wear" android:host="*" />
    </intent-filter>
</service>
```

Since multiple wearables can be connected to the handheld device, the wearable app needs to determine that a connected node
is capable of launching the activity. In your wearable app, advertise that the node it runs on provides specific capabilities.
We will use this later when sending messages from the handled device.

Create a `wear.xml` file inside `app/App_Resources/Android/values` to advertise the capabilities

```xml
<resources>
    <string-array name="android_wear_capabilities">
        <item>name_of_your_capabilty_handled</item>
    </string-array>
</resources>
```

Sending messages to the wear app:

```typescript
import { WearMessaging } from 'nativescript-wear-messaging';

let client = new WearMessaging();
client.send("/some/path", "some content", "name_of_your_capabilty_wear"); // Last parameter is the capablity name of then handled device
```

Receiving messages to from the wear app:

```typescript
import { WearMessaging } from 'nativescript-wear-messaging';

let client = new WearMessaging();
client.registerListener((path: string, content: string) => {
    if (path === "/some/path") {
        console.log(path + " " + content);
    }
});
client.startListener();
```

*You should read the [official Android](https://developer.android.com/training/wearables/data-layer/messages.html) doc anyway.

## License

Apache License Version 2.0, January 2018
