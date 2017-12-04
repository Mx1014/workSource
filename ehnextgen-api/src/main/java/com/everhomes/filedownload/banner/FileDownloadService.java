package com.everhomes.filedownload.banner;

import com.everhomes.rest.banner.*;
import com.everhomes.rest.banner.admin.*;
import com.everhomes.rest.ui.banner.GetBannersBySceneCommand;

import javax.servlet.http.HttpServletRequest;
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

    List<FileDownloadJobDTO> listJob();

    void cancelJob(Long jobId);
    
}
