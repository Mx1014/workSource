// @formatter:off
package com.everhomes.rest.family.admin;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区Id(可选)</li>
 * <li>pageOffset: 页码(可选)</li>
 * <li>pageSize: 每页大小(可选)</li>
 * </ul>
 */
public class ListWaitApproveFamilyAdminCommand {
    
    private Long communityId;
    
    private Long pageOffset;
    
    private Long pageSize;

    public ListWaitApproveFamilyAdminCommand() {
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
