package com.lyri.uiperformance.core.Intercepter;

/**
 * Created by lirui on 2017/4/5.
 */

public interface ISamplerAction {
    /**
     * 非UI线程，可以进行耗时擦欧走
     */
    void doSamplerAction();
}
