package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>status: 处理状态， 0-未处理， 1-已处理
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListFeedbacksCommand {
	private Byte status;
	private Long pageAnchor;
    private Integer pageSize;

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
