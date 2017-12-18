package com.everhomes.filedownload;

import com.everhomes.rest.filedownload.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface JobService {

    /**
     * 创建任务
     * @param name 实现了JobHandler的任务
     * @param type 任务类型 参考{@link JobType}
     * @param jobClass 实现了JobHandler的任务类
     * @param params 任务的业务参数
     * @param repeatFlag 是否支持重复执行 参考{@link JobRepeatFlag}
     * @param startTime 执行时间，为空时立刻加入任务队列
     * @return 任务id
     */
    Long createJob(String name, Byte type, Class jobClass, Map<String, Object> params, Byte repeatFlag, Date startTime);

    /**
     * 取消任务
     * @param jobId 任务进度
     */
    void cancelJob(Long jobId);

    /**
     * 更新任务状态及错误信息
     * @param jobId 任务id
     * @param status 任务状态 参考{@link JobStatus}
     * @param errorDesc 错误信息
     */
    void updateJobStatus(Long jobId, Byte status, String errorDesc);

    /**
     * 更新任务进度比例到redis和mysql
     * @param jobId 任务id
     * @param rate 任务进度百分数
     */
    void updateJobRate(Long jobId, Integer rate);

    List<Job> listJobs(Integer namespaceId, Long communityId, Long orgId, Long userId, Byte type, Byte status, Long pageAnchor, int count);

}
