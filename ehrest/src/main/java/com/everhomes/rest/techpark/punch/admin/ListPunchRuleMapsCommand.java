package com.everhomes.rest.techpark.punch.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 
 * <li>ownerType：所属对象类型organization/user</li>
 * <li>ownerId：所属对象id</li>
 * <li>organizationId：过滤目标为人员的时候通过organizationId过滤</li>
 * <li>targetType：映射目标类型(规则是设置给谁的) organization/user</li>
 * <li>targetId：映射目标 id</li>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListPunchRuleMapsCommand {

	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;

	private Long organizationId;
	private String targetType;
	private Long targetId;

	private Long pageAnchor;
	private Integer pageSize;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
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

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}


	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
}
