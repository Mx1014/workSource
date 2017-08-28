package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/26.
 */
public class StandardAndStatus {
    private Long standardId;

    @ItemType(Byte.class)
    private List<Byte> taskStatus;

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
    }

    public List<Byte> getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(List<Byte> taskStatus) {
        this.taskStatus = taskStatus;
    }
}
