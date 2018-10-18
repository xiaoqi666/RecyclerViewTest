package com.nini.recyclerviewtest;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nini.recyclerviewtest.adapter.MyAdapter;
import com.nini.recyclerviewtest.bean.Healthyfood;
import com.nini.recyclerviewtest.broadcast.MyStaticBroadcastReceiver;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView recyclerview;
    private FloatingActionButton fab;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (Healthyfood.datas == null)
            initData();//初始化展示的数据
        initEvent();//初始化事件
        initBoardCast();//发送广播
    }

    /**
     * 启动app的时候发送广播
     */
    private void initBoardCast() {
        Intent intent = new Intent("com.lv.appstart");
        Random random = new Random();
        int position = random.nextInt(Healthyfood.datas.size());
        intent.putExtra("position", position);
       // if (Build.VERSION.SDK_INT >= 26) {//判断版本号是否大于26
            //Android8.0  需要设置ComponentName
            intent.setComponent(new ComponentName(this/*上下文对象*/, MyStaticBroadcastReceiver.class/*广播接收者的类*/));
      //  }


        sendBroadcast(intent);
        Log.e("TAG", "------------initBoardCast-------------------");
    }

    private void initEvent() {
        myAdapter = new MyAdapter(this);
        recyclerview.setAdapter(myAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL/*垂直*/, false/*不逆序*/));

        recyclerview.setItemAnimator(new DefaultItemAnimator());//默认动画


        //条目被点击的时候
        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        //条目被长按的时候
        myAdapter.setOnItemLongClickListener(new MyAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                //获得被移除的对象
                Healthyfood healthyfood = myAdapter.remove(position);
                Toast.makeText(MainActivity.this, "**删除" + healthyfood.getFoodName() + "**", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 初始化展示的数据
     */
    private void initData() {
        Healthyfood.datas = new ArrayList<>();
        //大豆
        Healthyfood soybean = new Healthyfood("大豆", "粮", "粮食", "蛋白质", "#BB4C3B");
        Healthyfood.datas.add(soybean);
        //十字花科蔬菜
        Healthyfood cruciferousVegetables = new Healthyfood("十字花科蔬菜", "蔬", "蔬菜", "维生素C", "#C48D30");
        Healthyfood.datas.add(cruciferousVegetables);
        //牛奶
        Healthyfood milk = new Healthyfood("牛奶", "饮", "饮品", "钙", "#4469B0");
        Healthyfood.datas.add(milk);
        //海鱼
        Healthyfood seaFish = new Healthyfood("海鱼", "肉", "肉食", "蛋白质", "#20A17B");
        Healthyfood.datas.add(seaFish);
        //菌菇类
        Healthyfood mushroom = new Healthyfood("菌菇类", "蔬", "蔬菜", "微量元素", "#BB4C3B");
        Healthyfood.datas.add(mushroom);
        //番茄
        Healthyfood tomato = new Healthyfood("番茄", "蔬", "蔬菜", "番茄红素", "#4469B0");
        Healthyfood.datas.add(tomato);
        //胡萝卜
        Healthyfood carrot = new Healthyfood("胡萝卜", "蔬", "蔬菜", "胡萝卜素", "#20A17B");
        Healthyfood.datas.add(carrot);
        //荞麦
        Healthyfood buckwheat = new Healthyfood("荞麦", "粮", "粮食", "膳食纤维", "#BB4C3B");
        Healthyfood.datas.add(buckwheat);
        //鸡蛋
        Healthyfood egg = new Healthyfood("鸡蛋", "杂", "杂", "几乎所有营养物质", "#C48D30");
        Healthyfood.datas.add(egg);
    }


    private void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab://跳转到收藏页面
                Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
                startActivity(intent);
                break;
        }
    }

    private long firstTime;

    /**
     * 双击back退出
     */
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            finish();
        }
    }
}
