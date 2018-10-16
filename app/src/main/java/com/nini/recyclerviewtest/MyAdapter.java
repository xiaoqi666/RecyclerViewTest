package com.nini.recyclerviewtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Healthyfood> datas;
    private Context mContxt;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public MyAdapter(Context context) {
        this.mContxt = context;
        this.datas = Healthyfood.datas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(mContxt, R.layout.item_lv_food, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        //设置数据
        Healthyfood healthyfood = datas.get(i);
        holder.tvFoodname.setText(healthyfood.getFoodName());
        holder.tvSimpletype.setText(healthyfood.getSimpleType());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSimpletype;
        private TextView tvFoodname;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tvSimpletype = (TextView) view.findViewById(R.id.tv_simpletype);
            tvFoodname = (TextView) view.findViewById(R.id.tv_foodname);

            //监控item的点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, getLayoutPosition());
                    }
                }
            });

            //设置item的长按事件
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, getLayoutPosition());
                    }
                    return true;//消化掉事件,不再向下传递(不然触发  点击事件)
                }
            });
        }
    }

    /**
     * 长按某条的事件监听
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    /**
     * 设置长按事件
     *
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 点击RecyclerView 某条的监听
     */
    public interface OnItemClickListener {
        /**
         * 某条被点击的时候回调
         *
         * @param view     点击的布局
         * @param position 点击的位置
         */
        void onItemClick(View view, int position);
    }

    /**
     * 设置点击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    /**
     * 移除某条数据
     */
    public Healthyfood remove(int position) {
        Log.e("TAG", "--------------" + position);
        Healthyfood healthyfood = datas.remove(position);
        notifyItemRemoved(position);//移除
        return healthyfood;
    }


    /**
     * 添加数据
     */

    public void add(int position, Healthyfood healthyfood) {
        datas.add(position, healthyfood);
        notifyItemInserted(position);
    }

}
