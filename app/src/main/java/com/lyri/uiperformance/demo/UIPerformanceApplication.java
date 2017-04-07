package com.lyri.uiperformance.demo;

import android.app.Application;

import com.lyri.uiperformance.core.UIPerformance;

/**
 * Created by lirui on 2017/4/7.
 */

public class UIPerformanceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UIPerformance.getInstance(this).start();
    }
}
