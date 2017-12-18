// @formatter:off
package com.everhomes.filedownload;


import com.everhomes.rest.filedownload.TaskStatus;
import com.everhomes.scheduler.TaskScheduleJob;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.user.UserContext;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.*;

@Component
@DependsOn("platformContext")
public class TaskServiceImpl implements TaskService, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);
    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private TaskProvider taskProvider;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //TODO 检查未启动、失败任务，再次启动
    }


    @Override
    public Long createTask(String name, Byte type, Class taskClass, Map<String, Object> params, Byte repeatFlag, Date startTime) {

        Task job = saveNewJob(name, taskClass.getName(), params, repeatFlag);
        scheduleJob(job);
        return null;
    }


    @Override
    public void updateTaskProcess(Long taskId, Integer rate) {

        //TODO  保存Redis 百分百则再保存一份到数据库



        Task task = taskProvider.findById(taskId);
        task.setRate(rate);
        taskProvider.updateTask(task);
    }


    @Override
    public void cancelTask(Long taskId) {

        Task task = taskProvider.findById(taskId);
        if(task == null){
            //TODO 任务不存在
            throw null;
        }

        Long userId = UserContext.currentUserId();

        if(userId ==null || !userId.equals(task.getUserId())){
            //TODO 权限不足
            throw null;
        }

        //TODO 找到任务将其取消
        scheduleProvider.listScheduleJobs();

        //
        updateTaskStatus(taskId, TaskStatus.CANCEL.getCode(),  null);
    }


    @Override
    public void updateTaskStatus(Long taskId, Byte status, String errorDesc) {
        Task task = taskProvider.findById(taskId);
        task.setStatus(status);
        if(TaskStatus.fromName(status) == TaskStatus.SUCCESS){
            task.setRate(100);
        }
        task.setErrorDescription(errorDesc);
        taskProvider.updateTask(task);
    }


    private Task saveNewJob(String name, String taskClassName, Map<String, Object> params, Byte repeatFlag){
        Task job = new Task();
        Long ownerId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        job.setNamespaceId(namespaceId);
        job.setUserId(ownerId);
        job.setName(name);
        job.setClassName(taskClassName);
        job.setParams(JSONObject.toJSONString(params));
        job.setRepeatFlag(repeatFlag);
        job.setStatus(TaskStatus.WAITING.getCode());
        job.setCreateTime(new Timestamp(System.currentTimeMillis()));
        taskProvider.createTask(job);
        return job;
    }

    private void scheduleJob(Task task){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("jobId", task.getId());
        parameters.put("fileName", task.getName());
        parameters.put("jobClassName", task.getClassName());
        parameters.put("jobParams", task.getParams());
        parameters.put("jobClassName", task.getClassName());
        parameters.put("jobParams", task.getParams());
        String taskName = "fileDownload_" + task.getId() + "_" + System.currentTimeMillis();
        scheduleProvider.scheduleSimpleJob(taskName,taskName, new Date(), TaskScheduleJob.class,parameters);
    }

    @Override
    public List<Task> listTasks(Integer namespaceId, Long communityId, Long orgId, Long userId, Byte type, Byte status, Long pageAnchor, int count) {
        return taskProvider.listTask(namespaceId, communityId, orgId, userId, type, status, pageAnchor, count);
    }
}
