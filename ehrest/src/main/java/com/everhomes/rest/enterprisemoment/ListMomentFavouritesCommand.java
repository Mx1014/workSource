// @formatter:off
package com.everhomes.rest.enterprisemoment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>enterpriseMomentId: 动态id</li>
 * <li>pageAnchor:  锚点</li>
 * <li>pageSize: 单页数量</li>
 * </ul>
 */
public class ListMomentFavouritesCommand {
	private Long organizationId;
	private Long enterpriseMomentId;
	private Long pageAnchor;
	private Integer pageSize;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getEnterpriseMomentId() {
		return enterpriseMomentId;
	}

	public void setEnterpriseMomentId(Long enterpriseMomentId) {
		this.enterpriseMomentId = enterpriseMomentId;
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
