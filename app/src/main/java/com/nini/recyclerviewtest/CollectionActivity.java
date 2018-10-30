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
     * 订阅者,接收的是收藏页面的信息
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


    private void initEvent() {
        myAdapter = new MyAdapter();
        lv_collect.setAdapter(myAdapter);

        lv_collect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CollectionActivity.this, DetailActivity.class);
                int i = Healthyfood.datas.indexOf(data.get(position));
                intent.putExtra("position", i);
                startActivity(intent);
                //  finish();//关闭这个界面
            }
        });

        lv_collect.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });
    }

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
                healthyfood.setCollected(false);
                data.remove(healthyfood);
                myAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 准备数据
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
