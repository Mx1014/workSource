package com.everhomes.rest.launchpad;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>pageSize : 页大小</li>
 * <li>pageOffset : 当前页码</li>
 * <li>keyword : launchPadLayout关键字</li>
 * </ul>
 *
 */
public class ListLaunchPadLayoutCommand {
	
	private Integer pageSize;
	
	private Long pageOffset;
	
	private String keyword;
	
	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public Long getPageOffset() {
		return pageOffset;
	}
	
	public void setPageOffset(Long pageOffset) {
		this.pageOffset = pageOffset;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
