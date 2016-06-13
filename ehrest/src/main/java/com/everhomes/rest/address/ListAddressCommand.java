// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>communityId: 小区Id</li>
 * <li>pageOffset: 页码</li>
 * </ul>
 */
public class ListAddressCommand {
    Long communityId;
    
    // start from 1, page size is configurable at server side
    Long pageOffset;
    
    Long pageSize;
    
    public ListAddressCommand() {
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public void setPageOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Long getPageOffset() {
        return pageOffset;
    }

    public void setOffset(Long pageOffset) {
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
