package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>remindId: 同事的日程ID</li>
 * <li>remindUserId: 同事的userId</li>
 * </ul>
 */
public class TrackRemindDetailActionData {
    private Long remindId;
    private Long remindUserId;

    public Long getRemindId() {
        return remindId;
    }

    public void setRemindId(Long remindId) {
        this.remindId = remindId;
    }

    public Long getRemindUserId() {
        return remindUserId;
    }

    public void setRemindUserId(Long remindUserId) {
        this.remindUserId = remindUserId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
