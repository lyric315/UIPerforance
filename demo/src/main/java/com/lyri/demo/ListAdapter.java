package com.lyri.demo;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lirui on 2017/4/7.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.SimpleHolder>{
    private List<String> mListData;

    public ListAdapter(List<String> datas) {
        mListData = datas;
    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        // 实例化viewholder
        SimpleHolder viewHolder = new SimpleHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SimpleHolder holder, int position) {
        // 绑定数据
        if (position % 5 ==0) {
            try {
                Thread.sleep(300);
                holder.mTv.setTextColor(Color.RED);
                holder.mTv.setText(mListData.get(position)+"_300ms耗时");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            holder.mTv.setTextColor(Color.BLACK);
            holder.mTv.setText(mListData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    static class SimpleHolder extends RecyclerView.ViewHolder {
        TextView mTv;

        public SimpleHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.item_tv);
        }
    }
}
