package com.everhomes.filedownload;

import com.everhomes.rest.filedownload.ListFileDownloadJobsCommand;
import com.everhomes.rest.filedownload.ListFileDownloadJobsResponse;

public interface FileDownloadService extends JobService{


    /**
     * 管理后台查询下载任务记录
     * @param cmd
     * @return
     */
    ListFileDownloadJobsResponse listFileDownloadJobs(ListFileDownloadJobsCommand cmd);

    /**
     * 更新任务进度比例
     * @param jobId 任务id
     * @param rate 任务进度百分数
     */
    void updateJobRate(Long jobId, Integer rate);

}
