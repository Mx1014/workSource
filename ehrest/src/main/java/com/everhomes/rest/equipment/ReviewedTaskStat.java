package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>qualifiedTasks：审阅合格任务数</li>
 *     <li>unqualifiedTasks：审阅不合格任务数</li>
 * </ul>
 * Created by ying.xiong on 2017/4/20.
 */
public class ReviewedTaskStat {

    private Long qualifiedTasks;

    private Long unqualifiedTasks;

    public Long getQualifiedTasks() {
        return qualifiedTasks;
    }

    public void setQualifiedTasks(Long qualifiedTasks) {
        this.qualifiedTasks = qualifiedTasks;
    }

    public Long getUnqualifiedTasks() {
        return unqualifiedTasks;
    }

    public void setUnqualifiedTasks(Long unqualifiedTasks) {
        this.unqualifiedTasks = unqualifiedTasks;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
