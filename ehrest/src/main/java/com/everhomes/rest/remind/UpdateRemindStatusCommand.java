package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>remindId: 日程ID  </li>
 * <li>ownerType: 默认EhOrganizations，不用填</li>
 * <li>ownerId: 总公司ID，必填</li>
 * <li>status: 状态,参考{@link com.everhomes.rest.remind.RemindStatus}</li>
 * </ul>
 */
public class UpdateRemindStatusCommand {
    private Long remindId;
    private String ownerType;
    private Long ownerId;
    private Byte status;

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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
