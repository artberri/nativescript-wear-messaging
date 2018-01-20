package com.berriart.android.nativescriptwearmessaging;

import android.util.Log;

class Logger {
    static void debug(String tag, String message) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }

    static void info(String tag, String message) {
        if (Log.isLoggable(tag, Log.INFO)) {
            Log.i(tag, message);
        }
    }

    static void warn(String tag, String message) {
        if (Log.isLoggable(tag, Log.WARN)) {
            Log.w(tag, message);
        }
    }

    static void error(String tag, String message) {
        if (Log.isLoggable(tag, Log.ERROR)) {
            Log.e(tag, message);
        }
    }
}
