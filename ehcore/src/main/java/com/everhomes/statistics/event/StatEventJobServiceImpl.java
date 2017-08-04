// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.statistics.event.StatExecuteEventTaskCommand;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.statistics.event.step.StepEventStatistic;
import com.everhomes.statistics.event.step.StepLoadContentLogToTable;
import com.everhomes.statistics.event.step.StepPortalStatistic;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by xq.tian on 2017/8/4.
 */
@Service
public class StatEventJobServiceImpl implements StatEventJobService, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatEventJobServiceImpl.class);

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private StatEventTaskLogProvider statEventTaskLogProvider;

    private final List<StatEventStep> steps = new LinkedList<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            stepInit();
            scheduleInit();
        }
    }

    private void stepInit() {
        StepLoadContentLogToTable step1 = PlatformContext.getComponent(StepLoadContentLogToTable.class);
        StepPortalStatistic step2 = PlatformContext.getComponent(StepPortalStatistic.class);
        StepEventStatistic step3 = PlatformContext.getComponent(StepEventStatistic.class);

        steps.add(step1);
        steps.add(step2);
        steps.add(step3);
    }

    private void scheduleInit() {
        String triggerName = "StatEventJobTrigger-" + System.currentTimeMillis();
        String jobName = "StaEventJobName-" + System.currentTimeMillis();
        String cronExpression = "0 0 3 * * ?";
        scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, StatEventJob.class, new HashMap());
    }

    @Override
    public void executeTask(LocalDate statDate) {
        if (statDate == null) {
            statDate = LocalDate.now();
        }

        StatEventExecution execution = execution(statDate);
        //////////////////////////////////////////////
        // if (execution.getStatus() == StatEventExecution.Status.FINISH) {
        //     LOGGER.warn("stat event task {} already finished", statDate);
        //     return;
        // }
        //////////////////////////////////////////////
        long start = System.currentTimeMillis();
        try {
            for (StatEventStep step : steps) {
                try {
                    prepareExecute(execution, step);
                    step.execute(execution);
                } catch (Throwable t) {
                    LOGGER.error("stat event step error", t);
                    execution.setStatus(StatEventExecution.Status.ERROR);
                    execution.setT(t);
                    throw t;
                }
            }
            execution.setStatus(StatEventExecution.Status.FINISH);
        } finally {
            StatEventTaskLog taskLog = execution.getTaskLog();
            if (execution.getTaskLog() == null) {
                taskLog = new StatEventTaskLog();
            }
            taskLog.setTaskDate(Date.valueOf(statDate));
            taskLog.setStatus(execution.getStatus().name());
            taskLog.setTaskMeta(StringHelper.toJsonString(execution.getTaskMeta()));

            if (execution.getT() != null) {
                try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                     PrintStream stream = new PrintStream(out))
                {
                    execution.getT().printStackTrace(stream);
                    String stacktrace = out.toString("UTF-8");
                    taskLog.setExceptionStacktrace(stacktrace);
                } catch (Exception ignored) { }
            }
            long end = System.currentTimeMillis();
            long duration = (end - start) / 1000;
            taskLog.setDurationSeconds((int) duration);
            statEventTaskLogProvider.createOrUpdateStatEventTaskLog(taskLog);
        }
    }

    private void prepareExecute(StatEventExecution execution, StatEventStep step) {
        execution.setStatus(StatEventExecution.Status.PROCESSING);
    }

    private StatEventExecution execution(LocalDate statDate) {
        StatEventExecution execution = new StatEventExecution();
        execution.getParameters().put("statDate", statDate);
        StatEventTaskLog taskLog = statEventTaskLogProvider.findByTaskDate(Date.valueOf(statDate));
        if (taskLog != null) {
            execution.setTaskMeta((Map<String, Object>) StringHelper.fromJsonString(taskLog.getTaskMeta(), HashMap.class));
            execution.setStatus(StatEventExecution.Status.valueOf(taskLog.getStatus()));
            execution.setTaskLog(taskLog);
        }
        return execution;
    }

    public void executeTask(StatExecuteEventTaskCommand cmd) {
        LocalDate startDate = LocalDate.parse(cmd.getStartDate(), DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate endDate = LocalDate.parse(cmd.getEndDate(), DateTimeFormatter.BASIC_ISO_DATE);

        if (startDate.isAfter(endDate)) {
            LocalDate tmp = startDate;
            startDate = endDate;
            endDate = tmp;
        }

        do {
            executeTask(startDate);
            startDate = startDate.plusDays(1);
        } while (startDate.isBefore(endDate));
    }
}