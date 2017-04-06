package com.lyri.uiperformance.core;

import android.content.Context;

/**
 * Created by lirui on 2017/4/5.
 * UI性能监控
 */
public class UIPerformance {
    private static volatile UIPerformance sUIPerformance;
    private PerformanceCore mCore;

    private UIPerformance(Context context) {
        mCore = new PerformanceCore(context);
    }

    public static UIPerformance getInstance(Context context) {
        if (sUIPerformance == null) {
            synchronized (UIPerformance.class) {
                if (sUIPerformance == null) {
                    sUIPerformance = new UIPerformance(context.getApplicationContext());
                }
            }
        }
        return sUIPerformance;
    }

    /**
     * 初始化
     */
    public void init() {

    }

    public void start() {
        mCore.start();
    }

    public void stop() {
        mCore.stop();
    }
}
