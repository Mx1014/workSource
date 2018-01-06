package com.everhomes.filedownload;

import java.util.Map;

public interface TaskHandler {

    /**
     * 执行run方法之前执行的方法
     * @param params  创建任务时的业务参数 + taskId + name + status + process
     */
    void beforeExecute(Map<String, Object> params);

    /**
     * 任务的具体业务逻辑
     * 由于服务器重启等原因，失败的任务可能会被再次执行，具体业务需要根据任务启动前状态(status)和任务启动前状态执行进度(process)进行判断后续业务改怎么处理
     * @param params  创建任务时的业务参数 + taskId + name + status + process
     */
    void execute(Map<String, Object> params);

    /**
     * 提交任务方法，执行run完成后执行
     * @param params  创建任务时的业务参数 + taskId + name + status + process
     */
    void commit(Map<String, Object> params);

    /**
     * 所有操作执行后执行的方法
     * @param params  创建任务时的业务参数 + taskId + name + status + process
     */
    void afterExecute(Map<String, Object> params);

}
