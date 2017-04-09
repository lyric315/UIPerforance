package com.lyri.demo;

import android.app.Application;

import com.lyri.uiperformance.core.UIPerformance;


/**
 * Created by lirui on 2017/4/7.
 */

public class UIPerformanceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //引入UI监控
        UIPerformance.getInstance(this).start();
    }
}
