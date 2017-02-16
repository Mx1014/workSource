package com.everhomes.rest.pmtask;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>organizationId: 机构ID</li>
 * <li>keyword: 关键字</li>
 * </ul>
 */
public class ListAuthorizationCommunityCommand {
	
	@NotNull
    private String ownerType;

    @NotNull
    private Long ownerId;

    @NotNull
    private Long organizationId;
    
    private String keyword;

	private Byte checkPrivilegeFlag;

	public Byte getCheckPrivilegeFlag() {
		return checkPrivilegeFlag;
	}

	public void setCheckPrivilegeFlag(Byte checkPrivilegeFlag) {
		this.checkPrivilegeFlag = checkPrivilegeFlag;
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
