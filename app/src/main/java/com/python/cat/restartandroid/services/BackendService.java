package com.python.cat.restartandroid.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.restartandroid.utils.NotificationTools;

public class BackendService extends Service {

    private String channelId;
    private NotificationManager mNotificationManager;

    public BackendService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        LogUtils.d(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.d(intent);
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d("BackendServices onCreate...");
        mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager == null) {
            throw new NullPointerException("can't get NotificationManager,but why?");
        }
        PendingIntent mi = PendingIntent.getActivity(this, 0,
                new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mi.com")), 0);

        Notification notification = NotificationTools
                .builder(mNotificationManager, this)
                .setContentTitle("我来自服务")
                .setContentText("点我打开小米官网")
                .setContentIntent(mi)
                .build();
        channelId = notification.getChannelId();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(intent + " , " + flags + " , " + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy...");
        if (mNotificationManager != null) {
            if (channelId != null) {
                mNotificationManager.deleteNotificationChannel(channelId);
            }
            mNotificationManager.cancelAll();
        }
    }
}
