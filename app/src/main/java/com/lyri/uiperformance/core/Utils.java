package com.lyri.uiperformance.core;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by lirui on 2017/4/6.
 */

public class Utils {
    public static float dpToPixel(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density * dp;
    }


}
