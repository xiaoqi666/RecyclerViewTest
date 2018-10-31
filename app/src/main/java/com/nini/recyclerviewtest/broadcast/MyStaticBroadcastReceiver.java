package com.nini.recyclerviewtest.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nini.recyclerviewtest.DetailActivity;
import com.nini.recyclerviewtest.bean.Healthyfood;
import com.nini.recyclerviewtest.utils.NotifcationUtil;

/**
 * 静态广播
 * <p>
 * app启动的时候触发,发送通知栏通知,同时NewAppWidget也会接收到该广播
 * 两个广播接受者都在AndroidManifest.xml中配置了action常量(<action android:name="com.lv.appstart" />)
 */
public class MyStaticBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("TAG", "-----MyStaticBroadcastReceiver-----------------");

        if (action.equals("com.lv.appstart")) {//app启动的广播
            int intExtra = intent.getIntExtra("position", 0);
            //发送通知栏消息
            NotifcationUtil.showNotifcation(context, intExtra,
                    "今日推荐",
                    Healthyfood.datas.get(intExtra).getFoodName(),
                    DetailActivity.class, 10);//通知栏显示推荐信息
        }
    }


}
