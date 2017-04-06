package com.lyri.uiperformance.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.lyri.uiperformance.core.Intercepter.SamplerIntercepter;
import com.lyri.uiperformance.core.view.IMonitorRecord;
import com.lyri.uiperformance.core.view.IMonitorView;
import com.lyri.uiperformance.core.view.MonitorView;

import java.util.List;

/**
 * Created by lirui on 2017/4/5.
 */

public class PerformanceCore implements Application.ActivityLifecycleCallbacks {
    private SamplerThread mSamplerThread;
    private SamplerIntercepter mSamplerIntercepter;
    private IMonitorView mMonitorViewWrapper;
    private Context mContext;
    private boolean mIsForeground;//APP是否位于前台
    private boolean mStart;//是否已开启

    public PerformanceCore(Context context) {
        this.mContext = context;
    }

    public void start() {
        mStart = true;
        mIsForeground = true;


        if (mMonitorViewWrapper == null) {
            mMonitorViewWrapper = new MonitorView(mContext);
        }
        mMonitorViewWrapper.show();

        if (mMonitorViewWrapper instanceof IMonitorRecord) {
            mSamplerIntercepter = new SamplerIntercepter((IMonitorRecord) mMonitorViewWrapper);
        }
        mSamplerThread = new SamplerThread(mSamplerIntercepter, 16.67f);
        mSamplerThread.startSampling();

        if (mContext instanceof Application) {
            ((Application) mContext).registerActivityLifecycleCallbacks(this);
        }
    }

    public void stop() {
        mStart = false;
        mSamplerThread.stopSampling();
        mMonitorViewWrapper.close();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (mStart) {
            return;
        }

        if (isForegroundApp() && !mIsForeground) {
            mIsForeground = true;
            start();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (!isForegroundApp()) {
            mIsForeground = false;
            stop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    /**
     * 判断APP是否为前台应用
     * @return
     */
    private boolean isForegroundApp() {
        ActivityManager ac = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = mContext.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> runningApps = ac.getRunningAppProcesses();

        if (packageName == null || runningApps == null || runningApps.isEmpty()) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo app : runningApps) {
            if (app.processName.equals(packageName) && app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
