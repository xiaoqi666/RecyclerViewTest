package com.nini.recyclerviewtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nini.recyclerviewtest.bean.Healthyfood;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * 收藏界面
 */
public class CollectionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_simpletype;
    private TextView tv_foodname;
    private ListView lv_collect;
    private FloatingActionButton fab;
    private ArrayList<Healthyfood> data;
    private MyAdapter myAdapter;
    private boolean isFisrtRegister = true;


    @Override
    protected void onStart() {
        super.onStart();
        if (isFisrtRegister) {//防止多次注册,多次注册会报错
            isFisrtRegister = false;
            //注册
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initView();//初始化布局
        initData();//初始化数据
        initEvent();//初始化事件

    }

    /**
     * 订阅者,接收的是详情页面的信息
     * 若先进入收藏,再进入详情页面,从详情页面取消或者重新收藏,
     * 此订阅者将会接收到消息,对对应的食品进行处理,然后更新listview界面
     *
     * @param healthyfood
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(Healthyfood healthyfood) {
        if (data != null && myAdapter != null) {
            if (healthyfood.isCollected()) {
                data.add(healthyfood);
            } else {
                data.remove(healthyfood);
            }
            myAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        myAdapter = new MyAdapter();
        lv_collect.setAdapter(myAdapter);

        //设置ListView Item的点击事件,跳转到详情页面
        lv_collect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CollectionActivity.this, DetailActivity.class);
                int i = Healthyfood.datas.indexOf(data.get(position));
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });

        ////设置ListView Item的长按事件
        lv_collect.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;//消化掉此事件,不再进行传递
            }
        });
    }

    /**
     * 从收藏列表删除的提示对话框
     *
     * @param position 食物的位置信息(data集合中的,该集合中全部都是被收藏的食品,从总食品集合中挑选出来的,被收藏的)
     */
    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除");
        builder.setMessage("确定删除" + data.get(position).getFoodName() + "?");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Healthyfood healthyfood = data.get(position);
                //更新该食物的状态为false,即未收藏状态
                healthyfood.setCollected(false);
                //将该食物从收藏列表中移除
                data.remove(healthyfood);
                myAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 准备数据
     * <p>
     * 将被收藏的食品从总食品列表中挑选出来
     */
    private void initData() {
        data = new ArrayList<>();
        //遍历被收藏的数据
        for (int i = 0; i < Healthyfood.datas.size(); i++) {
            Healthyfood healthyfood = Healthyfood.datas.get(i);
            if (healthyfood.isCollected()) {
                data.add(healthyfood);
            }
        }
    }

    /**
     * 初始化布局
     */
    private void initView() {
        tv_simpletype = (TextView) findViewById(R.id.tv_simpletype);
        tv_foodname = (TextView) findViewById(R.id.tv_foodname);
        lv_collect = (ListView) findViewById(R.id.lv_collect);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(CollectionActivity.this, MainActivity.class);
                intent.putExtra("activity", "CollectionActivity");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myAdapter != null) {
            myAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 收藏列表的适配器
     */
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
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
                convertView = View.inflate(CollectionActivity.this, R.layout.item_lv_food, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Healthyfood healthyfood = data.get(position);
            holder.tv_foodname.setText(healthyfood.getFoodName());
            holder.tv_simpletype.setText(healthyfood.getSimpleType());
            return convertView;
        }

        public class ViewHolder {
            public View rootView;
            public TextView tv_simpletype;
            public TextView tv_foodname;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.tv_simpletype = (TextView) rootView.findViewById(R.id.tv_simpletype);
                this.tv_foodname = (TextView) rootView.findViewById(R.id.tv_foodname);
            }

        }
    }


}
