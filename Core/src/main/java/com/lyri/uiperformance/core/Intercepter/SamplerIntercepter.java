package com.lyri.uiperformance.core.Intercepter;

import com.lyri.uiperformance.core.SamplerThread;
import com.lyri.uiperformance.core.view.IMonitorRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lirui on 2017/4/5.
 * 采样拦截器
 */

public class SamplerIntercepter implements SamplerThread.ISamplerHandler {
    private List<ISamplerAction> mActionList = new ArrayList<>();

    public SamplerIntercepter(IMonitorRecord addMonitorRecord) {
        mActionList.add(new FPSSamplerAction(addMonitorRecord));
        mActionList.add(new MemSamplerAction(addMonitorRecord));
        mActionList.add(new CPUSamplerAction(addMonitorRecord));
    }

    @Override
    public void doSamplerEvent() {
        if (mActionList == null) {
            return;
        }

        for (ISamplerAction action : mActionList) {
            action.doSamplerAction();
        }
    }

    public void registerSamplerAction(ISamplerAction action) {
        if (action == null) {
            return;
        }
        mActionList.add(action);
    }

    public void unRegisterSamplerAction(ISamplerAction action) {
        if (action == null) {
            return;
        }
        mActionList.remove(action);
    }
}
