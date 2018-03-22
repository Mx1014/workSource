package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 分类ID，必填</li>
 * <li>ownerType: 默认EhOrganizations，不用填</li>
 * <li>ownerId: 总公司ID，必填</li>
 * <li>userId: 日程所有人的userId，不填则默认当前用户</li>
 * </ul>
 */
public class GetRemindCommand {
    private Long id;
    private String ownerType;
    private Long ownerId;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
