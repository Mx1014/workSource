// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId：政府机构id</li>
 * <li>isSignedup: 是否左邻注册用户</li>
 * <li>status: 状态:1-待认证 3-已同意 0-已拒绝</li>
 * <li>pageAnchor: 页码</li>
 * <li>pageSize: 每页大小</li>
 * <li>targetTypes: 是否注册{@link com.everhomes.rest.organization.OrganizationMemberTargetType} </li>
 * <li>filterScopeTypes: 过滤范围类型{@link com.everhomes.rest.organization.FilterOrganizationContactScopeType}</li>
 * </ul>
 */
public class ListOrganizationContactCommand {
	@NotNull
	private Long    organizationId;
	private Byte isSignedup;
	private Byte status;
	private Integer pageOffset;
	private Long pageAnchor; 
	private Integer pageSize;
	
	private String keywords;

	private Byte visibleFlag;

	@ItemType(String.class)
	private List<String> targetTypes;

	@ItemType(String.class)
	private List<String> filterScopeTypes;
	
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

	public Byte getVisibleFlag() {
		return visibleFlag;
	}

	public void setVisibleFlag(Byte visibleFlag) {
		this.visibleFlag = visibleFlag;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public Byte getStatus() {
		return status;
	}


	public void setStatus(Byte status) {
		this.status = status;
	}

	public List<String> getTargetTypes() {
		return targetTypes;
	}

	public void setTargetTypes(List<String> targetTypes) {
		this.targetTypes = targetTypes;
	}

	public List<String> getFilterScopeTypes() {
		return filterScopeTypes;
	}

	public void setFilterScopeTypes(List<String> filterScopeTypes) {
		this.filterScopeTypes = filterScopeTypes;
	}
}
