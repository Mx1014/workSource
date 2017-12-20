package com.everhomes.filedownload;

import com.everhomes.rest.filedownload.*;

import java.io.OutputStream;
import java.util.List;

public interface FileDownloadTaskService{

    /**
     * 管理后台查询下载任务记录
     * @param cmd
     * @return
     */
    ListFileDownloadTasksResponse listFileDownloadTasks(ListFileDownloadTasksCommand cmd);

    /**
     * 取消任务
     * @param cmd 任务进度
     */
    void cancelTask(CancelTaskCommand cmd);

    /**
     * 上传文件到contenServer
     * @param fileName
     * @param ops
     * @return
     */
    String uploadToContenServer(String fileName, OutputStream ops);
}
