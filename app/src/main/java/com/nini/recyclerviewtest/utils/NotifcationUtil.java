package com.nini.recyclerviewtest.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.nini.recyclerviewtest.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotifcationUtil {

    private static NotificationManager mNotifyMgr;

    /**
     * 通知栏消息
     *
     * @param context
     * @param intExtra
     */
    public static void showNotifcation(Context context, int intExtra, String title, String content, Class clazz, int id) {
        mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, getChannlId());
        Intent intent = new Intent(context, clazz);
        intent.putExtra("position", intExtra);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentTitle(title)//设置通知栏标题
                .setContentText(content).setContentIntent(pendingIntent)
                .setAutoCancel(true).setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.empty_star))
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(R.drawable.empty_star);//设置通知小ICON
        mNotifyMgr.notify(id, mBuilder.build());//第一个参数为自定义的通知唯一标识
    }


    /**
     * 适配android 8.0
     *
     * @return
     */
    public static String getChannlId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Build.VERSION_CODES.O==26
            String ChannelId = "1";
            String ChannelName = "ChannelName";
            NotificationChannel channel = new NotificationChannel(ChannelId,
                    ChannelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            mNotifyMgr.createNotificationChannel(channel);
            return ChannelId;
        }
        return null;
    }


}
