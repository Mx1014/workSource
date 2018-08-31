// @formatter:off
package com.everhomes.rest.filedownload;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>taskId: taskId</li>
 * </ul>
 */
public class UpdateDownloadTimesCommand {

    private Long taskId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
