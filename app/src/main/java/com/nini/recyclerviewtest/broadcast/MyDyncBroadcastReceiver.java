package com.nini.recyclerviewtest.broadcast;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.nini.recyclerviewtest.CollectionActivity;
import com.nini.recyclerviewtest.DetailActivity;
import com.nini.recyclerviewtest.R;
import com.nini.recyclerviewtest.bean.Healthyfood;
import com.nini.recyclerviewtest.utils.NotifcationUtil;
import com.nini.recyclerviewtest.widget.NewAppWidget;

/**
 * 动态广播
 * <p>
 * 点击收藏出发,接收到广播后做两件事:
 * 一是发送通知栏消息
 * 二是更新widget界面 "已收藏 XXX"
 */
public class MyDyncBroadcastReceiver extends BroadcastReceiver {

    private static int id = 100;


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("com.lw.collection")) {
            int position = intent.getIntExtra("position", 0);
            NotifcationUtil.showNotifcation(context, position, "已收藏",
                    Healthyfood.datas.get(position).getFoodName(), CollectionActivity.class, id++);
            //更新widget
            updateWidget(context, intent);
        }
    }

    /**
     * 更新widget界面
     *
     * @param context
     * @param intent
     */
    private void updateWidget(Context context, Intent intent) {
        int intExtra = intent.getIntExtra("position", 0);
        //根据传递过来的食品位置,获取对应的食品
        Healthyfood healthyfood = Healthyfood.datas.get(intExtra);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName me = new ComponentName(context, NewAppWidget.class/*被通知的类*/);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        rv.setTextViewText(R.id.tv_content, "已收藏  " + healthyfood.getFoodName());
        //设置星星图片被点击后跳转的位置,即收藏界面
        Intent i = new Intent(context, CollectionActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.iv, pi); //设置点击事件
        appWidgetManager.updateAppWidget(me, rv);

    }
}
