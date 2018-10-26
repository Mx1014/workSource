package com.everhomes.rest.organization;


import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>id : 机构id</li>
 * <li>groupTypes : 机构类型 参考{@link com.everhomes.rest.organization.OrganizationGroupType}</li>
 * <li>naviFlag : 机构类型 参考{@link com.everhomes.rest.organization.OrganizationNaviFlag}</li>
 * <li>keywords : 关键字搜索</li>
 *</ul>
 *
 */
public class OpenListAllChildrenOrganizationsCommand {
	
	@NotNull
	private Long id;
	
	@ItemType(String.class)
	private List<String> groupTypes;
	
	private Byte naviFlag;

	private String keywords;
	
	private Long pageAnchor;
	
	private Byte simpleFlag;
	
	private Integer pageSize;

	private Integer namespaceId;

	private String appKey;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getGroupTypes() {
		return groupTypes;
	}

	public void setGroupTypes(List<String> groupTypes) {
		this.groupTypes = groupTypes;
	}

	public Byte getNaviFlag() {
		return naviFlag;
	}

	public void setNaviFlag(Byte naviFlag) {
		this.naviFlag = naviFlag;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
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

	public Byte getSimpleFlag() {
		return simpleFlag;
	}

	public void setSimpleFlag(Byte simpleFlag) {
		this.simpleFlag = simpleFlag;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
}
