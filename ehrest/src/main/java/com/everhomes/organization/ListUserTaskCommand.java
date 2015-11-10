package com.everhomes.organization;

/**
 * <ul>
 * 	<li>pageOffset : 页码</li>
 *	<li>pageSize : 页大小</li>
 *</ul>
 *
 */
public class ListUserTaskCommand {

	private Long pageOffset;
	
	private Integer pageSize;

	public Long getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Long pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
}
