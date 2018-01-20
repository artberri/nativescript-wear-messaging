var WearMessaging = require("nativescript-wear-messaging").WearMessaging;
var wearMessaging = new WearMessaging();

describe("greet function", function() {
    it("exists", function() {
        expect(wearMessaging.greet).toBeDefined();
    });

    it("returns a string", function() {
        expect(wearMessaging.greet()).toEqual("Hello, NS");
    });
});