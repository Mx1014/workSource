package com.everhomes.filedownload;

import java.util.Map;

public interface JobHandler {

    /**
     * 获取进度比例，下载中心会不定期获取该进度更新任务状态（推荐业务内部更新内存变量rate，下面方法获取rate）
     */
    Integer getRate();

    /**
     * 执行run方法之前执行的方法
     * @param params  创建任务时的业务参数 + jobId + status + rate
     */
    void beforeRun(Map<String, Object> params);

    /**
     * 任务的具体业务逻辑
     * 由于服务器重启等原因，失败的任务可能会被再次执行（），具体业务需要根据当前状态status（失败）和当前执行进度rate进行判断后续业务改怎么处理
     * @param params  创建任务时的业务参数 + jobId + status + rate
     */
    void run(Map<String, Object> params);

    /**
     * 提交任务方法，执行run完成后执行
     * @param params  创建任务时的业务参数 + jobId + status + rate
     */
    void commit(Map<String, Object> params);

    /**
     * 所有操作执行后执行的方法
     * @param params  创建任务时的业务参数 + jobId + status + rate
     */
    void afterRun(Map<String, Object> params);


    
}
