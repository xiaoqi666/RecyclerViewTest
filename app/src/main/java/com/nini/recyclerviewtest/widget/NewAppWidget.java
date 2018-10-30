package com.nini.recyclerviewtest.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.nini.recyclerviewtest.DetailActivity;
import com.nini.recyclerviewtest.MainActivity;
import com.nini.recyclerviewtest.R;
import com.nini.recyclerviewtest.bean.Healthyfood;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    public static final String LUNCH_APP = "com.lv.appstart";//启动app
    public static final String ALREAD_COLLECTION = "alread_collection";//已收藏
    public static final String TODAY_RECOMMEND = "today_recommend";//今日推荐


    /**
     * 自动生成的方法,没感觉有什么用
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews updateView = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);//实例化RemoteView,其对应相应的Widget布局
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        updateView.setOnClickPendingIntent(R.id.iv, pi); //设置点击事件
        updateView.setTextViewText(R.id.tv_content, "当前没有任何信息");
        ComponentName me = new ComponentName(context, NewAppWidget.class);
        appWidgetManager.updateAppWidget(me, updateView);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (action.equals(LUNCH_APP)) {
            int intExtra = intent.getIntExtra("position", 0);
            Healthyfood healthyfood = Healthyfood.datas.get(intExtra);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName me = new ComponentName(context, NewAppWidget.class/*被通知的类*/);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            rv.setTextViewText(R.id.tv_content, "今日推荐  " + healthyfood.getFoodName());
            //如果文本显示的是今日推荐,点击后跳转到食物详情页面
            Intent i = new Intent(context, DetailActivity.class);
            i.putExtra("position", intExtra);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.iv, pi); //设置点击事件
            appWidgetManager.updateAppWidget(me, rv);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

