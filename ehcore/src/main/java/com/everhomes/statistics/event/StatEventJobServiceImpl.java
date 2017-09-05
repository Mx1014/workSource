// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.statistics.event.StatEventStatTimeInterval;
import com.everhomes.rest.statistics.event.StatExecuteEventTaskCommand;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.statistics.event.step.EventStatPortalStatStep;
import com.everhomes.statistics.event.step.EventStatProcessContentLogStep;
import com.everhomes.statistics.event.step.EventStatStep;
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

    @Autowired
    private StatEventPortalStatisticProvider statEventPortalStatisticProvider;

    @Autowired
    private StatEventStatisticProvider statEventStatisticProvider;

    private final List<StatEventStep> steps = new LinkedList<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            stepInit();
            scheduleInit();
        }
    }

    private void stepInit() {
        EventStatProcessContentLogStep step1 = PlatformContext.getComponent(EventStatProcessContentLogStep.class);
        EventStatPortalStatStep step2 = PlatformContext.getComponent(EventStatPortalStatStep.class);
        EventStatStep step3 = PlatformContext.getComponent(EventStatStep.class);

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
    public void executeTask(StatEventTaskExecution taskExecution) {
        try {
            prepareExecute(taskExecution);
            for (StatEventStep step : steps) {
                step.execute(taskExecution);
            }
            taskExecution.setStatus(StatEventTaskExecution.Status.FINISH);
        } catch (Throwable t) {
            LOGGER.error("stat event step error", t);
            taskExecution.setStatus(StatEventTaskExecution.Status.ERROR);
            taskExecution.setT(t);
        } finally {
            doFinally(taskExecution);
        }
    }

    private void doFinally(StatEventTaskExecution taskExecution) {
        Map<String, StatEventStepExecution> stepExecutionMap = taskExecution.getStepExecutionMap();
        stepExecutionMap.forEach((k, v) -> {
            StatEventTaskLog taskLog = v.getTaskLog();
            if (taskLog == null) {
                taskLog = new StatEventTaskLog();
            }

            if (v.getT() != null) {
                try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                     PrintStream stream = new PrintStream(out)) {
                    v.getT().printStackTrace(stream);
                    String stacktrace = out.toString("UTF-8");
                    taskLog.setExceptionStacktrace(stacktrace);
                } catch (Exception ignored) { }
            } else {
                taskLog.setExceptionStacktrace(null);
            }

            taskLog.setStatus(v.getStatus().name());
            taskLog.setTaskDate(Date.valueOf(v.getTaskDate()));
            taskLog.setTaskMeta(StringHelper.toJsonString(v.getTaskMeta()));
            taskLog.setDurationSeconds(v.getDurationSeconds());
            taskLog.setStepName(k);

            statEventTaskLogProvider.createOrUpdateStatEventTaskLog(taskLog);
        });
    }

    private void prepareExecute(StatEventTaskExecution execution) {
        execution.setStatus(StatEventTaskExecution.Status.PROCESSING);
        // 手动触发执行的话就把之前计算的数据都删掉
        if (execution.isManuallyExecute()) {
            execution.getStepExecutionMap().clear();
            Date date = Date.valueOf(execution.getTaskDate());
            statEventPortalStatisticProvider.deleteEventPortalStatByDate(date);
            statEventStatisticProvider.deleteEventStatByDate(date);
            statEventTaskLogProvider.deleteEventTaskLogByDate(date);
        }
    }

    @Override
    public StatEventTaskExecution getTaskExecution(LocalDate statDate, boolean manuallyExecute) {
        Date date = Date.valueOf(statDate);
        StatEventTaskExecution taskExecution = new StatEventTaskExecution();
        taskExecution.getParameters().put("statDate", statDate);
        taskExecution.setTaskDate(statDate);
        taskExecution.setStatus(StatEventTaskExecution.Status.PROCESSING);
        taskExecution.setInterval(StatEventStatTimeInterval.DAILY);
        taskExecution.setManuallyExecute(manuallyExecute);

        Map<String, StatEventStepExecution> stepExecutionMap = new HashMap<>();
        List<StatEventTaskLog> taskLogs = statEventTaskLogProvider.findByTaskDate(date);

        for (StatEventTaskLog taskLog : taskLogs) {
            StatEventStepExecution stepExecution = new StatEventStepExecution();
            stepExecution.setTaskLog(taskLog);
            stepExecution.setParameters(taskExecution.getParameters());
            stepExecution.setStatus(StatEventStepExecution.Status.valueOf(taskLog.getStatus()));
            stepExecution.setTaskDate(statDate);
            stepExecution.setTaskMeta((Map<String, Object>) StringHelper.fromJsonString(taskLog.getTaskMeta(), HashMap.class));
            stepExecution.setStepName(taskLog.getStepName());
            stepExecution.setInterval(taskExecution.getInterval());
            stepExecutionMap.put(stepExecution.getStepName(), stepExecution);
        }

        taskExecution.setStepExecutionMap(stepExecutionMap);
        return taskExecution;
    }

    public void executeTask(StatExecuteEventTaskCommand cmd) {
        LocalDate startDate = LocalDate.parse(cmd.getStartDate(), DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate endDate = LocalDate.parse(cmd.getEndDate(), DateTimeFormatter.BASIC_ISO_DATE);

        if (startDate.isAfter(endDate)) {
            LocalDate tmp = startDate;
            startDate = endDate;
            endDate = tmp;
        }

        boolean manuallyExecute = cmd.getDeleteOld() != null && cmd.getDeleteOld().equals(TrueOrFalseFlag.TRUE.getCode());

        do {
            StatEventTaskExecution taskExecution = getTaskExecution(startDate, manuallyExecute);
            executeTask(taskExecution);
            startDate = startDate.plusDays(1);
        } while (startDate.isBefore(endDate));
    }
}