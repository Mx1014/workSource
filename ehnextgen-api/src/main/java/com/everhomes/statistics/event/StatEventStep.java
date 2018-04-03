package com.everhomes.statistics.event;

/**
 * Created by xq.tian on 2017/8/14.
 */
public interface StatEventStep {

    void execute(StatEventTaskExecution execution);

    String getStepName();

    StatEventStepExecution getStepException(StatEventTaskExecution taskExecution);
}
