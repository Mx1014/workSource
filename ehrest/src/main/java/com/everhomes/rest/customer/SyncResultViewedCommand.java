package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>communityId: 园区id</li>
 *     <li>syncType: 同步类型 参考{@link com.everhomes.rest.customer.SyncDataTaskType}</li>
 * </ul>
 * Created by ying.xiong on 2018/4/8.
 */
public class SyncResultViewedCommand {
    private Long communityId;

    private String syncType;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }
}
