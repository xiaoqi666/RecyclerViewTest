package com.nini.recyclerviewtest.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nini.recyclerviewtest.CollectionActivity;
import com.nini.recyclerviewtest.bean.Healthyfood;
import com.nini.recyclerviewtest.utils.NotifcationUtil;

public class MyDyncBroadcastReceiver extends BroadcastReceiver {

    private static int id = 100;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("TAG","-------------------MyDyncBroadcastReceiver------------");
        String action = intent.getAction();
        if (action.equals("com.lw.collection")) {
            int position = intent.getIntExtra("position", 0);
            NotifcationUtil.showNotifcation(context, position, "已收藏",
                    Healthyfood.datas.get(position).getFoodName(),
                    CollectionActivity.class
                    , id++);
        }
    }
}
