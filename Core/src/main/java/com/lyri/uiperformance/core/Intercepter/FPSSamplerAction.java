package com.lyri.uiperformance.core.Intercepter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Choreographer;

import com.lyri.uiperformance.core.view.IMonitorRecord;

import java.util.concurrent.TimeUnit;

/**
 * Created by lirui on 2017/4/5.
 */

public class FPSSamplerAction extends BaseSamplerAction implements Choreographer.FrameCallback {
    private IMonitorRecord mMonitorRecord;
    private Choreographer mChoreographer;
    private long mLastFrameStartTime;//上一次Frame渲染的时间
    private int mFrameRendered;//Frame渲染的次数
    private int mFPS;//最新的帧率
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private static final int FPS_LIMIT = 30;//最低帧率，低于这个帧代表页面不流畅
    private static final int INTERVAL = 300;//Frame采样时间
    private static final String FPS = "FPS";

    public FPSSamplerAction(IMonitorRecord addMonitorRecord) {
        super(addMonitorRecord);
        this.mMonitorRecord = addMonitorRecord;
        mChoreographer = Choreographer.getInstance();
        mChoreographer.postFrameCallback(this);
    }

    @Override
    public void doSamplerAction() {
        if (mMonitorRecord != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mFPS < FPS_LIMIT) {
                        mMonitorRecord.addOneRecord(FPS,  String.valueOf(mFPS), false);
                    } else {
                        mMonitorRecord.addOneRecord(FPS, String.valueOf(mFPS), true);
                    }
                }
            });
        }
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        long currentTimeMillis = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos);

        if (mLastFrameStartTime > 0) {
            final long timeSpace = currentTimeMillis - mLastFrameStartTime;
            mFrameRendered++;

            if (timeSpace >= INTERVAL) {
                mFPS = (int) (mFrameRendered * 1000 / timeSpace);
                mLastFrameStartTime = currentTimeMillis;
                mFrameRendered = 0;
            }

            Log.v("fps", mFrameRendered + " " + mFPS);
        } else {
            mLastFrameStartTime = currentTimeMillis;
        }

        mChoreographer.postFrameCallback(this);
    }
}
