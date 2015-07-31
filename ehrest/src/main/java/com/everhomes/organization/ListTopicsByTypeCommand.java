package com.everhomes.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>pageOffset : 页码</li>
 *	<li>pageSize : 页大小</li>
 *	<li>topicType : 帖类型</li>
 *	<li>organizationId : 组织id</li>
 *</ul>
 *
 */
public class ListTopicsByTypeCommand {
	
	private Long pageOffset;
	private Integer pageSize;
	@NotNull
	private String topicType;
	@NotNull
	private Long organizationId;
	
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
	public String getTopicType() {
		return topicType;
	}
	public void setTopicType(String topicType) {
		this.topicType = topicType;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	

}
