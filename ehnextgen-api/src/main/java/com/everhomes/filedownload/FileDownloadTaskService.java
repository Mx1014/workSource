package com.everhomes.filedownload;

import com.everhomes.rest.filedownload.ListFileDownloadTasksCommand;
import com.everhomes.rest.filedownload.ListFileDownloadTasksResponse;

import java.io.OutputStream;

public interface FileDownloadTaskService extends TaskService {

    /**
     * 管理后台查询下载任务记录
     * @param cmd
     * @return
     */
    ListFileDownloadTasksResponse listFileDownloadTasks(ListFileDownloadTasksCommand cmd);

    /**
     * 上传文件到contenServer
     * @param fileName
     * @param ops
     * @return
     */
    String uploadToContenServer(String fileName, OutputStream ops);
}
