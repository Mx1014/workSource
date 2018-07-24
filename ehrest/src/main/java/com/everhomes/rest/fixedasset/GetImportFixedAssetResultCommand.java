package com.everhomes.rest.fixedasset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>taskId: 任务id</li>
 * </ul>
 */
public class GetImportFixedAssetResultCommand {

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
