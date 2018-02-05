package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *  <li>totayTasksCount  : 当前总任务数</li>
 *  <li>todayCompleteCount: 当前已完成 </li>
 */

public class OfflineTaskCountStat {
    @ItemType(Long.class)
    private List<Long> totayTasksCount;
    @ItemType(Long.class)
    private List<Long> todayCompleteCount;

    public List<Long> getTotayTasksCount() {
        return totayTasksCount;
    }

    public void setTotayTasksCount(List<Long> totayTasksCount) {
        this.totayTasksCount = totayTasksCount;
    }

    public List<Long> getTodayCompleteCount() {
        return todayCompleteCount;
    }

    public void setTodayCompleteCount(List<Long> todayCompleteCount) {
        this.todayCompleteCount = todayCompleteCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
