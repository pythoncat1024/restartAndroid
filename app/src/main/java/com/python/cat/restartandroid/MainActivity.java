package com.python.cat.restartandroid;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.restartandroid.services.BackendService;
import com.python.cat.restartandroid.utils.NotificationTools;
import com.python.cat.restartandroid.utils.NotificationUtils;

import java.nio.channels.Channel;

public class MainActivity extends AppCompatActivity {

    private NotificationManager mNotificationManager;
    private String channelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        View btnSendNotification = findViewById(R.id.show_notification);
        View btnStartService = findViewById(R.id.start_service);
        clickView(btnSendNotification);


        btnStartService
                .setOnClickListener(v -> {
                            LogUtils.w("click 2 start service...");
                            startService(new Intent(MainActivity.this,
                                    BackendService.class));
                        }
                );

    }

    private void clickView(View viewById) {
        Notification notification = NotificationTools
                .builder(mNotificationManager, this.getApplicationContext())
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
    }
}
