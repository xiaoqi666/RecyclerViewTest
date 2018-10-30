package com.nini.recyclerviewtest;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nini.recyclerviewtest.bean.Healthyfood;
import com.nini.recyclerviewtest.broadcast.MyDyncBroadcastReceiver;

import org.greenrobot.eventbus.EventBus;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton ib_back;
    private TextView tv_name;
    private ImageButton ib_star;
    private RelativeLayout rl;
    private TextView tv_type;
    private TextView tv_yingyang;
    private Healthyfood healthyfood;
    private ImageButton ib_collect;
    private ListView lv_detail;
    private int position;
    private MyDyncBroadcastReceiver myDyncBroadcastReceiver;
    private Intent intent;

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myDyncBroadcastReceiver);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /**
         * 注册广播
         */
        myDyncBroadcastReceiver = new MyDyncBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("com.lw.collection");
        registerReceiver(myDyncBroadcastReceiver, intentFilter);


        initView();//初始化布局
        initData();//初始化数据
        initEvent();
    }

    /**
     * 如果activity已经存在, android:launchMode="singleTask"
     * 则第二次启动的时候走这个方法
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.intent = intent;
    }

    private void initEvent() {
        lv_detail.setAdapter(new MyAdapter());
    }

    private void initData() {
        intent = getIntent();
        //获取点击的那个item
        position = intent.getIntExtra("position", 0);
        Log.e("TAG", "-------position----------" + position);
        //获取点击的对象
        healthyfood = Healthyfood.datas.get(position);

        /*
         * 设置界面显示的数据
         */
        tv_name.setText(healthyfood.getFoodName());
        tv_type.setText(healthyfood.getFullType());
        rl.setBackgroundColor(Color.parseColor(healthyfood.getColor()));
        tv_yingyang.setText("富含  " + healthyfood.getNutrientSubstance());

//        if (healthyfood.isCollected()) {//如果当前是收藏状态
//            ib_star.setBackgroundResource(R.drawable.full_star);//设置成实心星星
//        } else {
        ib_star.setBackgroundResource(R.drawable.empty_star);
        //    }
    }

    private void initView() {
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        tv_name = (TextView) findViewById(R.id.tv_name);
        ib_star = (ImageButton) findViewById(R.id.ib_star);
        rl = (RelativeLayout) findViewById(R.id.rl);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_yingyang = (TextView) findViewById(R.id.tv_yingyang);

        ib_back.setOnClickListener(this);
        ib_star.setOnClickListener(this);
        ib_collect = (ImageButton) findViewById(R.id.ib_collect);
        ib_collect.setOnClickListener(this);
        lv_detail = (ListView) findViewById(R.id.lv_detail);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_star:
                //讲收藏状态置反
                healthyfood.setCollected(!healthyfood.isCollected());
                EventBus.getDefault().post(healthyfood);//发送订阅消息
                //根据新的状态,更新小星星
                if (healthyfood.isCollected()) {//如果当前是收藏状态
                    ib_star.setBackgroundResource(R.drawable.full_star);
                    Intent intent = new Intent("com.lw.collection");
                    intent.putExtra("position", position);
                    sendBroadcast(intent);
                } else {
                    ib_star.setBackgroundResource(R.drawable.empty_star);
                }
                break;
            case R.id.ib_collect://调到收藏夹页面
                startActivity(new Intent(DetailActivity.this, CollectionActivity.class));
                finish();//关闭这个界面
                break;
        }
    }

    private String[] items = {"分享信息", "不感兴趣", "查看更多信息", "出错反馈"};

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(DetailActivity.this, R.layout.item_lv_detail, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_text.setText(items[position]);
            return convertView;
        }

        public class ViewHolder {
            public View rootView;
            public TextView tv_text;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.tv_text = (TextView) rootView.findViewById(R.id.tv_text);
            }

        }
    }


}
