package com.everhomes.filedownload;

import java.util.List;

public interface FileDownloadService {

    /**
     * 新建下载文件
     * @param fileName    文件名称
     * @param rowCount    文件行总数
     * @param rows        数据
     * @param cellMappers 关系表
     * @return 下载任务id
     */
    Long createJob(String fileName, Integer rowCount, List<Object> rows, List<CellMapper> cellMappers);

    //List<FileDownloadJobDTO> listJob();

    void cancelJob(Long jobId);
    
}
