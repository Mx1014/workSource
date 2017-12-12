package com.everhomes.filedownload;

import java.util.Map;

public interface FileDownloadHandler {
    /**
     * 下载任务的具体业务逻辑
     * 具体业务由业务自己实现
     * @param params  此处的参数需要在创建任务的时候传过来，并且文件下载中心会自动加上一个参数jobId
     * @return  载结果文件的uri
     */
    String run(Map<String, Object> params);
    
}
