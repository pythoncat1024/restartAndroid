package com.python.cat.restartandroid.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;

import com.python.cat.restartandroid.R;

/**
 * Created by cat on 2018/6/29.
 */

@TargetApi(Build.VERSION_CODES.O)
public class NotificationTools {

    public static Notification.Builder builder(@NonNull NotificationManager mNotificationManager,
                                               @NonNull Context context) {
        // 通知渠道的id
        String channelID = "my_channel_01";
        // 用户可以看到的通知渠道的名字.
        CharSequence name = "通知渠道";
        // 用户可以看到的通知渠道的描述
        String description = "通知渠道描述 | 通知渠道描述 x2";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(channelID, name, importance);
        // 配置通知渠道的属性
        mChannel.setDescription(description);
        // 设置通知出现时的闪灯（如果 android 设备支持的话）
        mChannel.enableLights(true); //是否在桌面icon右上角展示小红点
        mChannel.setLightColor(Color.RED); //小红点颜色
        mChannel.setShowBadge(true); // 是否在久按桌面图标时显示此渠道的通知
        // 设置通知出现时的震动（如果 android 设备支持的话）
//        mChannel.enableVibration(true);
//        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        // 最后在 notificationManager中创建该通知渠道
//
        mNotificationManager.createNotificationChannel(mChannel);

        PendingIntent baidu = PendingIntent.getActivity(context, 0,
                new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com")), 0);


// 为该通知设置一个id
        int notifyID = 1;
        // 通知渠道的id
// Create a notification and set the notification channel.
        return new Notification.Builder(context, mChannel.getId())
                .setContentTitle("通知来了")
                .setContentText("You've received new messages.")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(baidu);

    }
}



