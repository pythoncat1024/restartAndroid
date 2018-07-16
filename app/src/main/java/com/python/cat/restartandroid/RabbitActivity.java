package com.python.cat.restartandroid;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.restartandroid.base.BaseActivity;
import com.python.cat.restartandroid.services.BinderService;

import java.util.Random;

public class RabbitActivity extends BaseActivity {

    private ServiceConnection connection;
    private BinderService.InnerBinder binder;
    private Thread th;
    private static Handler hh;

    private boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rabbit);
        connection = new InnerConnection();
        bindService(new Intent(get(), BinderService.class),
                connection,
                Context.BIND_AUTO_CREATE);

        TextView tv = findViewById(R.id.rabbit_tv);
        findViewById(R.id.btn_change)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!first) {
                            th = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    hh = new Handler(Looper.myLooper()) {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            super.handleMessage(msg);
                                            LogUtils.w("aaa===>" + msg.what);
                                            // 不能更新UI的，这种，
// android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
                                            // tv.setText("aaa==>" + msg.what);
                                        }
                                    };

                                    Looper.loop();
                                }
                            });
                            th.start();
                            first = true;
                        }
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (hh != null) {
                            hh.sendEmptyMessage(new Random().nextInt(100));
                        }
                    }
                });
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
                    Looper.prepare();
                    Handler h = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
//                            setTitle("result===" + msg.what);
                            LogUtils.i("result--->< " + msg.what);
                        }
                    };
                    h.sendEmptyMessage(result);
                    Looper.loop();
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
