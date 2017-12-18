package com.everhomes.scheduler;

import com.everhomes.filedownload.*;
import com.everhomes.rest.filedownload.TaskStatus;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.lang.Class.forName;


@Component
public class TaskScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskScheduleJob.class);

    @Autowired
    TaskService taskService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Long jobId = (Long)jobDataMap.get("jobId");
        String jobClass = (String)jobDataMap.get("jobClassName");
        String jobParams = (String)jobDataMap.get("jobParams");
        Map params = (JSONObject) JSONValue.parse(jobParams);

        try {

            //1、获取业务实现类
            TaskHandler handler = null;
            try {
                Class c1 = forName(jobClass);
                handler = (FileDownloadTaskHandler) c1.newInstance();
            }catch (Exception ex){
                taskService.updateTaskStatus(jobId, TaskStatus.FAIL.getCode(),"get TaskHandler implements class fail.");
                ex.printStackTrace();
                throw ex;
            }

            //2、更新任务为执行状态
            taskService.updateTaskStatus(jobId, TaskStatus.RUNNING.getCode(), null);

            //3、执行前置方法
            try{
                handler.beforeExecute(params);
            }catch (Exception ex){
                taskService.updateTaskStatus(jobId, TaskStatus.FAIL.getCode(),"excute beforeExecute method fail.");
                ex.printStackTrace();
                throw ex;
            }

            //4、执行业务方法
            try {
                handler.execute(params);
            }catch (Exception ex){
                taskService.updateTaskStatus(jobId, TaskStatus.FAIL.getCode(), "excute execute method fail.");
                ex.printStackTrace();
                throw ex;
            }

            //5、执行commit方法
            try{
                handler.commit(params);
            }catch (Exception ex){
                taskService.updateTaskStatus(jobId, TaskStatus.FAIL.getCode(),"excute commit method fail.");
                ex.printStackTrace();
                throw ex;
            }

            //6、执行后置方法
            try{
                handler.afterExecute(params);
            }catch (Exception ex){
                taskService.updateTaskStatus(jobId, TaskStatus.FAIL.getCode(),"excute afterExecute method fail.");
                ex.printStackTrace();
                throw ex;
            }

            //7、更新任务状态为完成
            taskService.updateTaskStatus(jobId, TaskStatus.SUCCESS.getCode(),null);


        } catch (Exception e) {
            //1、更新任务状态为失败
            taskService.updateTaskStatus(jobId, TaskStatus.FAIL.getCode(),  "unexpected exception.");
            e.printStackTrace();
        }
    }

}
