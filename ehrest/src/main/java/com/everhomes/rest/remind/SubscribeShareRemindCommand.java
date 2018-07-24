package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>remindId: 日程ID  </li>
 * <li>remindUserId: 该日程的所有人的userId</li>
 * <li>ownerType: 默认EhOrganizations，不用填</li>
 * <li>ownerId: 总公司ID，必填</li>
 * </ul>
 */
public class SubscribeShareRemindCommand {
    private Long remindId;
    private Long remindUserId;
    private String ownerType;
    private Long ownerId;

    public Long getRemindId() {
        return remindId;
    }

    public void setRemindId(Long remindId) {
        this.remindId = remindId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
