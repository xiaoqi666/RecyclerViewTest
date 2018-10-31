package com.nini.recyclerviewtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.nini.recyclerviewtest.adapter.MyAdapter;
import com.nini.recyclerviewtest.bean.Healthyfood;
import com.nini.recyclerviewtest.widget.NewAppWidget;

import java.util.ArrayList;
import java.util.Random;

/**
 * 食品列表界面
 * RecyclerView + RecyclerView.Adapter + FloatingActionButton
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView recyclerview;
    private FloatingActionButton fab;
    private MyAdapter myAdapter;

    /**
     * AndroidManifest.xml文件中为MainActivity配置了launchMode="singleTask"
     * 所以,该界面二次启动后,将不再走onCreate方法重新实体化界面
     * 如果app启动后才将widget添加到手机桌面,widget处于"当前没有任何消息"的状态,
     * 点击后跳到主界面不走onCreate()方法则不会发送静态广播告知widget更新界面
     * <p>
     * 但这个时候系统会调用onNewIntent方法,所以在此发送广播,告知widget更新界面
     * 即更新到"今日推荐 XXXX食品"
     *
     * @param intent 开启这个界面的intent,此处并不关心它,因为我们并没有为它设置数据
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //发送广播
        initBoardCast();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化布局
        initView();

        //如果集合中的数据为空,初始化展示的数据
        if (Healthyfood.datas == null)
            initData();

        //初始化事件
        initEvent();

        //发送广播
        initBoardCast();
    }

    /**
     * 启动app的时候发送广播
     * NewAppWidget也可以收到
     * 不支持android 8.0,8.0系统需要设置ComponentName
     */
    private void initBoardCast() {
        /**
         * 设置值action常量,并在AndroidManifest.xml中进行配置
         * 一个是NewAppWidget,一个是MyStaticBroadcastReceiver
         * 因此,发送的广播,这两个类都可以接收到
         *
         * MyStaticBroadcastReceiver 接收到后会发送通知栏消息
         * NewAppWidget被重写了onReceive方法,接收到后会更新widget的界面,为"今日推荐 XXX"
         */
        Intent intent = new Intent(NewAppWidget.LUNCH_APP);
        //随机生成一个小于食品列表个数的数,也就是随机产生一个index,
        //该index便是被随机推荐的食物在Healthyfood.datas中的位置
        Random random = new Random();
        int position = random.nextInt(Healthyfood.datas.size());
        //intent携带随机推荐的食品的位置信息,传递给广播接受者
        intent.putExtra("position", position);
        //发送广播
        sendBroadcast(intent);
    }


    /**
     * 初始化事件
     * recyclerview设置Adapter(适配器)
     * recyclerview条目的长按事件和点击事件(这两个事件是由接口回调实现的,recyclerview和RecyclerView.Adapter本身并没有这两个事件)
     */
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
                //传递食品的位置到详情页面,然后详情页面根据食品在集合中的位置获取食品对象,进行详情展示
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


    /**
     * 初始化页面布局
     */
    private void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(this);
    }

    /**
     * 点击事件 该方法就一个FloatingActionButton的点击跳转到收藏页面
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                //跳转到收藏页面
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
