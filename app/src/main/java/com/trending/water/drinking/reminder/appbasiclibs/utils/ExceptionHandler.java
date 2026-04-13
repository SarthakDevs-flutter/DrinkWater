package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Process;

import com.trending.water.drinking.reminder.appbasiclibs.Screen_Error_Report;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final String LINE_SEPARATOR = "\n";
    private final Activity myContext;

    public ExceptionHandler(Activity context) {
        this.myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        Intent intent = new Intent(this.myContext, Screen_Error_Report.class);
        intent.putExtra("error", "************ CAUSE OF ERROR ************\n\n" + stackTrace.toString() + "\n************ DEVICE INFORMATION ***********\n" + "Brand: " + Build.BRAND + "\n" + "Device: " + Build.DEVICE + "\n" + "Model: " + Build.MODEL + "\n" + "Id: " + Build.ID + "\n" + "Product: " + Build.PRODUCT + "\n" + "\n************ FIRMWARE ************\n" + "SDK: " + Build.VERSION.SDK + "\n" + "Release: " + Build.VERSION.RELEASE + "\n" + "Incremental: " + Build.VERSION.INCREMENTAL + "\n");
        this.myContext.startActivity(intent);
        Process.killProcess(Process.myPid());
        System.exit(10);
    }
}
