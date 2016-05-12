// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId：政府机构id</li>
 * <li>isSignedup: 是否左邻注册用户</li>
 * <li>pageAnchor: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListOrganizationContactCommand {
	@NotNull
	private Long    organizationId;
	private Byte isSignedup;
	private Integer pageOffset;
	private Long pageAnchor; 
	private Integer pageSize;
	
	private String keywords;
	
	public ListOrganizationContactCommand() {
    }

	
	public Long getOrganizationId() {
		return organizationId;
	}


	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Byte getIsSignedup() {
		return isSignedup;
	}

	public void setIsSignedup(Byte isSignedup) {
		this.isSignedup = isSignedup;
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

	public Long getPageAnchor() {
		return pageAnchor;
	}


	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}


	public String getKeywords() {
		return keywords;
	}


	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
