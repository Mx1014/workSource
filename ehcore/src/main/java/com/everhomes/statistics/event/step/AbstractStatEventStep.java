// @formatter:off
package com.everhomes.statistics.event.step;

import com.everhomes.statistics.event.StatEventExecution;
import com.everhomes.statistics.event.StatEventStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xq.tian on 2017/8/14.
 */
abstract public class AbstractStatEventStep implements StatEventStep {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStatEventStep.class);

    @Override
    public void execute(StatEventExecution execution) {
        if (thisStepFinished(execution)) {
            LOGGER.warn("task [{}] step [{}] already finished", execution.getTaskDate(), thisStepName());
            return;
        }

        before(execution);
        doExecute(execution);
        after(execution);
    }

    protected void before(StatEventExecution execution) {
        //
    }

    protected void after(StatEventExecution execution) {
        execution.getTaskMeta().put(thisStepName(), 1);
    }

    private boolean thisStepFinished(StatEventExecution execution) {
        return execution.getTaskMeta().get(thisStepName()) != null
                && execution.getTaskMeta().get(thisStepName()).getClass().isInstance(Integer.class)
                && execution.getTaskMeta().get(thisStepName()).equals(1);
    }

    protected String thisStepName() {
        return getClass().getName();
    }

    public abstract void doExecute(StatEventExecution execution);
}
