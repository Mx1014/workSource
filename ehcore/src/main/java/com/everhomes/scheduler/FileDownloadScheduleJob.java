package com.everhomes.scheduler;

import com.everhomes.filedownload.FileDownloadHandler;
import com.everhomes.util.StringHelper;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

import static java.lang.Class.forName;


@Component
public class FileDownloadScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloadScheduleJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        System.out.print("download file");
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String jobClass = (String)jobDataMap.get("jobClassName");
        String jobParams = (String)jobDataMap.get("jobParams");
        Map params = (JSONObject) JSONValue.parse(jobParams);

        try {
            Class c1 = forName(jobClass);
            FileDownloadHandler handler = (FileDownloadHandler) c1.newInstance();
            handler.run(params);
//            Method run = c1.getMethod("run", Map.class);
//            run.invoke(params);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
