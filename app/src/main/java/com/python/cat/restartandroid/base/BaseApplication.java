package com.python.cat.restartandroid.base;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Created by cat on 2018/6/29.
 * <p>
 * application
 */

public class BaseApplication extends Application
        implements UncaughtExceptionHandler {
    static {
        LogUtils.getLogConfig().configShowBorders(false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.v("application onCreate...");
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LogUtils.e(t);
        LogUtils.e(e);
        System.exit(-1);
    }
}
