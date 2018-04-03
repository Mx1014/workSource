package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>pageAnchor: pageAnchor</li>
 *     <li>pageSize: pageSize</li>
 *     <li>keyword: keyword</li>
 *     <li>orgId: orgId</li>
 * </ul>
 */
public class ListCommunitiesByOrgIdCommand {

	private Long pageAnchor;

	private Integer pageSize;

	private String keyword;
	@NotNull
	private Long orgId;

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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


}
