// @formatter:off
package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 公司id</li>
 * <li>pageAnchor: 分页锚点</li>
 * <li>pageSize: 每页条数</li>
 * </ul>
 */
public class ListWelfaresCommand {

	private Long organizationId;

	private Long pageAnchor;

	private Integer pageSize;

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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

}
