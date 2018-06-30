package com.python.cat.restartandroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.apkfuns.logutils.Constant;
import com.apkfuns.logutils.LogUtils;
import com.python.cat.restartandroid.services.BackendService;
import com.python.cat.restartandroid.utils.Common;
import com.python.cat.restartandroid.utils.NotificationTools;

public class MainActivity extends AppCompatActivity {

    private NotificationManager mNotificationManager;

    private String channelId;
    private InnerBroadcastReceiver receiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        receiver = new InnerBroadcastReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Common.INNERACTION);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(receiver, intentFilter);

        View btnSendNotification = findViewById(R.id.show_notification);
        View btnStartService = findViewById(R.id.start_service);
        View btnSendBroadcast = findViewById(R.id.send_broadcast);
        click2sendNotification(btnSendNotification);
        click2startService(btnStartService);
        click2sendBroadcast(btnSendBroadcast);

    }

    private void click2sendBroadcast(View btnSendBroadcast) {
        btnSendBroadcast.setOnClickListener(v -> {
            LogUtils.w("click 2 send broadcast");
            Intent intent = new Intent();
            intent.setAction(Common.INNERACTION);
            intent.putExtra("aaa", "bbb");
            LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(intent);
        });
    }

    private void click2startService(View btnStartService) {
        btnStartService
                .setOnClickListener(v -> {
                            LogUtils.w("click 2 start service...");
                            startService(new Intent(MainActivity.this,
                                    BackendService.class));
                        }
                );
    }

    private void click2sendNotification(View viewById) {
        Notification notification = NotificationTools
                .builder(mNotificationManager, this.getApplicationContext())
                .setContentIntent(PendingIntent
                        .getForegroundService(this,
                                0,new Intent(MainActivity.this,
                                        BackendService.class),0))
                .build();
        viewById.setOnClickListener((v) -> {
            LogUtils.w("view click...");
            channelId = notification.getChannelId();
            mNotificationManager.notify(1, notification);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (channelId != null) {
            mNotificationManager.deleteNotificationChannel(channelId);
        }
        mNotificationManager.cancelAll();
        if (receiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        }
    }


    private class InnerBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.w("inner receive.." + context + " , " + intent);
            String action = intent.getAction();
            LogUtils.w("receive action: " + action);

            Toast.makeText(MainActivity.this,
                    "收到广播了...",Toast.LENGTH_SHORT).show();
        }
    }
}
