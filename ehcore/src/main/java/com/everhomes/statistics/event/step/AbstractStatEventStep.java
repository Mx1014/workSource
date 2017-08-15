// @formatter:off
package com.everhomes.statistics.event.step;

import com.everhomes.statistics.event.StatEventStep;
import com.everhomes.statistics.event.StatEventStepExecution;
import com.everhomes.statistics.event.StatEventTaskExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xq.tian on 2017/8/14.
 */
abstract public class AbstractStatEventStep implements StatEventStep {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStatEventStep.class);

    @Override
    public void execute(StatEventTaskExecution execution) {
        if (thisStepFinished(execution)) {
            LOGGER.warn("task [{}] step [{}] already finished", execution.getTaskDate(), getStepName());
            return;
        }

        try {
            before(execution);
            doExecute(getStepException(execution));
            after(getStepException(execution));
        } catch (Throwable t) {
            error(getStepException(execution), t);
            throw t;
        }
    }

    private void error(StatEventStepExecution stepExecution, Throwable t) {
        stepExecution.setT(t);
        stepExecution.setStatus(StatEventStepExecution.Status.ERROR);
        stepExecution.setEndTime(System.currentTimeMillis());
    }

    protected void before(StatEventTaskExecution execution) {
        StatEventStepExecution stepExecution = execution.getStepExecutionMap().computeIfAbsent(getStepName(), r -> new StatEventStepExecution());
        stepExecution.setInterval(execution.getInterval());
        stepExecution.setStatus(StatEventStepExecution.Status.PROCESSING);
        stepExecution.setParameters(execution.getParameters());
        stepExecution.setStepName(getStepName());
        stepExecution.setTaskDate(execution.getTaskDate());
        stepExecution.setStartTime(System.currentTimeMillis());
    }

    protected void after(StatEventStepExecution stepExecution) {
        stepExecution.setStatus(StatEventStepExecution.Status.FINISH);
        stepExecution.setEndTime(System.currentTimeMillis());
    }

    private boolean thisStepFinished(StatEventTaskExecution execution) {
        StatEventStepExecution stepExecution = getStepException(execution);
        return stepExecution != null && stepExecution.getStatus() == StatEventStepExecution.Status.FINISH;
    }

    @Override
    public String getStepName() {
        return getClass().getName();
    }

    @Override
    public StatEventStepExecution getStepException(StatEventTaskExecution taskExecution) {
        return taskExecution.getStepExecutionMap().get(getStepName());
    }

    public abstract void doExecute(StatEventStepExecution execution);
}
