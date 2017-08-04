// @formatter:off
package com.everhomes.statistics.event;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xq.tian on 2017/8/14.
 */
public class StatEventExecution {

    private Status status = Status.STOP;

    private LocalDate taskDate;
    private StatEventTaskLog taskLog;

    private Map<String, Object> parameters = new HashMap<>();
    private Map<String, Object> taskMeta = new HashMap<>();

    private Throwable t;

    public enum Status {
        STOP, START, PROCESSING, FINISH, ERROR
    }

    public <T> T getParam(String name) {
        Object o = parameters.get(name);
        if (o == null) {
            return null;
        }
        return (T) o;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Throwable getT() {
        return t;
    }

    public void setT(Throwable t) {
        this.t = t;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Map<String, Object> getTaskMeta() {
        return taskMeta;
    }

    public void setTaskMeta(Map<String, Object> taskMeta) {
        this.taskMeta = taskMeta;
    }

    public LocalDate getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }

    public StatEventTaskLog getTaskLog() {
        return taskLog;
    }

    public void setTaskLog(StatEventTaskLog taskLog) {
        this.taskLog = taskLog;
    }
}
