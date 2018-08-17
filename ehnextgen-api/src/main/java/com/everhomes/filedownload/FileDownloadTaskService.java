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
     * 上传文件到contenServer，fileName需要带上后缀，例如.xsl，带上任务
     * @param fileName
     * @param ops
     * @param taskId 任务Id为非必传，传了的话会记录上传文件的开始时间和结束时间
     * @return
     */
    CsFileLocationDTO uploadToContenServer(String fileName, OutputStream ops, Long taskId);

    /**
     * 查询是否所有下载任务都为已阅读
     * @return
     */
    GetFileDownloadReadStatusResponse getFileDownloadReadStatus();

    /**
     * 查看下载任务后，将所有未阅读的下载任务置为已阅读.
     */
    void updateFileDownloadReadStatus();

    /**
     * 更新下载次数
     * @param cmd
     */
    void updateFileDownloadTimes(UpdateDownloadTimesCommand cmd);
}
