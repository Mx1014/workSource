// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.scheduler.ScheduleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xq.tian on 2017/8/4.
 */
@Service
public class StatEventJobServiceImpl implements StatEventJobService {

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private StatEventLogContentProvider statEventLogContentProvider;

    @PostConstruct
    public void init() {
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

        loadLogContentToTable(statDate);
        // ......TODO
    }

    private void loadLogContentToTable(LocalDate statDate) {
        Timestamp minTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MIN));
        Timestamp maxTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MAX));

        List<StatEventLogContent> logContents = statEventLogContentProvider.listEventLogContent(minTime, maxTime);

        for (StatEventLogContent logContent : logContents) {
            String content = logContent.getContent();
            try (BufferedReader reader = new BufferedReader(new StringReader(content))) {
                String line = reader.readLine();




            } catch (IOException e) {

            }
        }
    }
}
