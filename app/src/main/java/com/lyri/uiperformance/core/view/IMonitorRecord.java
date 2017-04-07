package com.lyri.uiperformance.core.view;

/**
 * Created by lirui on 2017/4/5.
 */

public interface IMonitorRecord {
    /**
     * 向Monitor悬浮窗中添加性能记录
     *
     * @param tvName      名称
     * @param tvNum       数值
     * @param isGoodValue 该性能参数的好坏，在悬浮窗中通过颜色来区分
     */
    void addOneRecord(String tvName, String tvNum, boolean isGoodValue);

    /**
     * 在Monitor悬浮窗中删除某条记录
     *
     * @param tvName
     */
    void removeRecord(String tvName);
}
