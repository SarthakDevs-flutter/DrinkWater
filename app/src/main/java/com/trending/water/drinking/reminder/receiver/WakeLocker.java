package com.trending.water.drinking.reminder.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

public abstract class WakeLocker {
    private static PowerManager.WakeLock wakeLock;

    @SuppressLint({"InvalidWakeLockTag"})
    public static void acquire(Context ctx) {
        if (wakeLock != null) {
            wakeLock.release();
        }
        wakeLock = ((PowerManager) ctx.getSystemService(Context.POWER_SERVICE)).newWakeLock(805306394, "");
        wakeLock.acquire();
    }

    public static void release() {
        if (wakeLock != null) {
            wakeLock.release();
        }
        wakeLock = null;
    }
}
