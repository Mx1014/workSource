// @formatter:off
package com.everhomes.filedownload;


import com.everhomes.rest.filedownload.JobStatus;
import com.everhomes.scheduler.CenterScheduleJob;
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
public class JobServiceImpl implements JobService, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);
    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private JobProvider jobProvider;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //TODO 检查未启动、失败任务，再次启动
    }


    @Override
    public Long createJob(String name, Byte type, Class jobClass, Map<String, Object> jobParams, Byte repeatFlag, Date startTime) {

        Job job = saveNewJob(name, jobClass.getName(), jobParams, repeatFlag);
        scheduleJob(job);
        return null;
    }


    @Override
    public void updateJobRate(Long jobId, Integer rate) {

        //TODO  保存Redis 百分百则再保存一份到数据库



        Job job = jobProvider.findById(jobId);
        job.setRate(rate);
        jobProvider.updateJob(job);
    }


    @Override
    public void cancelJob(Long jobId) {

        Job job = jobProvider.findById(jobId);
        if(job == null){
            //TODO 任务不存在
            throw null;
        }

        Long userId = UserContext.currentUserId();

        if(userId ==null || !userId.equals(job.getUserId())){
            //TODO 权限不足
            throw null;
        }

        //TODO 找到任务将其取消
        scheduleProvider.listScheduleJobs();

        //
        updateJobStatus(jobId, JobStatus.CANCEL.getCode(),  null);
    }


    @Override
    public void updateJobStatus(Long jobId, Byte status, String errorDesc) {
        Job job = jobProvider.findById(jobId);
        job.setStatus(status);
        if(JobStatus.fromName(status) == JobStatus.SUCCESS){
            job.setRate(100);
        }
        job.setErrorDescription(errorDesc);
        jobProvider.updateJob(job);
    }


    private Job saveNewJob(String name, String jobClassName, Map<String, Object> jobParams, Byte repeatFlag){
        Job job = new Job();
        Long ownerId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        job.setNamespaceId(namespaceId);
        job.setUserId(ownerId);
        job.setName(name);
        job.setClassName(jobClassName);
        job.setParams(JSONObject.toJSONString(jobParams));
        job.setRepeatFlag(repeatFlag);
        job.setStatus(JobStatus.WAITING.getCode());
        job.setCreateTime(new Timestamp(System.currentTimeMillis()));
        jobProvider.createJob(job);
        return job;
    }

    private void scheduleJob(Job job){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("jobId", job.getId());
        parameters.put("fileName", job.getName());
        parameters.put("jobClassName", job.getClassName());
        parameters.put("jobParams", job.getParams());
        parameters.put("jobClassName", job.getClassName());
        parameters.put("jobParams", job.getParams());
        String jobName = "fileDownload_" + job.getId() + "_" + System.currentTimeMillis();
        scheduleProvider.scheduleSimpleJob(jobName,jobName, new Date(), CenterScheduleJob.class,parameters);
    }

    @Override
    public List<Job> listJobs(Integer namespaceId, Long communityId, Long orgId, Long userId, Byte type, Byte status, Long pageAnchor, int count) {
        return jobProvider.listJobs(namespaceId, communityId, orgId, userId, type, status, pageAnchor, count);
    }
}
