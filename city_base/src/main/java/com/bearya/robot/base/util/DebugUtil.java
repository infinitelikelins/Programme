package com.bearya.robot.base.util;

import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

public class DebugUtil {

    private static boolean debugMode = true;

    public static void debug(String format, Object... args) {
        if (!debugMode || format == null) return;
        try {
            Log.d("Robot-debug", String.format(format, args));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void error(String format, Object... args) {
        if (!debugMode || format == null) return;
        try {
            Log.e("Robot-error", String.format(format, args));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void info(String format, Object... args) {
        if (!debugMode || format == null) return;
        try {
            Log.i("Robot-info", String.format(format, args));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void warn(String format, Object... args) {
        if (!debugMode || format == null) return;
        try {
            Log.w("Robot-warn", String.format(format, args));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void report(String format, Object... args){
        if (format == null) return;
        try {
            CrashReport.postCatchedException(new Exception(String.format(format, args)));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void setDebugMode(boolean debugMode) {
        DebugUtil.debugMode = debugMode;
    }

}