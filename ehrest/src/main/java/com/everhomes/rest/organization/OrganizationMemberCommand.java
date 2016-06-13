package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>organizationId: 组织id</li>
 * <li>memberId: 成员id</li>
 * </ul>
 */
public class OrganizationMemberCommand {
	
	private Long parentId;
	
	@NotNull
	private Long organizationId;
	@NotNull
	private Long memberId;
	
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
