package com.everhomes.statistics.event;

import com.everhomes.rest.statistics.event.StatExecuteEventTaskCommand;

import java.time.LocalDate;

/**
 * Created by xq.tian on 2017/7/31.
 */
public interface StatEventJobService {

    void executeTask(StatEventTaskExecution taskExecution);

    StatEventTaskExecution getTaskExecution(LocalDate statDate, boolean manuallyExecute);

    void executeTask(StatExecuteEventTaskCommand cmd);
}
