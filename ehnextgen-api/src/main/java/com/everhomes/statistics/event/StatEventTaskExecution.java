// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.rest.statistics.event.StatEventStatTimeInterval;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xq.tian on 2017/8/14.
 */
public class StatEventTaskExecution {

    protected Status status = Status.STOP;

    protected LocalDate taskDate;

    // stemName => stepEx
    private Map<String, StatEventStepExecution> stepExecutionMap = new HashMap<>();

    protected Map<String, Object> parameters = new HashMap<>();

    protected Throwable t;

    protected StatEventStatTimeInterval interval;

    protected boolean manuallyExecute = false;

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

    public LocalDate getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }

    public Map<String, StatEventStepExecution> getStepExecutionMap() {
        return stepExecutionMap;
    }

    public StatEventStatTimeInterval getInterval() {
        return interval;
    }

    public void setInterval(StatEventStatTimeInterval interval) {
        this.interval = interval;
    }

    public void setStepExecutionMap(Map<String, StatEventStepExecution> stepExecutionMap) {
        this.stepExecutionMap = stepExecutionMap;
    }

    public boolean isManuallyExecute() {
        return manuallyExecute;
    }

    public void setManuallyExecute(boolean manuallyExecute) {
        this.manuallyExecute = manuallyExecute;
    }
}
