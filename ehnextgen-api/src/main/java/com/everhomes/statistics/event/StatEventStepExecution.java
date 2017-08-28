// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xq.tian on 2017/8/14.
 */
public class StatEventStepExecution extends StatEventTaskExecution {

    private String stepName;

    private StatEventTaskLog taskLog;

    private long startTime;
    private long endTime;

    private Map<String, Object> taskMeta = new HashMap<>();

    public <T> T getParam(String name) {
        Object o = parameters.get(name);
        if (o == null) {
            return null;
        }
        return (T) o;
    }

    public Integer getDurationSeconds() {
        return Math.toIntExact((endTime - startTime) / 1000);
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public StatEventTaskLog getTaskLog() {
        return taskLog;
    }

    public void setTaskLog(StatEventTaskLog taskLog) {
        this.taskLog = taskLog;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Map<String, Object> getTaskMeta() {
        return taskMeta;
    }

    public void setTaskMeta(Map<String, Object> taskMeta) {
        this.taskMeta = taskMeta;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
