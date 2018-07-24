package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <p>移动端缓存排序的结果，定时批量请求排序</p>
 * <ul>
 * <li>ownerType: 默认EhOrganizations，不用填</li>
 * <li>ownerId: 总公司ID，必填</li>
 * <li>sortedRemindIds: 排过顺序的日程ID列表，必填</li>
 * </ul>
 */
public class BatchSortRemindCommand {
    private String ownerType;
    private Long ownerId;
    private List<Long> sortedRemindIds;

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

    public List<Long> getSortedRemindIds() {
        return sortedRemindIds;
    }

    public void setSortedRemindIds(List<Long> sortedRemindIds) {
        this.sortedRemindIds = sortedRemindIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
