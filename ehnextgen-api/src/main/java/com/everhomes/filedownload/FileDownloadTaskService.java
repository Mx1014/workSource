package com.everhomes.filedownload;

import com.everhomes.rest.contentserver.CsFileLocationDTO;
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
     * 上传文件到contenServer，fileName需要带上后缀，例如.xsl
     * @param fileName
     * @param ops
     * @return
     */
    CsFileLocationDTO uploadToContenServer(String fileName, OutputStream ops);
}
