//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/3/14.
 */
/**
 *<ul>
 * <li>taskId:任务id</li>
 *</ul>
 */
public class BatchImportBillsResponse {
    private Long taskId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
