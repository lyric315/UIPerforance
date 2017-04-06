package com.lyri.uiperformance.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lyri.uiperformance.R;
import com.lyri.uiperformance.core.UIPerformance;

/**
 * Created by lirui on 2017/4/5.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        UIPerformance.getInstance(this).start();
    }
}
