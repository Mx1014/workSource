package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>communityId: 园区id</li>
 *     <li>syncType: 同步类型 参考{@link com.everhomes.rest.customer.SyncDataTaskType}</li>
 *     <li>pageSize: 页面大小</li>
 *     <li>pageAnchor: 页码</li>
 * </ul>
 * Created by ying.xiong on 2018/1/16.
 */
public class ListCommunitySyncResultCommand {
    private Long communityId;

    private String syncType;

    private Integer pageSize;

    private Long pageAnchor;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }
}
