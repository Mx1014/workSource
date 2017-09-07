package com.everhomes.statistics.terminal;

import com.everhomes.rest.statistics.terminal.TerminalStatisticsTaskDTO;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.sms.DateUtil;
import com.everhomes.util.StringHelper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
@Scope("prototype")
public class StatTerminalScheduleJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatTerminalScheduleJob.class);

    public static final String SCHEDELE_NAME = "stat-terminal-";

    public static String CRON_EXPRESSION = "0 0 2 * * ?";

    public static String STAT_CRON_EXPRESSION = "terminal.statistics.cron.expression";

    @Autowired
    private StatTerminalService statTerminalService;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        LOGGER.debug("start schedele job, excute task date = {}", calendar.getTime());

        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            //执行任务区
            List<TerminalStatisticsTaskDTO> tasks = statTerminalService.executeStatTask(null, DateUtil.dateToStr(calendar.getTime(), DateUtil.YMR_SLASH), DateUtil.dateToStr(calendar.getTime(), DateUtil.YMR_SLASH));
            LOGGER.debug("schedele job result: {}", StringHelper.toJsonString(tasks));
        }
    }
}
