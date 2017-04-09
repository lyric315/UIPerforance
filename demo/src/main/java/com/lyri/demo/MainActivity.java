package com.lyri.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lirui on 2017/4/5.
 */

public class MainActivity extends Activity {
    private RecyclerView mList;
    private List<String> mDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mList = (RecyclerView) findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(this));
        initData();
        mList.setAdapter(new ListAdapter(mDatas));
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mDatas.add("Data_"+i);
        }
    }
}
