package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>remindId: 同事的日程ID</li>
 * <li>remindUserId: 同事的userId</li>
 * <li>organizationId: 总公司ID</li>
 * </ul>
 */
public class TrackRemindDetailActionData {
    private Long remindId;
    private Long remindUserId;
    private Long organizationId;

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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
