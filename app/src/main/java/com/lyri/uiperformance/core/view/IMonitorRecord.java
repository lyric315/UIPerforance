package com.lyri.uiperformance.core.view;

/**
 * Created by lirui on 2017/4/5.
 */

public interface IMonitorRecord {
    /**
     * 向Monitor悬浮窗中添加性能记录
     * @param tvName 名称
     * @param tvNum  数值
     */
    void addOneRecord(String tvName, String tvNum);

    /**
     * 在Monitor悬浮窗中删除某条记录
     * @param tvName
     */
    void removeRecord(String tvName);
}
