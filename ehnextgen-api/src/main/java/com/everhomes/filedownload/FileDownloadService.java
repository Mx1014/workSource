package com.everhomes.filedownload;

import com.everhomes.rest.filedownload.CancelJobCommand;
import com.everhomes.rest.filedownload.ListFileDownloadJobsCommand;
import com.everhomes.rest.filedownload.ListFileDownloadJobsResponse;

import java.util.Map;

public interface FileDownloadService {

    /**
     * 创建任务
     * @param fileName  下载文件名
     * @param jobClass  实现了FileDownloadHandler的任务类
     * @param params    任务参数，此参数会传到jobClass的run方法中，并且会多加一个参数jobId
     * @return
     */
    Long createJob(String fileName, Class jobClass, Map<String, Object> params);

    /**
     * 更新任务进度比例
     * @param jobId
     * @param rate
     */
    void updateJobProgressRate(Long jobId, Float rate);


    /**
     * 取消任务
     * @param cmd
     */
    void cancelJob(CancelJobCommand cmd);

    /**
     * 管理后台查询下载任务记录
     * @param cmd
     * @return
     */
    ListFileDownloadJobsResponse listFileDownloadJobs(ListFileDownloadJobsCommand cmd);
}
