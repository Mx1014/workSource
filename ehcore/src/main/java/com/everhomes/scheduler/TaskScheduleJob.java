package com.everhomes.scheduler;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.filedownload.Task;
import com.everhomes.filedownload.TaskHandler;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.filedownload.TaskStatus;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;

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

import java.sql.Timestamp;
import java.util.Map;

import static java.lang.Class.forName;


@Component
public class TaskScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskScheduleJob.class);

    @Autowired
    TaskService taskService;
    @Autowired
    private ScheduleProvider scheduleProvider;
    @Autowired
    UserProvider userProvider;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        //双机判断
        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) != RunningFlag.TRUE) {
            return;
        }

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Long taskId = (Long)jobDataMap.get("taskId");
        String name = (String)jobDataMap.get("name");
        Byte status = (Byte)jobDataMap.get("status");
        Integer process = (Integer)jobDataMap.get("process");
        Integer ns = (Integer)jobDataMap.get("ns");
        Long userId = (Long)jobDataMap.get("userId");
        
        //setup user context, modify by janson
        if(ns != null && userId != null) {
        		User user = userProvider.findUserById(userId);
        		if(user != null) {
        			UserContext.current().setUser(user);
        			UserContext.current().setNamespaceId(ns);		
        		}
        }
        //end modify by janson

        String className = (String)jobDataMap.get("className");
        String paramsStr = (String)jobDataMap.get("params");
        Map params = (JSONObject) JSONValue.parse(paramsStr);
        params.put("taskId", taskId);
        params.put("name", name);
        params.put("status", status);
        params.put("process", process);

        try {

            //0、更新任务为开始执行
            Task task = taskService.findById(taskId);
            task.setExecuteStartTime(new Timestamp(System.currentTimeMillis()));
            taskService.updateTask(task);

            //1、获取业务实现类
            TaskHandler handler = null;
            try {
                Class c1 = forName(className);
//                handler = (FileDownloadTaskHandler) c1.newInstance();
                handler = (TaskHandler) PlatformContext.getComponent(c1);
            }catch (Exception ex){
                taskService.updateTaskStatus(taskId, TaskStatus.FAIL.getCode(),"get TaskHandler implements class fail. search log keyword: Task fail, taskId=" + taskId);
                throw ex;
            }

            //2、更新任务为执行状态
            taskService.updateTaskStatus(taskId, TaskStatus.RUNNING.getCode(), null);

            //3、执行前置方法
            try{
                handler.beforeExecute(params);
            }catch (Exception ex){
                taskService.updateTaskStatus(taskId, TaskStatus.FAIL.getCode(),"excute taskHandler.beforeExecute method fail. search log keyword: Task fail, taskId=" + taskId);
                throw ex;
            }

            //4、执行业务方法
            try {
                handler.execute(params);
            }catch (Exception ex){
                taskService.updateTaskStatus(taskId, TaskStatus.FAIL.getCode(), "execute taskHandler.execute method fail. search log keyword: Task fail, taskId=" + taskId);
                throw ex;
            }

            //5、执行commit方法
            try{
                handler.commit(params);
            }catch (Exception ex){
                taskService.updateTaskStatus(taskId, TaskStatus.FAIL.getCode(), "excute taskHandler.commit method fail. search log keyword: Task fail, taskId=" + taskId);
                throw ex;
            }

            //6、执行后置方法
            try{
                handler.afterExecute(params);
            }catch (Exception ex){
                taskService.updateTaskStatus(taskId, TaskStatus.FAIL.getCode(),"excute taskHandler.afterExecute method fail. search log keyword: Task fail, taskId=" + taskId);
                throw ex;
            }

            //7、更新任务状态为完成
            taskService.updateTaskStatus(taskId, TaskStatus.SUCCESS.getCode(),null);


        } catch (Exception ex) {
            LOGGER.error("Task fail, taskId={}, Exception", taskId, ex);
            ex.printStackTrace();
        }
    }

}
