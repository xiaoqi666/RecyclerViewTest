package com.nini.recyclerviewtest.broadcast;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nini.recyclerviewtest.DetailActivity;
import com.nini.recyclerviewtest.bean.Healthyfood;
import com.nini.recyclerviewtest.utils.NotifcationUtil;

public class MyStaticBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("TAG", "-----MyStaticBroadcastReceiver-----------------");

        if (action.equals("com.lv.appstart")) {//app启动的广播
            int intExtra = intent.getIntExtra("position", 0);
            NotifcationUtil.showNotifcation(context, intExtra,
                    "今日推荐",
                    Healthyfood.datas.get(intExtra).getFoodName(),
                    DetailActivity.class, 10);//通知栏显示推荐信息
        }
    }


}
