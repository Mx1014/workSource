package com.everhomes.filedownload;

import com.everhomes.rest.filedownload.JobStatus;
import com.everhomes.rest.filedownload.ListFileDownloadJobsCommand;
import com.everhomes.rest.filedownload.ListFileDownloadJobsResponse;

import java.io.OutputStream;

public interface FileDownloadService extends JobService{

    /**
     * 管理后台查询下载任务记录
     * @param cmd
     * @return
     */
    ListFileDownloadJobsResponse listFileDownloadJobs(ListFileDownloadJobsCommand cmd);

    /**
     * 上传文件到contenServer
     * @param fileName
     * @param ops
     * @return
     */
    String uploadToContenServer(String fileName, OutputStream ops);
}
