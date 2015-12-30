package com.everhomes.rest.business;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>pageOffset : 页码</li>
 * <li>pageSize : 每页大小</li>
 * <li>keyword : 昵称关键字</li>
 * </ul>
 *
 */
public class ListBusinessByKeywordCommand {
	private Integer pageOffset;
    private Integer pageSize;
	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

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
