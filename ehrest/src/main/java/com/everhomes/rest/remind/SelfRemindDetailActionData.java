package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>remindId: 我的日程ID</li>
 * </ul>
 */
public class SelfRemindDetailActionData {
    private Long remindId;

    public Long getRemindId() {
        return remindId;
    }

    public void setRemindId(Long remindId) {
        this.remindId = remindId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
