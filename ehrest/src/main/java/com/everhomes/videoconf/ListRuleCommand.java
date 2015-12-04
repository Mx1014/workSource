package com.everhomes.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>pageOffset: 页码，从1开始</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListRuleCommand {
	
	private Integer pageOffset;
    
    private Integer pageSize;

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
