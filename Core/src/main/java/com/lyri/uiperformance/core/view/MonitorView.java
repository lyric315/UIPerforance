package com.lyri.uiperformance.core.view;

import android.animation.Animator;
import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyri.uiperformance.R;

import java.util.HashMap;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by lirui on 2017/4/5.
 * 监控悬浮窗
 */

public class MonitorView extends Thread implements IMonitorView, IMonitorRecord {
    private LinearLayout mMonitorView;
    private WindowManager mWindowManager;
    private Context mContext;
    private Handler mHandler;

    private volatile boolean isShow;

    private HashMap<String, TextView> mMapView = new HashMap<>();

    public MonitorView(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
        mContext = context;
    }

    @Override
    public void addOneRecord(final String tvName, final String tvNum, final boolean isGoodValue) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (tvName == null || tvNum == null) {
                    return;
                }

                String tvResult = tvName + ": " + tvNum;
                TextView tv = mMapView.get(tvName);
                if (tv == null) {
                    tv = new TextView(mMonitorView.getContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                    lp.setMargins(8, 8, 8, 8);
                    tv.setLayoutParams(lp);
                    tv.setTextSize(15);
                    mMonitorView.addView(tv);
                    mMapView.put(tvName, tv);
                }

                if (isGoodValue) {
                    tv.setTextColor(mContext.getResources().getColor(R.color.monitor_nomal_color));
                } else {
                    tv.setTextColor(mContext.getResources().getColor(R.color.monitor_error_color));
                }

                tv.setText(tvResult);
            }
        });
    }

    @Override
    public void removeRecord(final String tvName) {
        if (tvName == null || tvName.isEmpty()) {
            return;
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView tv = mMapView.get(tvName);
                if (tv != null) {
                    mMonitorView.removeView(tv);
                    mMapView.remove(tvName);
                }
            }
        });
    }

    @Override
    public View getMonitorView() {
        return mMonitorView;
    }

    @Override
    public void show() {
        //悬浮窗已经在显示
        if (isShow) {
            return;
        }

        //不需要在开启一次线程
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    attachMonitorViewToWindown();
                }
            });
            return;
        }

        //开启线程，监控悬浮窗的绘制不在主线程
        start();
    }

    @Override
    public void close() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                hide(true);
            }
        });
    }

    @Override
    public void run() {
        Looper.prepare();

        attachMonitorViewToWindown();

        /**
         * 这里用的Application的context，所以不会存在内存泄漏
         */
        mHandler = new Handler();

        Looper.loop();
    }

    private void initMonitorView(Context context) {
        mMonitorView = new LinearLayout(context);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        mMonitorView.setLayoutParams(lp);
        mMonitorView.setBackgroundResource(R.drawable.monitor_bg);
        mMonitorView.setOrientation(LinearLayout.VERTICAL);
    }

    private void attachMonitorViewToWindown() {
        if (mMonitorView == null) {
            initMonitorView(mContext);
        }

        mMonitorView.setAlpha(0f);
        mMonitorView.animate()
                .alpha(1f)
                .setDuration(200)
                .setListener(null);
        mMonitorView.setVisibility(View.VISIBLE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.END;
        params.x = 20;
        params.y = 20;
        mWindowManager.addView(mMonitorView, params);

        GestureDetector gestureDetector = new GestureDetector(mMonitorView.getContext(), simpleOnGestureListener);
        mMonitorView.setOnTouchListener(new FpsTouchListener(params, mWindowManager, gestureDetector));
        mMonitorView.setHapticFeedbackEnabled(false);

        isShow = true;
    }

    public void hide(final boolean remove) {
        mMonitorView.animate()
                .alpha(0f)
                .setDuration(100)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mMonitorView.setVisibility(View.GONE);
                        if (remove) {
                            mWindowManager.removeView(mMonitorView);
                        }
                        isShow = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });

    }

    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            hide(false);
            return super.onDoubleTap(e);
        }
    };

    private static class FpsTouchListener implements View.OnTouchListener {
        private int initialX;
        private int initialY;
        private float initialTouchX;
        private float initialTouchY;

        private WindowManager.LayoutParams paramsF;
        private WindowManager windowManager;
        private GestureDetector gestureDetector;

        public FpsTouchListener(WindowManager.LayoutParams paramsF,
                                WindowManager windowManager, GestureDetector gestureDetector) {
            this.windowManager = windowManager;
            this.paramsF = paramsF;
            this.gestureDetector = gestureDetector;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = paramsF.x;
                    initialY = paramsF.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    paramsF.x = initialX - (int) (event.getRawX() - initialTouchX);
                    paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                    windowManager.updateViewLayout(v, paramsF);
                    break;
            }
            return false;
        }

    }

}
