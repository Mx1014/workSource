package com.everhomes.filedownload;

import com.everhomes.rest.filedownload.*;

import java.util.Date;
import java.util.Map;

public interface JobService {

    /**
     * 创建任务
     * @param jobClass 实现了JobHandler的任务类
     * @param params 任务的业务参数
     * @param repeatFlag 是否支持重复执行 参考{@link JobRepeatFlag}
     * @param startTime 执行时间，为空时立刻加入任务队列
     * @return 任务id
     */
    Long createJob(Class jobClass, Map<String, Object> params, Byte repeatFlag, Date startTime);

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

}
