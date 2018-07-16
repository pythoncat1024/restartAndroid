package com.python.cat.restartandroid;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.restartandroid.base.BaseActivity;
import com.python.cat.restartandroid.services.BinderService;

public class RabbitActivity extends BaseActivity {

    private ServiceConnection connection;
    private BinderService.InnerBinder binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rabbit);
        connection = new InnerConnection();
        bindService(new Intent(get(), BinderService.class),
                connection,
                Context.BIND_AUTO_CREATE);
    }

    private class InnerConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.w(name + " # " + service);
            if (service instanceof BinderService.InnerBinder) {
                binder = (BinderService.InnerBinder) service;
                BinderService se = binder.getService(result -> {
                    LogUtils.w(Thread.currentThread().getName() + " ### " + result);
                    LogUtils.i(Thread.currentThread().getName() + " ### " + result);
                });

                se.add(3, 4);
            } else {
                throw new RuntimeException(service.toString()
                        + " is not the target IBinder object");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.w(name);
        }
    }

    @Override
    protected void onDestroy() {
        if (connection != null) {
            unbindService(connection);
        }
        super.onDestroy();


    }
}
