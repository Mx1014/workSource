package com.everhomes.filedownload;

import java.util.List;

public interface FileDownloadService {

    /**
     * 新建下载文件
     * @param fileName    文件名称
     * @param rows        数据
     * @param cellMappers 关系表
     * @return 下载任务id
     */
    Long createJob(String fileName, List<Object> rows, List<CellMapper> cellMappers);

    //List<FileDownloadJobDTO> listJob();

    void cancelJob(Long jobId);
    
}
