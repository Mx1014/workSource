package com.everhomes.filedownload;

import java.util.Map;

public interface FileDownloadService {

    /**
     *
     * @param jobClass
     * @param params
     * @return
     */
    Long createJob(String jobClass, Map<String, String> params);

    void cancelJob(Long jobId);
    
}
