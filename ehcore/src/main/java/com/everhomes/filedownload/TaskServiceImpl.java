// @formatter:off
package com.everhomes.filedownload;


import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.filedownload.AllReadStatus;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskStatus;
import com.everhomes.rest.scheduler.ScheduleJobInfoDTO;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.TaskScheduleJob;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
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
public class TaskServiceImpl implements TaskService, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);
    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private TaskProvider taskProvider;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<Task> tasks = taskProvider.listWaitingAndRunningTask();

        //等待中的直接加入任务队列，执行中的按照约定加入队列或者标识为失败
        for(Task task: tasks){
            if(TaskStatus.fromCode(task.getStatus()) == TaskStatus.WAITING){
                scheduleTask(task);
            }else {
                if(TaskRepeatFlag.fromCode(task.getRepeatFlag()) == TaskRepeatFlag.REPEAT || TaskRepeatFlag.fromCode(task.getRepeatFlag()) == TaskRepeatFlag.RATE_REPEAT){
                    scheduleTask(task);
                }else {
                    updateTaskStatus(task.getId(), TaskStatus.FAIL.getCode(), "task is running when system onApplication, and it is no support repeat execute.");
                }
            }
        }
    }


    @Override
    public Long createTask(String name, Byte type, Class taskClass, Map<String, Object> params, Byte repeatFlag, Date startTime) {

        Task task = saveNewTask(name, type, taskClass.getName(), params, repeatFlag);
        scheduleTask(task);
        return task.getId();
    }


    @Override
    public void updateTaskProcess(Long taskId, Integer process) {

        Task task = taskProvider.findById(taskId);
        task.setProcess(process);
        taskProvider.updateTask(task);
    }


    @Override
    public void updateTask(Task task) {
        taskProvider.updateTask(task);
    }


    @Override
    public Task findById(Long taskId) {
       return taskProvider.findById(taskId);
    }


    @Override
    public void cancelTask(Long taskId) {

        Task task = taskProvider.findById(taskId);
        if(task == null){
            LOGGER.error("task not exists, taskId = {}", taskId);
            throw RuntimeErrorException.errorWith(TaskServiceErrorCode.SCOPE,
                    TaskServiceErrorCode.TASK_NOT_FOUND, "task not exists");
        }

        Long userId = UserContext.currentUserId();

        if(userId ==null || !userId.equals(task.getUserId())){
            LOGGER.error("insufficient privileges, taskId={}, currentUserId={}, taskUserId={}", taskId, userId, task.getUserId());
            throw RuntimeErrorException.errorWith(TaskServiceErrorCode.SCOPE,
                    TaskServiceErrorCode.INSUFFICIENT_PRIVILEGES, "insufficient privileges");
        }

        if(TaskStatus.SUCCESS == TaskStatus.fromCode(task.getStatus())){
            LOGGER.error("task already success, taskId={}", taskId);
            throw RuntimeErrorException.errorWith(TaskServiceErrorCode.SCOPE,
                    TaskServiceErrorCode.TASK_ALREADY_SUCCESS, "task already success");
        }

        //取消任务
        String taskName = "task_" + task.getType() + "_" + task.getId();
        boolean result = scheduleProvider.unscheduleJob(taskName);
        if(!result){
            LOGGER.error("task cancel fail, taskId={}", taskId);
            throw RuntimeErrorException.errorWith(TaskServiceErrorCode.SCOPE,
                    TaskServiceErrorCode.TASK_CANCEL_FAIL, "task cancel fail");
        }

        updateTaskStatus(taskId, TaskStatus.CANCEL.getCode(),  null);
    }


    @Override
    public void updateTaskStatus(Long taskId, Byte status, String errorDesc) {
        Task task = taskProvider.findById(taskId);
        //已经取消的不能再操作
        if(TaskStatus.fromCode(task.getStatus()) == TaskStatus.CANCEL){
            return;
        }
        task.setStatus(status);
        if(TaskStatus.fromCode(status) == TaskStatus.SUCCESS){
            task.setProcess(100);
            task.setFinishTime(new Timestamp(System.currentTimeMillis()));
        }
        task.setErrorDescription(errorDesc);
        taskProvider.updateTask(task);
    }


    private Task saveNewTask(String name, Byte type, String className, Map<String, Object> params, Byte repeatFlag){
        Task task = new Task();
        Long ownerId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        task.setNamespaceId(namespaceId);
        task.setUserId(ownerId);
        task.setName(name);
        task.setType(type);
        task.setClassName(className);
        task.setParams(JSONObject.toJSONString(params));
        task.setRepeatFlag(repeatFlag);
        task.setProcess(0);
        task.setStatus(TaskStatus.WAITING.getCode());
        task.setCreateTime(new Timestamp(System.currentTimeMillis()));
        task.setReadStatus(AllReadStatus.NOTALLREAD.getCode());
        task.setDownloadTimes(0);
        taskProvider.createTask(task);
        return task;
    }

    private void scheduleTask(Task task){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("taskId", task.getId());
        parameters.put("name", task.getName());
        parameters.put("status", task.getStatus());
        parameters.put("process", task.getProcess() == null ? 0: task.getProcess());
        parameters.put("className", task.getClassName());
        parameters.put("params", task.getParams());
        parameters.put("ns", task.getNamespaceId());
        parameters.put("userId", task.getUserId());
        String taskName = "task_" + task.getType() + "_" + task.getId();
        scheduleProvider.scheduleSimpleJob(taskName, taskName, new Date(), TaskScheduleJob.class, parameters);
    }

    @Override
    public List<Task> listTasks(Integer namespaceId, Long communityId, Long orgId, Long userId, Byte type, Byte status, String keyword, Long startTime, Long endTime,  Long pageAnchor, Integer count) {
        return taskProvider.listTask(namespaceId, communityId, orgId, userId, type, status, keyword, startTime, endTime, pageAnchor, count);
    }

    @Override
    public List<Long> listWaitingTaskIds() {
        return taskProvider.listWaitingTaskIds();
    }

    @Override
    public void processUpdateTask(Long taskId, CsFileLocationDTO fileLocationDTO){
        Task task = findById(taskId);
        if(fileLocationDTO != null && fileLocationDTO.getUri() != null){
            task.setProcess(100);
            task.setResultString1(fileLocationDTO.getUri());
            if(fileLocationDTO.getSize() != null){
                task.setResultLong1(fileLocationDTO.getSize().longValue());
            }
            updateTask(task);
        }
    }

    @Override
    public Integer countNotAllReadStatus(Long userId) {
        return this.taskProvider.countNotAllReadStatus(userId);
    }

    @Override
    public List<Task> listNotReadStatusTasks(Long userId) {
        return this.taskProvider.listNotReadStatusTasks(userId);
    }

}
