package com.example.blackjack.utils;

import android.support.compat.BuildConfig;
import android.util.Log;

/**
 * LogManager class. if BuildConfig.DEBUG is false, do not show log.
 *
 * Created by hiroki.kakuno on 2017/07/14.
 */

public class Logger {

    private static boolean DEBUG = true;

    public static void d(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void w(String tag, String message) {
        Log.w(tag, message);
    }

    public static void w(String tag, String message, Throwable th) {
        Log.w(tag, message, th);
    }

    public static void e(String tag, String message) {
        Log.e(tag, message);
    }

    public static void e(String tag, String message, Throwable th) {
        Log.e(tag, message, th);
    }
}